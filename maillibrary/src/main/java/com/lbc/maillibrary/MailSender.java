package com.lbc.maillibrary;

import javax.activation.DataHandler;   
import javax.activation.DataSource;   
import javax.activation.FileDataSource;
import javax.mail.BodyPart;  
import javax.mail.PasswordAuthentication;   
import javax.mail.Session;   
import javax.mail.Transport;  
import javax.mail.internet.MimeBodyPart;
import javax.mail.internet.MimeMessage;
import javax.mail.internet.MimeMultipart;

import android.util.Log;

import java.io.ByteArrayInputStream;
import java.io.IOException;   
import java.io.InputStream;   
import java.io.OutputStream;   
import java.security.Security;
import java.util.Properties;   

public class MailSender extends javax.mail.Authenticator {
    private static final String TAG = MailSender.class.getSimpleName();
    private static String defaultmailhost = "smtp.163.com";
    private static String defaultmailport = "465";
    private String user;   
    private String password;   
    private Session session;
    private MimeMultipart allPart;

    static {   
        Security.addProvider(new JSSEProvider());   
    }  

    public MailSender(String user, String password){
        this(user, password, defaultmailhost, defaultmailport);
    }
    public MailSender(String user, String password,String mailhost,String port) {
    	Log.d(TAG, "Mail sender");
        this.user = user;   
        this.password = password;   

        Properties props = new Properties();   
        props.setProperty("mail.transport.protocol", "smtp");   
        props.setProperty("mail.host", mailhost);   
        props.put("mail.smtp.auth", "true");   
        props.put("mail.smtp.port", port); 
        props.put("mail.smtp.socketFactory.port", port);   
        props.put("mail.smtp.socketFactory.class", "javax.net.ssl.SSLSocketFactory");
        props.put("mail.smtp.socketFactory.fallback", "false");   
        props.setProperty("mail.smtp.quitwait", "false");   

        session = Session.getDefaultInstance(props, this);
        allPart = new MimeMultipart("mixed");//related mixed
    }   

    /**
     * 
     * <p>Title: PasswordAuthentication.</p>
     * <p>Description: 获取邮件服务器的PasswordAuthentication对象.</p>
     * 
     * @return
     */
    protected PasswordAuthentication getPasswordAuthentication() {   
        return new PasswordAuthentication(user, password);   
    }   

    /**
     * 
     * <p>Title: sendMail.</p>
     * <p>Description: 邮件发送.</p>
     * 
     * @param mailinfo
     * @throws Exception
     */
    public synchronized void sendMail(MailInfoModel mailinfo) throws Exception {
        WithAttachmentMessage mail = new WithAttachmentMessage();  
        MimeMessage message = mail.createMessage(session,mailinfo);
        Transport.send(message);
    }
    /**
     * 
     * <p>Title: addAttachment.</p>
     * <p>Description: 添加附件.</p>
     * 
     * @param filename
     * @throws Exception
     */
    public void addAttachment(String filename) throws Exception {
        BodyPart messageBodyPart = new MimeBodyPart();
        DataSource source = new FileDataSource(filename);
        messageBodyPart.setDataHandler(new DataHandler(source));
        Log.d(TAG, "filename:"+filename);
        messageBodyPart.setFileName(filename);
        allPart.addBodyPart(messageBodyPart);
    }
    /**
     *  
     * <p>Title: createContent.</p>
     * <p>Description: 根据传入的邮件正文body和文件路径创建图文并茂的正文部分 .</p>
     * 
     * @param body
     * @param fileName
     * @return
     * @throws Exception
     */
    public MimeBodyPart createContent(String body, String fileName) throws Exception {
        // 用于保存最终正文部分
        MimeBodyPart contentBody = new MimeBodyPart();
        // 用于组合文本和图片，"related"型的MimeMultipart对象
        MimeMultipart contentMulti = new MimeMultipart("related");
        // 正文的文本部分
        MimeBodyPart textBody = new MimeBodyPart();
        textBody.setContent(body, "text/html;charset=gbk");
        contentMulti.addBodyPart(textBody);
        // 正文的图片部分
        MimeBodyPart jpgBody = new MimeBodyPart();
        FileDataSource fds = new FileDataSource(fileName);
        jpgBody.setDataHandler(new DataHandler(fds));
        Log.d(TAG, "fds.getName():"+fds.getName());
        jpgBody.setContentID("logo_jpg");
        contentMulti.addBodyPart(jpgBody);
        // 将上面"related"型的 MimeMultipart 对象作为邮件的正文
        contentBody.setContent(contentMulti);
        return contentBody;
    }  

    public class ByteArrayDataSource implements DataSource {   
        private byte[] data;   
        private String type;   

        public ByteArrayDataSource(byte[] data, String type) {   
            super();   
            this.data = data;   
            this.type = type;   
        }   

        public ByteArrayDataSource(byte[] data) {   
            super();   
            this.data = data;   
        }   

        public void setType(String type) {   
            this.type = type;   
        }   

        public String getContentType() {   
            if (type == null)   
                return "application/octet-stream";   
            else  
                return type;   
        }   

        public InputStream getInputStream() throws IOException {   
            return new ByteArrayInputStream(data);   
        }   

        public String getName() {   
            return "ByteArrayDataSource";   
        }   

        public OutputStream getOutputStream() throws IOException {   
            throw new IOException("Not Supported");   
        }   
    }   
}  
