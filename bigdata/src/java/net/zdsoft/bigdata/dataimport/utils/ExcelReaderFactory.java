package net.zdsoft.bigdata.dataimport.utils;

import java.io.IOException;
import java.io.InputStream;
import java.io.PushbackInputStream;

import org.apache.poi.openxml4j.exceptions.InvalidFormatException;

import com.alibaba.excel.ExcelReader;
import com.alibaba.excel.context.AnalysisContext;
import com.alibaba.excel.event.AnalysisEventListener;
import com.alibaba.excel.support.ExcelTypeEnum;

public class ExcelReaderFactory {

	/**
	 * @param in
	 *            文件输入流
	 * @param excelTypeEnum
	 *            文件格式
	 * @param customContent
	 *            自定义模型可以在
	 *            {@link AnalysisEventListener#invoke(Object, AnalysisContext) }
	 *            AnalysisContext中获取用于监听者回调使用
	 * @param eventListener
	 *            用户监听
	 * @param trim
	 *            是否对解析的String做trim()默认true,用于防止 excel中空格引起的装换报错。
	 * @throws IOException
	 * @throws EmptyFileException
	 * @throws InvalidFormatException
	 */
	@SuppressWarnings("deprecation")
	public static ExcelReader getExcelReader(InputStream in,
			ExcelTypeEnum excelTypeEnum, Object customContent,
			AnalysisEventListener<?> eventListener, boolean trim)
			throws IOException, InvalidFormatException {
		// 如果输入流不支持mark/reset，需要对其进行包裹
		if (!in.markSupported()) {
			in = new PushbackInputStream(in, 8);
		}

		if (excelTypeEnum != null) {
			return new ExcelReader(in, excelTypeEnum, customContent,
					eventListener, trim);
		}
		throw new InvalidFormatException(
				"Your InputStream was neither an OLE2 stream, nor an OOXML stream");
	}
}