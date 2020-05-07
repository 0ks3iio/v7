package net.zdsoft.license;

import java.io.FileWriter;
import java.io.PrintWriter;
import java.util.Date;

public class CmdlineLicenseTool {

    public static void main(String[] args) throws Exception {

        String errorPath = "error.txt";
        PrintWriter pw = new PrintWriter(new FileWriter(errorPath));

        if (args.length < 2 ) {
            pw.println("-1");
            pw.println("缺少参数");
            pw.close();
            return;
        }

        String unitName = args[0];
        String path = args[1];
        LicenseInfo info = null;
        try {
            info = LicenseDecryptor.decodeFromFile(path);
        } catch (Exception e) {
            pw.println("-1");
            pw.println(e.getMessage());
            pw.close();
            return;
        }

        if (!unitName.equals(info.getUnitName())) {
            pw.println("-1");
			pw.println("注册单位和license文本信息中的单位名称不匹配");
            pw.close();
            return;
		}
		if (info.getExpireDate().compareTo(new Date()) < 0) {
            pw.println("-1");
			pw.println("license已经过期.");
            pw.close();
            return;
		}

        pw.println("1");
        pw.close();

    }

}
