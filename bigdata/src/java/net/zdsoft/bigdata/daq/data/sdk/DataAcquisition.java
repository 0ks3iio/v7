package net.zdsoft.bigdata.daq.data.sdk;

import java.io.ByteArrayOutputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.Writer;
import java.nio.channels.FileChannel;
import java.nio.channels.FileLock;
import java.nio.charset.Charset;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.ListIterator;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.regex.Pattern;
import java.util.zip.GZIPOutputStream;

import net.zdsoft.bigdata.daq.data.sdk.exceptions.DaqInvalidArgumentException;
import net.zdsoft.bigdata.daq.data.sdk.util.DaqBase64Coder;

import org.apache.http.HttpResponse;
import org.apache.http.NameValuePair;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.client.methods.HttpUriRequest;
import org.apache.http.impl.client.HttpClientBuilder;
import org.apache.http.message.BasicNameValuePair;
import org.apache.http.util.EntityUtils;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.PropertyNamingStrategy;

/**
 * Wanpeng 数据采集(data acquisition) SDK
 */
public class DataAcquisition {

	private final static String SDK_VERSION = "1.0.0";

	private final static Pattern KEY_PATTERN = Pattern
			.compile(
					"^((?!^original_id$|^time$|^properties$|^id$|^users$|^events$|^event$|^user_id$|^date$|^datetime$)[a-zA-Z_$][a-zA-Z\\d_$]{0,99})$",
					Pattern.CASE_INSENSITIVE);

	private final Consumer consumer;

	private final Map<String, Object> superProperties;

	private boolean enableTimeFree = false;

	public boolean isEnableTimeFree() {
		return enableTimeFree;
	}

	public void setEnableTimeFree(boolean enableTimeFree) {
		this.enableTimeFree = enableTimeFree;
	}

	private interface Consumer {
		void send(Map<String, Object> message);

		void flush();

		void close();
	}

	public static class BatchConsumer implements Consumer {

		public BatchConsumer(final String serverUrl) {
			this(serverUrl, MAX_FLUSH_BULK_SIZE);
		}

		public BatchConsumer(final String serverUrl, final int bulkSize) {
			this(serverUrl, bulkSize, false);
		}

		public BatchConsumer(final String serverUrl, final int bulkSize,
				final boolean throwException) {
			this.messageList = new LinkedList<Map<String, Object>>();
			this.httpConsumer = new HttpConsumer(serverUrl, null);
			this.jsonMapper = getJsonObjectMapper();
			this.bulkSize = Math.min(MAX_FLUSH_BULK_SIZE, bulkSize);
			this.throwException = throwException;
		}

		@Override
		public void send(Map<String, Object> message) {
			synchronized (messageList) {
				messageList.add(message);
				if (messageList.size() >= bulkSize) {
					flush();
				}
			}
		}

		@Override
		public void flush() {
			synchronized (messageList) {
				while (!messageList.isEmpty()) {
					String sendingData = null;
					List<Map<String, Object>> sendList = messageList.subList(0,
							Math.min(bulkSize, messageList.size()));
					try {
						sendingData = jsonMapper.writeValueAsString(sendList);
					} catch (JsonProcessingException e) {
						sendList.clear();
						if (throwException) {
							throw new RuntimeException(
									"Failed to serialize data.", e);
						}
						continue;
					}

					try {
						this.httpConsumer.consume(sendingData);
						sendList.clear();
					} catch (Exception e) {
						if (throwException) {
							throw new RuntimeException(
									"Failed to dump message with BatchConsumer.",
									e);
						}
						return;
					}
				}
			}
		}

		@Override
		public void close() {
			flush();
		}

		private final static int MAX_FLUSH_BULK_SIZE = 50;
		private final List<Map<String, Object>> messageList;
		private final HttpConsumer httpConsumer;
		private final ObjectMapper jsonMapper;
		private final int bulkSize;
		private final boolean throwException;
	}

	public static class ConsoleConsumer implements Consumer {

		public ConsoleConsumer(final Writer writer) {
			this.jsonMapper = getJsonObjectMapper();
			this.writer = writer;
		}

