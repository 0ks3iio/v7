package net.zdsoft.bigdata.data.action.demo;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

import net.zdsoft.echarts.common.StringUtils;
import net.zdsoft.echarts.convert.api.JData;
import net.zdsoft.framework.annotation.ControllerInfo;
import net.zdsoft.framework.entity.Json;
import net.zdsoft.framework.entity.JsonArray;

import org.apache.commons.lang3.math.NumberUtils;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

@Controller
@RequestMapping("/bigdata/common/demo")
public class DemoController {

    @ResponseBody
    @ControllerInfo("动态数字demo")
    @RequestMapping("/dynamicDigital")
    public String dynamicDigitalDemo() {
        Json json = new Json();
        json.put("value", System.currentTimeMillis() % 100000);
        return json.toJSONString();
    }

    @ResponseBody
    @ControllerInfo("字符云demo")
    @RequestMapping("/wordCloud")
    public String wordCloud() {
        JsonArray list = new JsonArray();
        Json json1 = new Json();
        json1.put("x", "高中");
        json1.put("y", 500);
        list.add(json1);

        Json json2 = new Json();
        json2.put("x", "初中");
        json2.put("y", 1000);
        list.add(json2);

        Json json3 = new Json();
        json3.put("x", "小学");
        json3.put("y", 1500);
        list.add(json3);
        return list.toJSONString();
    }

    @ResponseBody
    @ControllerInfo("地图demo")
    @RequestMapping("/mapLine")
    public String mapLineDemo() {
        JsonArray list = new JsonArray();
        Json json1 = new Json();
        json1.put("x", "阿勒泰地区");
        json1.put("y", 100);
        json1.put("toX", "哈密地区");
        list.add(json1);

        Json json2 = new Json();
        json2.put("x", "和田地区");
        json2.put("y", 200);
        json2.put("toX", "哈密地区");
        list.add(json2);

        Json json3 = new Json();
        json3.put("x", "吐鲁番地区");
        json3.put("y", 300);
        json3.put("toX", "哈密地区");
        list.add(json3);
        return list.toJSONString();
    }

    private List<JData.Entry> entryList;

    @ResponseBody
    @RequestMapping("/dynamic/bar")
    public String dynamicBar() {
        Random random = new Random();
        if (entryList == null) {
            entryList = new ArrayList<>(20);
            for (int i=1; i< 20; i++) {
                JData.Entry entry = new JData.Entry();
                entry.setName("s1");
                entry.setX("第" + (i+1) + "天");
                entry.setY(random.nextInt(200));
                entryList.add(entry);
            }
        } else {
            entryList.remove(0);
            int index = 0;
            for (JData.Entry entry : entryList) {
                index = NumberUtils.toInt(entry.getX().replace("第", "").replace("天", ""));
                entry.setX("第" + (index) + "天");
            }
            JData.Entry entry = new JData.Entry();
            entry.setName("s1");
            entry.setX("第"+(index+1)+"天");
            entry.setY(random.nextInt(200));
            entryList.add(entry);
        }

        System.out.println(StringUtils.toJSONString(entryList));
        return StringUtils.toJSONString(entryList);
    }
}
