package com.bgj.util;

import java.util.Date;
import java.util.Properties;

import javax.mail.Address;
import javax.mail.Authenticator;
import javax.mail.Message;
import javax.mail.PasswordAuthentication;
import javax.mail.Session;
import javax.mail.Transport;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeMessage;

import com.bgj.autojobs.NotificationMail;

public class MailUtil {
	private static String host = "smtp.163.com";
	private static String username = "lqj_liu@163.com";
	private static String password = "GGaaooyyoouu456";

	private static String mail_head_name = "KLineService Daily Report";

	private static String mail_head_value = "KLineService Daily Report";

	private static String mail_from = "lqj_liu@163.com";

	private static String personalName = "KLineServiceNotification(NotReply)";


	/**
	 * 此段代码用来发送普通电子邮件
	 */
	public static void send(NotificationMail notification) throws Exception {
		try {
			Properties props = new Properties();
			Authenticator auth = new Authenticator(){
				public PasswordAuthentication getPasswordAuthentication() {
					return new PasswordAuthentication(username, password);
				}
			}; // 进行邮件服务器用户认证
			props.put("mail.smtp.host", host);
			props.put("mail.smtp.auth", "true");
			Session session = Session.getDefaultInstance(props, auth);
			// 设置session,和邮件服务器进行通讯。
			MimeMessage message = new MimeMessage(session);
			// message.setContent("foobar, "application/x-foobar"); // 设置邮件格式
			message.setSubject(notification.getSubject()); // 设置邮件主题
			message.setText(notification.getBody()); // 设置邮件正文
			message.setHeader(mail_head_name, mail_head_value); // 设置邮件标题

			message.setSentDate(new Date()); // 设置邮件发送日期
			Address address = new InternetAddress(mail_from, personalName);
			message.setFrom(address); // 设置邮件发送者的地址

			message.addRecipient(Message.RecipientType.TO, new InternetAddress("lqj.liu@qq.com"));

			Transport.send(message); // 发送邮件
			System.out.println("send success!");
		} catch (Exception ex) {
			ex.printStackTrace();
			throw new Exception(ex.getMessage());
		}
	}


	public static void main(String[] args) {
		try {
			NotificationMail notification = new NotificationMail();
			notification.setSuccessful(true);
			MailUtil.send(notification);
			
			NotificationMail notificationFailed = new NotificationMail();
			notificationFailed.setSuccessful(false);
			notificationFailed.setFailedCause(new NullPointerException());
			MailUtil.send(notificationFailed);

		} catch (Exception ex) {
			ex.printStackTrace();
		}
	}

}