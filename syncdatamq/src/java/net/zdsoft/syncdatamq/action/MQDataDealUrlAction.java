package net.zdsoft.syncdatamq.action;

import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.request;

import java.io.UnsupportedEncodingException;
import java.util.ArrayList;
import java.util.List;
import java.util.ResourceBundle;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.TimeUnit;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import com.alibaba.druid.util.StringUtils;
import com.alibaba.fastjson.JSONObject;

import net.zdsoft.basedata.entity.Unit;
import net.zdsoft.basedata.entity.User;
import net.zdsoft.basedata.remote.service.UnitRemoteService;
import net.zdsoft.basedata.remote.service.UserRemoteService;
import net.zdsoft.framework.action.BaseAction;
import net.zdsoft.framework.annotation.ControllerInfo;
import net.zdsoft.framework.enums.BooleanEnum;
import net.zdsoft.framework.utils.SecurityUtils;
import net.zdsoft.syncdatamq.utils.ActiveMQUtils;

@RequestMapping("/syncdatamq")
@Controller
public class MQDataDealUrlAction extends BaseAction {
	private static final String CONF_FILE_PATH = "conf/symConf";
	private static ResourceBundle evn = ResourceBundle.getBundle(CONF_FILE_PATH);

	@Autowired
	private UnitRemoteService unitRemoteService;
	@Autowired
	private UserRemoteService userRemoteService;

	@RequestMapping("/openapi/receive/test")
	@ResponseBody
	@ControllerInfo(ignoreLog=ControllerInfo.LOG_FORCE_IGNORE)
	public String receiveData(String activeMqParam, String sck) {
		try {
			String data = new String(SecurityUtils.decryptAESAndBase64(activeMqParam.getBytes("utf8"), ActiveMQUtils.ACTIVE_MQ_AES_ENCRYPT_KEY + "." + sck), "utf8");
		} catch (UnsupportedEncodingException e) {
			e.printStackTrace();
		} catch (Exception e) {
			e.printStackTrace();
		}
		return "";
	}
	@RequestMapping("/openapi/receive/{type}")
	@ResponseBody
	public String receiveData(String activeMqParam, String sck, @PathVariable String type) {
		if (org.apache.commons.lang3.StringUtils.isBlank(activeMqParam)
				|| org.apache.commons.lang3.StringUtils.isBlank(sck)) {
			return BooleanEnum.TRUE.stringValue();
		}
		try {
			String useThread = evn.getString("receiver.activemq.use.thread");
			if (StringUtils.equals(String.valueOf(BooleanEnum.TRUE.intValue()), useThread)) {
				new Thread(() -> {
					dealReceivedData(activeMqParam, sck, type);
				}).start();
			} else {
				dealReceivedData(activeMqParam, sck, type);
			}
			return BooleanEnum.TRUE.stringValue();
		} catch (Exception e) {
			// 为了避免一直在同步，如果异常，也返回true，先抛掉这批数据
			return BooleanEnum.TRUE.stringValue();
		}
	}

	private void dealReceivedData(String activeMqParam, String sck, String type) {
		String sck2 = ActiveMQUtils.ACTIVE_MQ_AES_ENCRYPT_KEY + "." + sck;
		try {
			String data = new String(SecurityUtils.decryptAESAndBase64(activeMqParam.getBytes("utf8"), sck2), "utf8");
			JSONObject json = JSONObject.parseObject(data);
			String array = json.getJSONArray("data").toJSONString();
			if (StringUtils.equals(type, "unit")) {
				deal4Units(array);
			} else if (StringUtils.equals(type, "user")) {
				deal4Users(array);
			}
		} catch (Exception e) {
			throw new RuntimeException(e);
		}
	}

	private void deal4Users(String data) {
		String mark = ResourceBundle.getBundle(CONF_FILE_PATH).getString("receiver.activemq.unit.mark");
		List<User> users = User.dt(data);
		CountDownLatch latch = new CountDownLatch(users.size());
		int threadCount = 20;
		int batchSize = users.size() / threadCount;
		for (int i = 0; i < threadCount; i++) {
			int end = (i + 1) * batchSize;
			end = end > users.size() ? users.size() : end;
			List<User> usersDeal = users.subList(i * batchSize, end);
			new Thread(() -> {
				for (User user : usersDeal) {
					dealOneUser(mark, latch, user);
				}
			}).start();
		}

		if (users.size() % threadCount != 0) {
			List<User> usersDeal = users.subList(batchSize * threadCount, users.size());
			for (User user : usersDeal) {
				dealOneUser(mark, latch, user);
			}
		}

		try {
			latch.await(1, TimeUnit.HOURS);
		} catch (InterruptedException e) {
		}
	}

	private void dealOneUser(String mark, CountDownLatch latch, User user) {
		String newName = (mark + "_").toLowerCase() + user.getUsername();
		user.setUsername(newName.length() > 20 ? user.getUsername() : newName);
		if (Integer.valueOf(User.USER_TYPE_TOP_ADMIN).equals(user.getUserType())) {
			user.setUserType(User.USER_TYPE_UNIT_ADMIN);
		}
		userRemoteService.saveUser(user);
		latch.countDown();
	}

	private void deal4Units(String data) {
		String mark = ResourceBundle.getBundle(CONF_FILE_PATH).getString("receiver.activemq.unit.mark");
		List<Unit> units = Unit.dt(data);
		List<Unit> subUnits = new ArrayList<>();
		for (Unit unit : units) {
			unit.setUnionCode(mark + unit.getUnionCode());
			if (StringUtils.equalsIgnoreCase(unit.getParentId(), Unit.TOP_UNIT_GUID)) {
				unit.setParentId(Unit.TOP_UNIT_PARENT_GUID_NOT_TRUE);
				unit.setUnitType(Unit.UNIT_EDU_SUB);
				unitRemoteService.save(unit);
			} else {
				subUnits.add(unit);
			}
		}
		unitRemoteService.saveAll(subUnits.toArray(new Unit[0]));
	}

}