		@Override
		public void send(Map<String, Object> message) {
			try {
				synchronized (writer) {
					writer.write(jsonMapper.writeValueAsString(message));
					writer.write("\n");
				}
			} catch (IOException e) {
				throw new RuntimeException(
						"Failed to dump message with ConsoleConsumer.", e);
			}
		}

		@Override
		public void flush() {
			synchronized (writer) {
				try {
					writer.flush();
				} catch (IOException e) {
					throw new RuntimeException(
							"Failed to flush with ConsoleConsumer.", e);
				}
			}
		}

		@Override
		public void close() {
			flush();
		}

		private final ObjectMapper jsonMapper;
		private final Writer writer;
	}

	public static class ConcurrentLoggingConsumer extends InnerLoggingConsumer {

		public ConcurrentLoggingConsumer(final String filenamePrefix)
				throws IOException {
			this(filenamePrefix, 8192);
		}

		public ConcurrentLoggingConsumer(String filenamePrefix, int bufferSize)
				throws IOException {
			super(new LoggingFileWriterFactory() {
				@Override
				public LoggingFileWriter getFileWriter(String fileName,
						String scheduleFileName) throws FileNotFoundException {
					return InnerLoggingFileWriter.getInstance(scheduleFileName);
				}

				@Override
				public void closeFileWriter(LoggingFileWriter writer) {
					ConcurrentLoggingConsumer.InnerLoggingFileWriter
							.removeInstance((ConcurrentLoggingConsumer.InnerLoggingFileWriter) writer);
				}
			}, filenamePrefix, bufferSize);
		}

		static class InnerLoggingFileWriter implements LoggingFileWriter {

			private final String fileName;
			private final FileOutputStream outputStream;
			private int refCount;

			private static final Map<String, InnerLoggingFileWriter> instances;

			static {
				instances = new HashMap<String, InnerLoggingFileWriter>();
			}

			static InnerLoggingFileWriter getInstance(final String fileName)
					throws FileNotFoundException {
				synchronized (instances) {
					if (!instances.containsKey(fileName)) {
						instances.put(fileName, new InnerLoggingFileWriter(
								fileName));
					}

					InnerLoggingFileWriter writer = instances.get(fileName);
					writer.refCount = writer.refCount + 1;
					return writer;
				}
			}

			static void removeInstance(final InnerLoggingFileWriter writer) {
				synchronized (instances) {
					writer.refCount = writer.refCount - 1;
					if (writer.refCount == 0) {
						writer.close();
						instances.remove(writer.fileName);
					}
				}
			}

			private InnerLoggingFileWriter(final String fileName)
					throws FileNotFoundException {
				this.outputStream = new FileOutputStream(fileName, true);
				this.fileName = fileName;
				this.refCount = 0;
			}

			public void close() {
				try {
					outputStream.close();
				} catch (Exception e) {
					throw new RuntimeException("fail to close output stream.",
							e);
				}
			}

			public boolean isValid(final String fileName) {
				return this.fileName.equals(fileName);
			}

			public boolean write(final StringBuilder sb) {
				synchronized (this.outputStream) {
					FileLock lock = null;
					try {
						final FileChannel channel = outputStream.getChannel();
						lock = channel.lock(0, Long.MAX_VALUE, false);
						outputStream.write(sb.toString().getBytes("UTF-8"));
					} catch (Exception e) {
						throw new RuntimeException("fail to write file.", e);
					} finally {
						if (lock != null) {
							try {
								lock.release();
							} catch (IOException e) {
								throw new RuntimeException(
										"fail to release file lock.", e);
							}
						}
					}
				}
				return true;
			}
		}
	}

	interface LoggingFileWriter {
		boolean isValid(final String fileName);

		boolean write(final StringBuilder sb);

		void close();
	}

	interface LoggingFileWriterFactory {
		LoggingFileWriter getFileWriter(final String fileName,
				final String scheduleFileName) throws FileNotFoundException;

		void closeFileWriter(LoggingFileWriter writer);
	}

	static class InnerLoggingConsumer implements Consumer {

		private final static int BUFFER_LIMITATION = 1 * 1024 * 1024 * 1024; // 1G

		private final ObjectMapper jsonMapper;
		private final String filenamePrefix;
		private final StringBuilder messageBuffer;
		private final int bufferSize;
		private final SimpleDateFormat simpleDateFormat = new SimpleDateFormat(
				"yyyy-MM-dd");

