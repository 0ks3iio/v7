package net.zdsoft.bigdata.frame.data.demo;

import net.zdsoft.framework.entity.Json;
import net.zdsoft.framework.utils.RedisUtils;

import org.junit.Test;

public class RedisDemo {

	@Test
	public void test() throws Exception {
		for (int i = 1; i < 100; i++) {
			Json json=new Json();
			json.put("key", "value");
			json.put("value", i * 10);
			RedisUtils.set("111111",json.toJSONString());
			System.out.println(json.toJSONString());
			Thread.sleep(3000);
		}
	}
}

