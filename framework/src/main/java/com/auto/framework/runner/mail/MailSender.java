package com.auto.framework.runner.mail;

import org.apache.commons.lang.StringUtils;

import javax.activation.DataHandler;
import javax.mail.*;
import javax.mail.Message.RecipientType;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeBodyPart;
import javax.mail.internet.MimeMessage;
import javax.mail.internet.MimeMultipart;
import javax.mail.util.ByteArrayDataSource;
import java.util.Collection;
import java.util.List;
import java.util.Properties;

/**
 * User: Priytam Jee Pandey
 * Date: 28/05/20
 * Time: 1:17 pm
 * email: mrpjpandey@gmail.com
 */
public class MailSender {

    private Session session = null;
    private Properties props = new Properties();
    private String user = "";
    private String pwd = "";

    public MailSender(String host, String port, String user, String password) {
        this.user = StringUtils.isEmpty(user) ? "" : user;
        this.pwd = StringUtils.isEmpty(user) ? "" : password;
        props.put("mail.smtp.host", host);
        props.put("mail.smtp.port", port);
        if (!StringUtils.isBlank(user)) {
            props.put("mail.smtp.auth", "true");
        }
        props.put("mail.smtp.socketFactory.port", port);
        props.put("mail.smtp.socketFactory.fallback", "true");
        session = Session.getDefaultInstance(props);
    }

    public void sendEmail(String to, String from, String subjectMessage, String bodyMessage, String fromName, String contentType) throws MessagingException {
        MimeMessage mimeMessage = new MimeMessage(session);

        if (contentType != null) {
            mimeMessage.setContent(bodyMessage, contentType);
        } else {
            mimeMessage.setText(bodyMessage);
        }

        mimeMessage.setSubject(subjectMessage);
        if (fromName != null) {
            from = fromName + "<" + from + ">";
        }
        mimeMessage.setFrom(new InternetAddress(from));
        if (to.indexOf(',') >= 0) {
            for (String nextTo : to.split(",")) {
                mimeMessage.addRecipient(RecipientType.TO, new InternetAddress(nextTo));
            }
        } else {
            mimeMessage.addRecipient(RecipientType.TO, new InternetAddress(to));
        }
        mimeMessage.saveChanges();
        send(mimeMessage);
    }

    public void sendEmail(String email, String from, String subjectMessage, String bodyMessage) throws MessagingException {
        sendEmail(email, from, subjectMessage, bodyMessage, null, null);
    }

    private void send(Message message) throws MessagingException {
        Transport transport = session.getTransport("smtp");
        if (!StringUtils.isBlank(user)) {
            transport.connect(user, pwd);
        } else {
            transport.connect();
        }
        transport.sendMessage(message, message.getAllRecipients());
        transport.close();
    }

    public void sendEmail(Collection<String> toList, Collection<String> ccList, Collection<String> bccList, String from, String subjectMessage, String bodyMessage) throws MessagingException {
        sendEmail(toList, ccList, bccList, from, subjectMessage, bodyMessage, null);
    }

    public void sendEmail(Collection<String> toList, Collection<String> ccList, Collection<String> bccList, String from, String subjectMessage, String bodyMessage, byte[] file, String fileName,
                          String contentType) throws MessagingException {
        Message mimeMessage = new MimeMessage(session);
        mimeMessage.setSubject(subjectMessage);
        mimeMessage.setFrom(new InternetAddress(from));
        if (toList != null) {
            for (String to : toList) {
                mimeMessage.addRecipient(RecipientType.TO, new InternetAddress(to));
            }
        }
        if (ccList != null) {
            for (String cc : ccList) {
                mimeMessage.addRecipient(RecipientType.CC, new InternetAddress(cc));
            }
        }
        if (bccList != null) {
            for (String bcc : bccList) {
                mimeMessage.addRecipient(RecipientType.BCC, new InternetAddress(bcc));
            }
        }

        Multipart multipart = new MimeMultipart();

        MimeBodyPart bodyMessagePart = new MimeBodyPart();
        bodyMessagePart.setText(bodyMessage);
        bodyMessagePart.setContent(bodyMessage, "text/html");

        MimeBodyPart attachFilePart = new MimeBodyPart();
        attachFilePart.setDataHandler(new DataHandler(new ByteArrayDataSource(file, contentType)));
        attachFilePart.setFileName(fileName);
        attachFilePart.setDisposition(Part.ATTACHMENT);

        multipart.addBodyPart(attachFilePart);
        multipart.addBodyPart(bodyMessagePart);

        mimeMessage.setContent(multipart);
        mimeMessage.saveChanges();

        send(mimeMessage);
    }

    public void sendEmail(Collection<String> toList, Collection<String> ccList, Collection<String> bccList, String from, String subjectMessage, String bodyMessage, byte[][] files,
                          List<String> fileNames, List<String> contentTypes) throws MessagingException {
        Message mimeMessage = new MimeMessage(session);
        mimeMessage.setSubject(subjectMessage);
        mimeMessage.setFrom(new InternetAddress(from));
        if (toList != null) {
            for (String to : toList) {
                mimeMessage.addRecipient(RecipientType.TO, new InternetAddress(to));
            }
        }
        if (ccList != null) {
            for (String cc : ccList) {
                mimeMessage.addRecipient(RecipientType.CC, new InternetAddress(cc));
            }
        }
        if (bccList != null) {
            for (String bcc : bccList) {
                mimeMessage.addRecipient(RecipientType.BCC, new InternetAddress(bcc));
            }
        }

        Multipart multipart = new MimeMultipart();

        MimeBodyPart bodyMessagePart = new MimeBodyPart();
        bodyMessagePart.setText(bodyMessage);
        bodyMessagePart.setContent(bodyMessage, "text/html");

        for (int i = 0; i < fileNames.size(); i++) {
            MimeBodyPart attachFilePart = new MimeBodyPart();
            attachFilePart.setDataHandler(new DataHandler(new ByteArrayDataSource(files[i], contentTypes.get(i))));
            attachFilePart.setFileName(fileNames.get(i));
            attachFilePart.setDisposition(Part.ATTACHMENT);
            multipart.addBodyPart(attachFilePart);
        }
        multipart.addBodyPart(bodyMessagePart);

        mimeMessage.setContent(multipart);
        mimeMessage.saveChanges();

        send(mimeMessage);
    }

    public void sendEmail(Collection<String> toList, Collection<String> ccList, Collection<String> bccList, String from, String subjectMessage, String bodyMessage, String contentType)
            throws MessagingException {
        MimeMessage mimeMessage = new MimeMessage(session);

        if (contentType != null) {
            mimeMessage.setContent(bodyMessage, contentType);
        } else {
            mimeMessage.setText(bodyMessage);
        }

        mimeMessage.setSubject(subjectMessage);
        mimeMessage.setFrom(new InternetAddress(from));
        if (toList != null) {
            for (String to : toList) {
                mimeMessage.addRecipient(RecipientType.TO, new InternetAddress(to));
            }
        }
        if (ccList != null) {
            for (String cc : ccList) {
                mimeMessage.addRecipient(RecipientType.CC, new InternetAddress(cc));
            }
        }
        if (bccList != null) {
            for (String bcc : bccList) {
                mimeMessage.addRecipient(RecipientType.BCC, new InternetAddress(bcc));
            }
        }
        mimeMessage.saveChanges();
        send(mimeMessage);
    }
}