		private final LoggingFileWriterFactory fileWriterFactory;
		private LoggingFileWriter fileWriter;

		public InnerLoggingConsumer(LoggingFileWriterFactory fileWriterFactory,
				String filenamePrefix, int bufferSize) throws IOException {
			this.fileWriterFactory = fileWriterFactory;
			this.filenamePrefix = filenamePrefix;
			this.jsonMapper = getJsonObjectMapper();
			this.messageBuffer = new StringBuilder(bufferSize);
			this.bufferSize = bufferSize;
		}

		@Override
		public synchronized void send(Map<String, Object> message) {
			if (messageBuffer.length() < BUFFER_LIMITATION) {
				try {
					messageBuffer
							.append(jsonMapper.writeValueAsString(message));
					messageBuffer.append("\n");
				} catch (JsonProcessingException e) {
					throw new RuntimeException("fail to process json", e);
				}
			} else {
				throw new RuntimeException(
						"logging buffer exceeded the allowed limitation.");
			}

			if (messageBuffer.length() >= bufferSize) {
				flush();
			}
		}

		private String constructFileName(Date now) {
			return filenamePrefix + "." + simpleDateFormat.format(now);
		}

		@Override
		public synchronized void flush() {
			if (messageBuffer.length() == 0) {
				return;
			}

			String filename = constructFileName(new Date());

			if (fileWriter != null && !fileWriter.isValid(filename)) {
				this.fileWriterFactory.closeFileWriter(fileWriter);
				fileWriter = null;
			}

			if (fileWriter == null) {
				try {
					fileWriter = this.fileWriterFactory.getFileWriter(
							filenamePrefix, filename);
				} catch (FileNotFoundException e) {
					throw new RuntimeException(e);
				}
			}

			if (fileWriter.write(messageBuffer)) {
				messageBuffer.setLength(0);
			}
		}

		@Override
		public synchronized void close() {
			flush();
			if (fileWriter != null) {
				this.fileWriterFactory.closeFileWriter(fileWriter);
				fileWriter = null;
			}
		}
	}

	public DataAcquisition(final Consumer consumer) {
		this.consumer = consumer;

		this.superProperties = new ConcurrentHashMap<String, Object>();
		clearSuperProperties();
	}

	/**
	 * 设置每个事件都带有的一些公共属性
	 *
	 * 当track的Properties，superProperties和SDK自动生成的automaticProperties有相同的key时，
	 * 遵循如下的优先级： track.properties 高于 superProperties 高于 automaticProperties
	 *
	 * 另外，当这个接口被多次调用时，是用新传入的数据去merge先前的数据
	 *
	 * 例如，在调用接口前，dict是 {"a":1, "b": "bbb"}，传入的dict是 {"b": 123, "c":
	 * "asd"}，则merge后 的结果是 {"a":1, "b": 123, "c": "asd"}
	 *
	 * @param superPropertiesMap
	 *            一个或多个公共属性
	 */
	public void registerSuperProperties(Map<String, Object> superPropertiesMap) {
		for (Map.Entry<String, Object> item : superPropertiesMap.entrySet()) {
			this.superProperties.put(item.getKey(), item.getValue());
		}
	}

	/**
	 * 清除公共属性
	 */
	public void clearSuperProperties() {
		this.superProperties.clear();
		this.superProperties.put("$lib", "Java");
		this.superProperties.put("$lib_version", SDK_VERSION);
	}

	/**
	 * 记录一个没有任何属性的事件
	 *
	 * @param userId
	 *            用户 ID
	 * @param anonymous
	 *            false 表示该 ID 是一个匿名 ID
	 * @param eventName
	 *            事件名称
	 *
	 * @throws DaqInvalidArgumentException
	 *             eventName 或 properties 不符合命名规范和类型规范时抛出该异常
	 */
	public void track(String userId, boolean anonymous, String eventName)
			throws DaqInvalidArgumentException {
		addEvent(userId, anonymous, null, "track", eventName, null);
	}

