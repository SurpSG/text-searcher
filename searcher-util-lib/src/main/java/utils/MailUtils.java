package utils;


import javax.mail.*;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeMessage;
import java.io.UnsupportedEncodingException;
import java.util.Properties;

public class MailUtils {
    static final String ENCODING = "UTF-8";
    public static String smtpHost="smtp.gmail.com";
    public static String DestinationAddress="batynchuk@gmail.com";
    public static String from="TextSearcher";
    public static String login="javatestbat@gmail.com";
    public static String password="qweqazqwe";
    public static String smtpPort="465";

    public static void sendSimpleMessage(String content, String subject)
            throws MessagingException, UnsupportedEncodingException {
        Authenticator auth = new MyAuthenticator(login, password);
        Properties props = System.getProperties();

        props.put("mail.mime.charset", ENCODING);
        props.put("mail.transport.protocol", "smtps");
        props.put("mail.smtps.host", login);
        props.put("mail.smtp.host", smtpHost);
        props.put("mail.smtp.socketFactory.port", smtpPort);
        props.put("mail.smtp.socketFactory.class", "javax.net.ssl.SSLSocketFactory");
        props.put("mail.smtp.auth", "true");
        Session session = Session.getDefaultInstance(props, auth);

        Message msg = new MimeMessage(session);
        msg.setFrom(new InternetAddress(login, from));
        msg.setRecipient(Message.RecipientType.TO, new InternetAddress(DestinationAddress));
        msg.setSubject(subject);
        msg.setText(content);
        Transport.send(msg);
    }
}

class MyAuthenticator extends Authenticator {
    private String user;
    private String password;

    MyAuthenticator(String user, String password) {
        this.user = user;
        this.password = password;
    }

    public PasswordAuthentication getPasswordAuthentication() {
        String user = this.user;
        String password = this.password;
        return new PasswordAuthentication(user, password);
    }
}