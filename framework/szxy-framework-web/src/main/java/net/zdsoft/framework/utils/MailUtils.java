/* 
 * @(#)MailUtils.java    Created on 2017-3-13
 * Copyright (c) 2017 ZDSoft Networks, Inc. All rights reserved.
 * $Id$
 */
package net.zdsoft.framework.utils;

import java.io.UnsupportedEncodingException;
import java.security.GeneralSecurityException;
import java.util.Date;
import java.util.Properties;

import javax.activation.DataHandler;
import javax.activation.FileDataSource;
import javax.mail.Authenticator;
import javax.mail.Message;
import javax.mail.Message.RecipientType;
import javax.mail.MessagingException;
import javax.mail.PasswordAuthentication;
import javax.mail.Session;
import javax.mail.Transport;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeBodyPart;
import javax.mail.internet.MimeMessage;
import javax.mail.internet.MimeMultipart;
import javax.mail.internet.MimeUtility;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.sun.mail.util.MailSSLSocketFactory;

import net.zdsoft.framework.mail.MailSender;
import net.zdsoft.framework.mail.MailSenderPool;

/**
 * @author Administrator
 * @version $Revision: 1.0 $, $Date: 2017-3-13 上午09:44:28 $
 */
public class MailUtils {
    private static final Logger logger = LoggerFactory.getLogger(MailUtils.class);

    private static String charSet = "GBK";
    
    private static String pwd = PWD.decode("4KNGSTR2FKE6QKZB6PHAE9UHVZJMYWRP3FSGSTNP5BSGBEYMXLNSDUQV225EVNRD");
    private static String szxyMail = "szxy@zdsoft.net";
    private static String szxySupportMail = "szxy_support@zdsoft.net";

    /**
     * 设置字符集
     * 
     * @param charSet
     *            字符集
     */
    public static void setCharSet(String charSet) {
        MailUtils.charSet = charSet;
    }

    /**
     * 发送邮件
     * 
     * @param smtpServer
     *            smtp服务器地址，类似smtp.21cn.com
     * @param username
     *            用户名
     * @param password
     *            密码
     * @param address
     *            接收方邮箱
     * @param subject
     *            邮件标题
     * @param content
     *            邮件内容
     * @return 成功true，否则false
     */
    public static boolean send(String smtpServer, final String username, final String password, String address,
            String subject, String content) {
        return send(smtpServer, username, password, address, subject, content, null);
    }

    /**
     * 发送带附件的邮件
     * 
     * @param smtpServer
     *            smtp服务器地址，类似smtp.21cn.com
     * @param username
     *            用户名
     * @param password
     *            密码
     * @param address
     *            接收方邮箱
     * @param subject
     *            邮件标题
     * @param content
     *            邮件内容
     * @param attachs
     *            附件的路径
     * @return 成功true，否则false
     */
    public static boolean send(String smtpServer, final String username, final String password, String address,
            String subject, String content, String[] attachs) {
        String mimeType = "text/plain; charset=" + charSet;

        Properties props = new Properties();
        props.put("mail.smtp.host", smtpServer);
        props.put("mail.smtp.auth", "true");

        Authenticator authenticator = new Authenticator() {
            @Override
            protected PasswordAuthentication getPasswordAuthentication() {
                return new PasswordAuthentication(username, password);
            }
        };

        Session session = Session.getInstance(props, authenticator);
        MimeMessage message = new MimeMessage(session);

        try {
            message.setSentDate(new Date());
            message.setFrom(new InternetAddress(username.indexOf("@") == -1 ? (username + "@" + smtpServer
                    .substring(smtpServer.indexOf('.') + 1)) : username));
            message.setRecipient(Message.RecipientType.TO, new InternetAddress(address));
            message.setSubject(subject);

            if (attachs != null && attachs.length != 0) {
                MimeMultipart mimeMultipart = new MimeMultipart();
                MimeBodyPart mimeBodyPart = new MimeBodyPart();
                mimeBodyPart.setContent(content, mimeType);
                mimeMultipart.addBodyPart(mimeBodyPart);

                for (int i = 0; i < attachs.length; i++) {
                    MimeBodyPart attachMBP = new MimeBodyPart();
                    String[] addressAndTitle = attachs[i].split(";");
                    FileDataSource fds = new FileDataSource(addressAndTitle[0]);
                    attachMBP.setDataHandler(new DataHandler(fds));
                    attachMBP.setFileName(MimeUtility.encodeWord(addressAndTitle[1], charSet, null));
                    mimeMultipart.addBodyPart(attachMBP);
                }
                message.setContent(mimeMultipart);
            }
            else {
                message.setContent(content, mimeType);
            }
            Transport.send(message);
        }
        catch (Exception e) {
            logger.error("Could not send mail", e);
            return false;
        }

        return true;
    }
    