	/**
	 * 记录一个拥有一个或多个属性的事件。属性取值可接受类型为{@link Number}, {@link String}, {@link Date}和
	 * {@link List}； 若属性包含 $time 字段，则它会覆盖事件的默认时间属性，该字段只接受{@link Date}类型； 若属性包含
	 * $project 字段，则它会指定事件导入的项目；
	 *
	 * @param userId
	 *            用户 ID
	 * @param anonymous
	 *            false 表示该 ID 是一个匿名 ID
	 * @param eventName
	 *            事件名称
	 * @param properties
	 *            事件的属性
	 *
	 * @throws DaqInvalidArgumentException
	 *             eventName 或 properties 不符合命名规范和类型规范时抛出该异常
	 */
	public void track(String userId, boolean anonymous, String eventName,
			Map<String, Object> properties) throws DaqInvalidArgumentException {
		addEvent(userId, anonymous, null, "track", eventName, properties);
	}

	/**
	 * 设置用户的属性。属性取值可接受类型为{@link Number}, {@link String}, {@link Date}和
	 * {@link List}；
	 *
	 * 如果要设置的properties的key，之前在这个用户的profile中已经存在，则覆盖，否则，新创建
	 *
	 * @param userId
	 *            用户 ID
	 * @param anonymous
	 *            false 表示该 ID 是一个匿名 ID
	 * @param properties
	 *            用户的属性
	 *
	 * @throws DaqInvalidArgumentException
	 *             eventName 或 properties 不符合命名规范和类型规范时抛出该异常
	 */
	public void profileSet(String userId, boolean anonymous,
			Map<String, Object> properties) throws DaqInvalidArgumentException {
		addEvent(userId, anonymous, null, "profile_set", null, properties);
	}

	/**
	 * 设置用户的属性。这个接口只能设置单个key对应的内容，同样，如果已经存在，则覆盖，否则，新创建
	 *
	 * @param userId
	 *            用户 ID
	 * @param anonymous
	 *            false 表示该 ID 是一个匿名 ID
	 * @param property
	 *            属性名称
	 * @param value
	 *            属性的值
	 *
	 * @throws DaqInvalidArgumentException
	 *             eventName 或 properties 不符合命名规范和类型规范时抛出该异常
	 */
	public void profileSet(String userId, boolean anonymous, String property,
			Object value) throws DaqInvalidArgumentException {
		Map<String, Object> properties = new HashMap<String, Object>();
		properties.put(property, value);
		addEvent(userId, anonymous, null, "profile_set", null, properties);
	}

	/**
	 * 为用户的一个或多个数值类型的属性累加一个数值，若该属性不存在，则创建它并设置默认值为0。属性取值只接受 {@link Number}类型
	 *
	 * @param userId
	 *            用户 ID
	 * @param anonymous
	 *            false 表示该 ID 是一个匿名 ID
	 * @param properties
	 *            用户的属性
	 *
	 * @throws DaqInvalidArgumentException
	 *             eventName 或 properties 不符合命名规范和类型规范时抛出该异常
	 */
	public void profileIncrement(String userId, boolean anonymous,
			Map<String, Object> properties) throws DaqInvalidArgumentException {
		addEvent(userId, anonymous, null, "profile_increment", null, properties);
	}

	/**
	 * 为用户的数值类型的属性累加一个数值，若该属性不存在，则创建它并设置默认值为0
	 *
	 * @param userId
	 *            用户 ID
	 * @param anonymous
	 *            false 表示该 ID 是一个匿名 ID
	 * @param property
	 *            属性名称
	 * @param value
	 *            属性的值
	 *
	 * @throws DaqInvalidArgumentException
	 *             eventName 或 properties 不符合命名规范和类型规范时抛出该异常
	 */
	public void profileIncrement(String userId, boolean anonymous,
			String property, long value) throws DaqInvalidArgumentException {
		Map<String, Object> properties = new HashMap<String, Object>();
		properties.put(property, value);
		addEvent(userId, anonymous, null, "profile_increment", null, properties);
	}

	/**
	 * 为用户的一个或多个数组类型的属性追加字符串，属性取值类型必须为 {@link java.util.List}，且列表中元素的类型 必须为
	 * {@link java.lang.String}
	 *
	 * @param userId
	 *            用户 ID
	 * @param anonymous
	 *            false 表示该 ID 是一个匿名 ID
	 * @param properties
	 *            用户的属性
	 *
	 * @throws DaqInvalidArgumentException
	 *             eventName 或 properties 不符合命名规范和类型规范时抛出该异常
	 */
	public void profileAppend(String userId, boolean anonymous,
			Map<String, Object> properties) throws DaqInvalidArgumentException {
		addEvent(userId, anonymous, null, "profile_append", null, properties);
	}

