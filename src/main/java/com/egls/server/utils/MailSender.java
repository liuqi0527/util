package com.egls.server.utils;

import java.util.Date;
import java.util.Map;
import java.util.Properties;

import javax.activation.DataHandler;
import javax.activation.DataSource;
import javax.mail.*;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeBodyPart;
import javax.mail.internet.MimeMessage;
import javax.mail.internet.MimeMultipart;
import javax.mail.util.ByteArrayDataSource;
import javax.net.SocketFactory;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;


/**
 * @author mayer - [Created on 2018-08-09 22:22]
 */
public final class MailSender {

    private static final Logger LOGGER = LoggerFactory.getLogger(MailSender.class);

    private final Properties properties = new Properties();

    private boolean debugEnabled = false;

    private Authenticator authenticator = null;

    private PasswordAuthentication passwordAuthentication = null;

    public MailSender() {
        enableAuth(true);
        enableSSL(true);
        setConnectTimeout(20000);
        setReadTimeout(50000);
    }

    public MailSender enableAuth(final boolean b) {
        properties.setProperty("mail.smtp.auth", String.valueOf(b));
        return this;
    }

    public MailSender enableSSL(final boolean b) {
        properties.setProperty("mail.smtp.ssl.enable", String.valueOf(b));
        return this;
    }

    public MailSender enableSTARTTLS(final boolean b) {
        properties.setProperty("mail.smtp.starttls.enable", String.valueOf(b));
        return this;
    }

    public MailSender setHost(final String s) {
        properties.setProperty("mail.smtp.host", s);
        return this;
    }

    public MailSender setConnectTimeout(final int milliseconds) {
        properties.setProperty("mail.smtp.connectiontimeout", String.valueOf(milliseconds));
        return this;
    }

    public MailSender setReadTimeout(final int milliseconds) {
        properties.setProperty("mail.smtp.timeout", String.valueOf(milliseconds));
        return this;
    }

    public MailSender setProtocols(final String s) {
        //QQ邮箱:TLSv1.2
        properties.setProperty("mail.smtp.ssl.protocols", s);
        return this;
    }

    public MailSender setCipherSuites(final String s) {
        //linux命令查询加密算法openssl s_client -showcerts -connect smtp.qq.com:465
        //QQ邮箱:SSL_RSA_WITH_RC4_128_SHA
        properties.setProperty("mail.smtp.ssl.ciphersuites", s);
        return this;
    }

    public MailSender setSocketFactory(final SocketFactory o) {
        properties.put("mail.smtp.ssl.socketFactory", o);
        return this;
    }

    public MailSender setSocketFactoryClass(final String s) {
        properties.setProperty("mail.smtp.socketFactory.class", s);
        return this;
    }

    public MailSender enableDebug(final boolean debugEnabled) {
        this.debugEnabled = debugEnabled;
        return this;
    }

    public MailSender setAccount(final String username, final String password) {
        this.passwordAuthentication = new PasswordAuthentication(username, password);
        this.authenticator = new Authenticator() {
            @Override
            protected PasswordAuthentication getPasswordAuthentication() {
                return passwordAuthentication;
            }
        };
        return this;
    }

    public boolean send(final String toAddress, final String subject, final String content) {
        return send(toAddress, subject, content, null);
    }

    public boolean send(final String toAddress, final String subject, final String content, final Map<String, byte[]> attachments) {
        Session session = Session.getInstance(properties, authenticator);
        session.setDebug(debugEnabled);

        try {
            MimeMultipart multipart = new MimeMultipart();
            MimeBodyPart messageBodyPart = new MimeBodyPart();
            messageBodyPart.setText(content);
            multipart.addBodyPart(messageBodyPart);

            if (attachments != null) {
                for (Map.Entry<String, byte[]> attachment : attachments.entrySet()) {
                    messageBodyPart = new MimeBodyPart();
                    DataSource source = new ByteArrayDataSource(attachment.getValue(), "application/octet-stream");
                    messageBodyPart.setDataHandler(new DataHandler(source));
                    messageBodyPart.setFileName(attachment.getKey());
                    multipart.addBodyPart(messageBodyPart);
                }
            }

            MimeMessage msg = new MimeMessage(session);
            msg.setSubject(subject);
            msg.setFrom(new InternetAddress(this.passwordAuthentication.getUserName()));
            msg.setRecipients(Message.RecipientType.TO, toAddress);
            msg.setSentDate(new Date());
            msg.setContent(multipart);

            Transport.send(msg);
            return true;
        } catch (Exception exception) {
            LOGGER.error("send mail error", exception);
            return false;
        }
    }

}
