package net.zdsoft.framework.utils;

import java.io.BufferedReader;
import java.io.File;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.Date;
import java.util.Map;


public class HtmlToPdf {
    //wkhtmltopdf在系统中的路径
    private static final String toPdfToolWindows = "D:\\1Tools\\wkhtmltox\\wkhtmltopdf\\bin\\wkhtmltopdf.exe";
    private static final String toPdfToolLinux = "/opt/server_data/v7/wkhtmltox/bin/wkhtmltopdf";
    
    public static boolean convert(String[] srcPaths, String destPath, String height, String width, int waitSecond){
    	return convertFin(srcPaths, destPath, height, width, waitSecond, null);
    }
    /**
     * html转pdf
     * @param srcPath html路径，可以是硬盘上的路径，也可以是网络路径
     * @param destPath pdf保存路径
     * @param height 高度     (高度和宽度要一起设置才能生效，如果只设置其中一个或不设置默认用A4大小)
     * @param width	   宽度
     * @param waitSecond 等待页面加载时间，等待多久后转换
     * @return 转换成功返回true
     */
    @SuppressWarnings("deprecation")
	public static boolean convertFin(String[] srcPaths, String destPath, String height, String width, int waitSecond,
			Map<String,String> parMap){
    	boolean Landscape=false;
    	String  someName=null;
        File file = new File(destPath);
        File parent = file.getParentFile();
        //如果pdf保存路径不存在，则创建路径
        if(!parent.exists()){
            parent.mkdirs();
        }
        if(parMap!=null){
        	Landscape="true".equals(parMap.get("Landscape"));
        	someName=parMap.get("someName");
        }
        StringBuilder cmd = new StringBuilder();
        if(waitSecond==0) {
        	waitSecond = 2000;
        }
        boolean result = true;
        InputStreamReader isr = null;
        BufferedReader br = null;
        try{
        	if(OSUtil.isWindows()) {
        		cmd.append(toPdfToolWindows);
        	}else {
        		cmd.append(toPdfToolLinux);
        	}
    		for (String srcPath : srcPaths) {
    	        cmd.append(" ");
    	        if(StringUtils.isNotBlank(someName)){
    	        	cmd.append("  --header-center "+someName+"");//页眉中间内容
    	        }
    	        cmd.append(" --header-spacing 2 ");//    (设置页眉和内容的距离,默认0)
    	        cmd.append(" --javascript-delay "+waitSecond+" ");
    	        cmd.append(" --debug-javascript ");
    	        if(Landscape){
    	        	cmd.append(" --orientation  Landscape ");
    	        }
    	        if (StringUtils.isNotEmpty(height)) {
    	        	cmd.append(" --page-height " + height + " ");
    	        }
    	        if(StringUtils.isNotEmpty(width)) {
    	        	cmd.append(" --page-width "+width+" ");
    	        }
//    	        cmd.append(" --run-script ");
    	       
    	        cmd.append(srcPath);
			}
    		cmd.append(" ");
	        cmd.append(destPath);
        	
//        		for (String srcPath : srcPaths) {
//        	        cmd.append(" ");
//        	        cmd.append("  --header-line");//页眉下面的线
//        	        cmd.append("  --header-center 这里是页眉这里是页眉这里是页眉这里是页眉 ");//页眉中间内容
//        	        cmd.append(" --header-spacing 10 ");//    (设置页眉和内容的距离,默认0)
//        	        cmd.append(" --stop-slow-scripts ");
//        	        
//        	        cmd.append(srcPath);
//				}
//        		cmd.append(" ");
//    	        cmd.append(destPath);
//        	}
//	        System.out.println(cmd.toString());
            Process proc = Runtime.getRuntime().exec(cmd.toString());
            
//            error = new HtmlToPdfInterceptor(proc.getErrorStream());
            isr = new InputStreamReader(proc.getErrorStream(), "utf-8");
            br = new BufferedReader(isr);
            String line = null;
            while ((line = br.readLine()) != null) {
                //System.out.println(line.toString()); //输出内容
            	if(line.indexOf("error")>=0) {
            		result = false;
            	}else if(line.indexOf("Warning")>=0) {
            		//result = false;
            	}
            }
//            HtmlToPdfInterceptor output = new HtmlToPdfInterceptor(proc.getInputStream());
//            error.start();
//            System.out.println(111);
//            output.start();
//            error.destroy();
        }catch(Exception e){
            result = false;
            e.printStackTrace();
        }finally {
        	
    		try {
    			if(isr!=null) {
    				isr.close();
    			}
    			if(br!=null) {
    				br.close();
    			}
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
        	
//        	if(error!=null) {
//        		error.wait();
//        		error.destroy();
//        	}
		}
        
        return result;
    }
    
    
    
   /* public static void main(String[] args) {
        System.out.println(HtmlToPdf.convert(new String[]{"http://www.baidu.com"}, "d:/wkhtmltopdf"+new Date().getTime()+".pdf","",6000,false));
        
//        HtmlToPdf.convert(new String[]{"http://www.baidu.com"}, "d:/wkhtmltopdf"+new Date().getTime()+".pdf","",4000);
        
    }*/
}