	/**
	 * 为用户的数组类型的属性追加一个字符串
	 *
	 * @param userId
	 *            用户 ID
	 * @param anonymous
	 *            false 表示该 ID 是一个匿名 ID
	 * @param property
	 *            属性名称
	 * @param value
	 *            属性的值
	 *
	 * @throws DaqInvalidArgumentException
	 *             eventName 或 properties 不符合命名规范和类型规范时抛出该异常
	 */
	public void profileAppend(String userId, boolean anonymous,
			String property, String value) throws DaqInvalidArgumentException {
		List<String> values = new ArrayList<String>();
		values.add(value);
		Map<String, Object> properties = new HashMap<String, Object>();
		properties.put(property, values);
		addEvent(userId, anonymous, null, "profile_append", null, properties);
	}

	/**
	 * 删除用户某一个属性
	 *
	 * @param userId
	 *            用户 ID
	 * @param anonymous
	 *            false 表示该 ID 是一个匿名 ID
	 * @param property
	 *            属性名称
	 *
	 * @throws DaqInvalidArgumentException
	 *             eventName 或 properties 不符合命名规范和类型规范时抛出该异常
	 */
	public void profileUnset(String userId, boolean anonymous, String property)
			throws DaqInvalidArgumentException {
		Map<String, Object> properties = new HashMap<String, Object>();
		properties.put(property, true);
		addEvent(userId, anonymous, null, "profile_unset", null, properties);
	}

	/**
	 * 删除用户所有属性
	 *
	 * @param userId
	 *            用户 ID
	 * @param anonymous
	 *            false 表示该 ID 是一个匿名 ID
	 *
	 * @throws DaqInvalidArgumentException
	 *             distinctId 不符合命名规范时抛出该异常
	 */
	public void profileDelete(String userId, boolean anonymous)
			throws DaqInvalidArgumentException {
		addEvent(userId, anonymous, null, "profile_delete", null,
				new HashMap<String, Object>());
	}

	/**
	 * 设置 item
	 *
	 * @param itemType
	 *            item 类型
	 * @param itemId
	 *            item ID
	 * @param properties
	 *            item 相关属性
	 * @throws DaqInvalidArgumentException
	 *             取值不符合规范抛出该异常
	 */
	public void itemSet(String itemType, String itemId,
			Map<String, Object> properties) throws DaqInvalidArgumentException {
		addItem(itemType, itemId, "item_set", properties);
	}

	/**
	 * 删除 item
	 *
	 * @param itemType
	 *            item 类型
	 * @param itemId
	 *            item ID
	 * @throws DaqInvalidArgumentException
	 *             取值不符合规范抛出该异常
	 */
	public void itemDelete(String itemType, String itemId)
			throws DaqInvalidArgumentException {
		addItem(itemType, itemId, "item_delete", null);
	}

	/**
	 * 立即发送缓存中的所有日志
	 */
	public void flush() {
		this.consumer.flush();
	}

	/**
	 * 停止DataAPI所有线程，API停止前会清空所有本地数据
	 */
	public void shutdown() {
		this.consumer.close();
	}

	private static class HttpConsumer {

		HttpConsumer(String serverUrl, Map<String, String> httpHeaders) {
			this.serverUrl = serverUrl.trim();
			this.httpHeaders = httpHeaders;

			this.compressData = true;
		}

		HttpResponse consume(final String data) throws Exception {
			HttpResponse response = HttpClientBuilder.create().build()
					.execute(getHttpRequest(data));

			int httpStatusCode = response.getStatusLine().getStatusCode();
			if (httpStatusCode < 200 || httpStatusCode >= 300) {
				String httpContent = EntityUtils.toString(response.getEntity(),
						"UTF-8");
				throw new Exception(String.format(
						"Unexpected response %d from Wanpeng "
								+ "Analytics: %s", httpStatusCode, httpContent));
			}

			return response;
		}

