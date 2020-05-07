package net.zdsoft.framework.utils;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import org.apache.commons.io.FileUtils;
import org.apache.commons.io.FilenameUtils;
import org.apache.commons.lang3.StringUtils;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.alibaba.fastjson.TypeReference;

public class SUtils extends SerializationUtils {

    public static <T> String s(T t) {
        return serialize(t);
    }

    public static <T> T dc(String data, Class<T> clazz) {
        return deserialize(data, clazz);
    }

    public static <T> T dt(String data, TypeReference<T> type) {
        return deserialize(data, type);
    }

    public static <T> List<T> dt(String data, Class<T> clazz) {
        return JSON.parseArray(data, clazz);
    }

    public static <T> String s(T t, Long max) {
        JSONObject json = new JSONObject();
        json.put("count", max);
        json.put("data", s(t));
        return json.toJSONString();
    }

    public static void changeAutoWire(File file) throws IOException {
        File[] fs = file.listFiles();
        for (File f : fs) {
            if (f.isDirectory()) {
                changeAutoWire(f);
            } else {
                if (StringUtils.equalsIgnoreCase(FilenameUtils.getExtension(f.getName()), "java")) {
                    List<String> lines = FileUtils.readLines(f, "utf8");
                    List<String> writeLines = new ArrayList<String>();
                    int mark = 0;
                    for (String line : lines) {
                        if (StringUtils.trim(line).equals("@Resource")) {
                            line = StringUtils.replace(line, "@Resource", "@Autowired");
                            mark = -1;
                        }
                        if (StringUtils.trim(line).equals("import javax.annotation.Resource;")) {
                            line = StringUtils.replace(line, "import javax.annotation.Resource;", "import org.springframework.beans.factory.annotation.Autowired;");
                            mark = -1;
                        }

                        writeLines.add(line);
                    }
                    if (mark == -1)
                        FileUtils.writeLines(f, writeLines);
                } else {
                    continue;
                }
            }
        }
    }

    public static void readFiles(File file) throws Exception {
        File[] fs = file.listFiles();
        for (File f : fs) {
            if (f.isDirectory()) {
                readFiles(f);
            } else {
                if (StringUtils.equalsIgnoreCase(FilenameUtils.getExtension(f.getName()), "java")) {
                    List<String> lines = FileUtils.readLines(f, "utf8");
                    List<String> writeLines = new ArrayList<String>();
                    List<String> writeLines2 = new ArrayList<String>();
                    int mark = 0;
                    String serviceLine = "";
                    for (String line : lines) {
                        if (StringUtils.trim(line).equals("@Service")) {
                            mark = 1;
                            continue;
                        }
                        if (mark > 0) {
                            if (StringUtils.contains(line, "implements")) {
                                if (!StringUtils.contains(line, "{")) {
                                    System.out.println(f);
                                    break;
                                }
                                String interfaceName = StringUtils.substringBetween(line, "implements ", "{");
                                interfaceName = StringUtils.trim(interfaceName);
                                serviceLine = "@Service(\""
                                        + StringUtils.lowerCase(StringUtils.substring(interfaceName, 0, 1))
                                        + StringUtils.substring(interfaceName, 1) + "\")";
                                writeLines.add(serviceLine);

                                writeLines.addAll(writeLines2);
                                writeLines.add(line);
                                mark = -1;
                                writeLines2.clear();
                            } else {
                                writeLines2.add(line);
                            }
                        } else {
                            writeLines.add(line);
                        }
                    }
                    if (mark == -1)
                        FileUtils.writeLines(f, writeLines);
                } else {
                    continue;
                }
            }
        }
    }

}
