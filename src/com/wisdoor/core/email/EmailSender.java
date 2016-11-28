package com.wisdoor.core.email;


import java.io.File;
import java.util.Date;
import java.util.Properties;

import javax.activation.DataHandler;
import javax.activation.FileDataSource;
import javax.mail.Authenticator;
import javax.mail.Message;
import javax.mail.Multipart;
import javax.mail.PasswordAuthentication;
import javax.mail.Session;
import javax.mail.Transport;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeBodyPart;
import javax.mail.internet.MimeMessage;
import javax.mail.internet.MimeMultipart;
import javax.mail.internet.MimeUtility;

public class EmailSender {
    private static final String charset = "UTF-8";
    private static final String defaultMimetype = "text/plain";

	
    public static void main(String[] args) throws Exception {
    	EmailSender.send(new String[]{"466015526@qq.com"}, "123456", "<b>测试</b>", null , "text/html");  
    }
    /**
     * 发�?邮件
     * @param receiver 收件�?
     * @param subject 标题 
     * @param mailContent 邮件内容
     * @param mimetype 内容类型 默认为text/plain,如果要发送HTML内容,应设置为text/html
     */
    public static void send(String receiver, String subject, String mailContent, String mimetype) {
    	send(new String[]{receiver}, subject, mailContent, mimetype);
    }
    /**
     * 发�?邮件
     * @param receivers 收件�?
     * @param subject 标题
     * @param mailContent 邮件内容
     * @param mimetype 内容类型 默认为text/plain,如果要发送HTML内容,应设置为text/html
     */
    public static void send(String[] receivers, String subject, String mailContent, String mimetype) {
    	send(receivers, subject, mailContent, null, mimetype);
    }
    /**
     * 发�?邮件
     * @param receivers 收件�?
     * @param subject 标题
     * @param mailContent 邮件内容
     * @param attachements 附件
     * @param mimetype 内容类型 默认为text/plain,如果要发送HTML内容,应设置为text/html
     */
    public static void send(String[] receivers, String subject, String mailContent, File[] attachements, String mimetype) {
        Properties props = new Properties();
        props.put("mail.smtp.host", ReadEmailProperties.readValue("email.host"));//smtp服务器地�?sohu 
        props.put("mail.smtp.socketFactory.class", "javax.net.ssl.SSLSocketFactory");
        props.put("mail.smtp.socketFactory.fallback", "false"); 
        props.put("mail.smtp.socketFactory.port", "465"); 
        //props.put("mail.smtp.port", ReadEmailProperties.readValue("email.port"));//�?��校验
        props.put("mail.smtp.auth", "true");//�?��校验
        Session session = Session.getDefaultInstance(props, new Authenticator() {
            protected PasswordAuthentication getPasswordAuthentication() {
                return new PasswordAuthentication(ReadEmailProperties.readValue("email.username"),ReadEmailProperties.readValue("email.password"));//登录用户�?密码
            }
        });
        session.setDebug(true);
        try {
            MimeMessage mimeMessage = new MimeMessage(session);
            mimeMessage.setFrom(new InternetAddress(ReadEmailProperties.readValue("email.username")));//发件人邮�?

            InternetAddress[] toAddress = new InternetAddress[receivers.length];
            for (int i=0; i<receivers.length; i++) {
                toAddress[i] = new InternetAddress(receivers[i]);
            }
            mimeMessage.setRecipients(Message.RecipientType.TO, toAddress);//收件人邮�?
            mimeMessage.setSubject(subject, charset);
            
            Multipart multipart = new MimeMultipart();
            //正文
            MimeBodyPart body = new MimeBodyPart(); 
            body.setContent(mailContent, (mimetype!=null && !"".equals(mimetype) ? mimetype : defaultMimetype)+ ";charset="+ charset);
            multipart.addBodyPart(body);//发件内容
            //附件
            if(attachements!=null){
	            for (File attachement : attachements) {
	                MimeBodyPart attache = new MimeBodyPart(); 
	                attache.setDataHandler(new DataHandler(new FileDataSource(attachement)));
	                String fileName = getLastName(attachement.getName());
	                attache.setFileName(MimeUtility.encodeText(fileName, charset, null));
	                multipart.addBodyPart(attache);
	            }
            }
            mimeMessage.setContent(multipart);           
            mimeMessage.setSentDate(new Date());
            Transport.send(mimeMessage);            
        } catch (Exception e) {
        	e.printStackTrace();
        }
    }

    private static String getLastName(String fileName) {
        int pos = fileName.lastIndexOf("\\");
        if (pos > -1) {
            fileName = fileName.substring(pos + 1);
        }
        pos = fileName.lastIndexOf("/");
        if (pos > -1) {
            fileName = fileName.substring(pos + 1);
        }
        return fileName;
    }
    
	
}