		HttpUriRequest getHttpRequest(final String data) throws IOException {
			HttpPost httpPost = new HttpPost(this.serverUrl);

			httpPost.setEntity(getHttpEntry(data));
			httpPost.addHeader("User-Agent", "Wanpeng Java SDK " + SDK_VERSION);

			if (this.httpHeaders != null) {
				for (Map.Entry<String, String> entry : this.httpHeaders
						.entrySet()) {
					httpPost.addHeader(entry.getKey(), entry.getValue());
				}
			}

			return httpPost;
		}

		UrlEncodedFormEntity getHttpEntry(final String data) throws IOException {
			byte[] bytes = data.getBytes(Charset.forName("UTF-8"));

			List<NameValuePair> nameValuePairs = new ArrayList<NameValuePair>();

			if (compressData) {
				ByteArrayOutputStream os = new ByteArrayOutputStream(
						bytes.length);
				GZIPOutputStream gos = new GZIPOutputStream(os);
				gos.write(bytes);
				gos.close();
				byte[] compressed = os.toByteArray();
				os.close();

				nameValuePairs.add(new BasicNameValuePair("gzip", "1"));
				nameValuePairs.add(new BasicNameValuePair("data_list",
						new String(DaqBase64Coder.encode(compressed))));
			} else {
				nameValuePairs.add(new BasicNameValuePair("gzip", "0"));
				nameValuePairs.add(new BasicNameValuePair("data_list",
						new String(DaqBase64Coder.encode(bytes))));
			}
			return new UrlEncodedFormEntity(nameValuePairs);
		}

		final String serverUrl;
		final Map<String, String> httpHeaders;

		final Boolean compressData;
	}

	private void addEvent(String userId, boolean anonymous,
			String originDistinceId, String actionType, String eventName,
			Map<String, Object> properties) throws DaqInvalidArgumentException {
		assertKey("UserId", userId);
		assertProperties(actionType, properties);
		if (actionType.equals("track")) {
			assertKeyWithRegex("Event Name", eventName);
		}

		// Event time
		long time = System.currentTimeMillis();
		if (properties != null && properties.containsKey("$time")) {
			Date eventTime = (Date) properties.get("$time");
			properties.remove("$time");
			time = eventTime.getTime();
		}

		Map<String, Object> eventProperties = new HashMap<String, Object>();
		if (actionType.equals("track") || actionType.equals("track_signup")) {
			eventProperties.putAll(superProperties);
		}
		if (properties != null) {
			eventProperties.putAll(properties);
		}

		if (anonymous) {
			eventProperties.put("$anonymous", true);
		}

		Map<String, String> libProperties = getLibProperties();

		Map<String, Object> event = new HashMap<String, Object>();

		event.put("type", actionType);
		event.put("time", time);
		event.put("user_id", userId);
		event.put("properties", eventProperties);
		event.put("lib", libProperties);

		if (enableTimeFree) {
			event.put("time_free", true);
		}

		if (actionType.equals("track")) {
			event.put("event", eventName);
		}

		this.consumer.send(event);
	}

	private void addItem(String itemType, String itemId, String actionType,
			Map<String, Object> properties) throws DaqInvalidArgumentException {
		assertKeyWithRegex("Item Type", itemType);
		assertKey("Item Id", itemId);
		assertProperties(actionType, properties);

		String eventProject = null;
		if (properties != null && properties.containsKey("$project")) {
			eventProject = (String) properties.get("$project");
			properties.remove("$project");
		}

		Map<String, Object> eventProperties = new HashMap<String, Object>();
		if (properties != null) {
			eventProperties.putAll(properties);
		}

		Map<String, String> libProperties = getLibProperties();

		Map<String, Object> record = new HashMap<String, Object>();
		record.put("type", actionType);
		record.put("time", System.currentTimeMillis());
		record.put("properties", eventProperties);
		record.put("lib", libProperties);

		if (eventProject != null) {
			record.put("project", eventProject);
		}

		record.put("item_type", itemType);
		record.put("item_id", itemId);
		this.consumer.send(record);
	}