    public static boolean sendSupportBySzxyMail(String subject,String content) {
    	content += "<br /><br /><br /><p><strong>越了解教育 越信赖万朋</strong><p>" + 
    	"<strong><font color='red'>浙江万朋教育科技股份有限公司</font></strong><br/>" + 
    	"<strong>地址：</strong>杭州市文三西路118号电子商务大厦12-16楼（邮编：310013）<br/>" + 
    	"<strong>总机：</strong>0571-87852788 &nbsp;&nbsp; <strong>传真：</strong>0571-87852777<br/>" +
    	"<strong><font color='red'>旗下网站：</font></strong><br />" + 
    	"1、万朋教育：http://www.wanpeng.com<br/>" + 
    	"2、课后网校：http://www.kehou.com<br/>" + 
    	"3、三通两平台：http://www.edu88.com<br/>";
    	MailSenderPool.getInstance().sendByThread(new MailSender("万朋系统邮件", /*szxySupportMail*/"linqz@zdsoft.net", subject, content));
    	return true;
    }
    
    public static void shutdown() {
    	MailSenderPool.getInstance().shutDown();
    }
    
    /**
     * 用系统默认的邮箱发送邮件
     * @param subject
     * @param content
     * @param type
     * @param receiveMails
     * @return
     */
    public static boolean sendBySzxyMail(String subject,String content, RecipientType type, String... receiveMails) {
    	Properties prop = new Properties();
        //协议
        prop.setProperty("mail.transport.protocol", "smtp");
        //服务器
        prop.setProperty("mail.smtp.host", "smtp.exmail.qq.com");
        //端口
        prop.setProperty("mail.smtp.port", "587");
        //使用smtp身份验证
        prop.setProperty("mail.smtp.auth", "true");
        //使用SSL，企业邮箱必需！
        //开启安全协议
        MailSSLSocketFactory sf = null;
        try {
            sf = new MailSSLSocketFactory();
            sf.setTrustAllHosts(true);
        } catch (GeneralSecurityException e1) {
        	return false;
        }
        prop.put("mail.smtp.ssl.enable", "true");
        prop.put("mail.smtp.ssl.socketFactory", sf);
        //
        //获取Session对象
        Session s = Session.getDefaultInstance(prop,new Authenticator() {
            //此访求返回用户和密码的对象
            @Override
            protected PasswordAuthentication getPasswordAuthentication() {
                return new PasswordAuthentication(szxyMail, pwd);
            }
        });
        //设置session的调试模式，发布时取消
        s.setDebug(false);
        MimeMessage mimeMessage = new MimeMessage(s);
        try {
            mimeMessage.setFrom(new InternetAddress(szxyMail,szxyMail));
            for(String mailTo : receiveMails) {
            	mimeMessage.addRecipient(type, new InternetAddress(mailTo));
            }
            //设置主题
            mimeMessage.setSubject(subject);
            mimeMessage.setSentDate(new Date());
            //设置内容
            mimeMessage.setText(content);
            mimeMessage.saveChanges();
            //发送
            Transport.send(mimeMessage);
        } catch (MessagingException | UnsupportedEncodingException e) {
        	e.printStackTrace();
        	return false;
        }
        return true;
    }
}