	private Map<String, String> getLibProperties() {
		Map<String, String> libProperties = new HashMap<String, String>();
		libProperties.put("$lib", "Java");
		libProperties.put("$lib_version", SDK_VERSION);
		libProperties.put("$lib_method", "code");

		if (this.superProperties.containsKey("$app_version")) {
			libProperties.put("$app_version",
					(String) this.superProperties.get("$app_version"));
		}

		StackTraceElement[] trace = (new Exception()).getStackTrace();

		if (trace.length > 3) {
			StackTraceElement traceElement = trace[3];
			libProperties.put("$lib_detail", String.format("%s##%s##%s##%s",
					traceElement.getClassName(), traceElement.getMethodName(),
					traceElement.getFileName(), traceElement.getLineNumber()));
		}

		return libProperties;
	}

	private void assertKey(String type, String key)
			throws DaqInvalidArgumentException {
		if (key == null || key.length() < 1) {
			throw new DaqInvalidArgumentException("The " + type + " is empty.");
		}
		if (key.length() > 255) {
			throw new DaqInvalidArgumentException("The " + type
					+ " is too long, max length is 255.");
		}
	}

	private void assertKeyWithRegex(String type, String key)
			throws DaqInvalidArgumentException {
		assertKey(type, key);
		if (!(KEY_PATTERN.matcher(key).matches())) {
			throw new DaqInvalidArgumentException("The " + type + "'" + key
					+ "' is invalid.");
		}
	}

	@SuppressWarnings("unchecked")
	private void assertProperties(String eventType,
			Map<String, Object> properties) throws DaqInvalidArgumentException {
		if (null == properties) {
			return;
		}
		for (Map.Entry<String, Object> property : properties.entrySet()) {
			if (property.getKey().equals("$anonymous")) {
				if (!(property.getValue() instanceof Boolean)) {
					throw new DaqInvalidArgumentException(
							"The property value of '$anonymous' should be "
									+ "Boolean.");
				}
				continue;
			}

			assertKeyWithRegex("property", property.getKey());

			if (!(property.getValue() instanceof Number)
					&& !(property.getValue() instanceof Date)
					&& !(property.getValue() instanceof String)
					&& !(property.getValue() instanceof Boolean)
					&& !(property.getValue() instanceof List<?>)) {
				throw new DaqInvalidArgumentException("The property '"
						+ property.getKey() + "' should be a basic type: "
						+ "Number, String, Date, Boolean, List<String>.");
			}

			if (property.getKey().equals("$time")
					&& !(property.getValue() instanceof Date)) {
				throw new DaqInvalidArgumentException(
						"The property '$time' should be a java.util.Date.");
			}

			// List 类型的属性值，List 元素必须为 String 类型
			if (property.getValue() instanceof List<?>) {
				for (final ListIterator<Object> it = ((List<Object>) property
						.getValue()).listIterator(); it.hasNext();) {
					Object element = it.next();
					if (!(element instanceof String)) {
						throw new DaqInvalidArgumentException("The property '"
								+ property.getKey()
								+ "' should be a list of String.");
					}
					if (((String) element).length() > 8192) {
						it.set(((String) element).substring(0, 8192));
					}
				}
			}

			// String 类型的属性值，长度不能超过 8192
			if (property.getValue() instanceof String) {
				String value = (String) property.getValue();
				if (value.length() > 8192) {
					property.setValue(value.substring(0, 8192));
				}
			}

			if (eventType.equals("profile_increment")) {
				if (!(property.getValue() instanceof Number)) {
					throw new DaqInvalidArgumentException(
							"The property value of PROFILE_INCREMENT should be a "
									+ "Number.");
				}
			} else if (eventType.equals("profile_append")) {
				if (!(property.getValue() instanceof List<?>)) {
					throw new DaqInvalidArgumentException(
							"The property value of PROFILE_INCREMENT should be a "
									+ "List<String>.");
				}
			}
		}
	}

	private static ObjectMapper getJsonObjectMapper() {
		ObjectMapper jsonObjectMapper = new ObjectMapper();
		// 容忍json中出现未知的列
		jsonObjectMapper.configure(
				DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false);
		// 兼容java中的驼峰的字段名命名
		jsonObjectMapper
				.setPropertyNamingStrategy(PropertyNamingStrategy.SNAKE_CASE);
		jsonObjectMapper.setDateFormat(new SimpleDateFormat(
				"yyyy-MM-dd HH:mm:ss.SSS"));
		return jsonObjectMapper;
	}

}
