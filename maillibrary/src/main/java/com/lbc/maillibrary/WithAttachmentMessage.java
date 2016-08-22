package com.lbc.maillibrary;
import android.text.TextUtils;
import java.util.ArrayList;
import javax.activation.DataHandler;  
import javax.activation.FileDataSource;  
import javax.mail.Message;  
import javax.mail.Session;  
import javax.mail.internet.InternetAddress;  
import javax.mail.internet.MimeBodyPart;  
import javax.mail.internet.MimeMessage;  
import javax.mail.internet.MimeMultipart;  
import javax.mail.util.ByteArrayDataSource;
 
/**  
 * 创建内含附件、图文并茂的邮件  
 *  
 */ 
public class WithAttachmentMessage {
    /**
     * 
     * <p>Title: createAttachment.</p>
     * <p>Description: 根据传入的文件路径创建附件并返回 .</p>
     * 
     * @param fileName
     * @return
     * @throws Exception
     */
    public MimeBodyPart createAttachment(String fileName) throws Exception {  
        MimeBodyPart attachmentPart = new MimeBodyPart();  
        FileDataSource fds = new FileDataSource(fileName);  
        attachmentPart.setDataHandler(new DataHandler(fds));  
        attachmentPart.setFileName(fds.getName());  
        return attachmentPart;  
    }  
 
    /**
     * 
     * <p>Title: createContent.</p>
     * <p>Description: 根据传入的邮件正文body和文件路径创建图文并茂的正文部分  .</p>
     * 
     * @param body
     * @param fileName
     * @return
     * @throws Exception
     */
    public MimeBodyPart createContent(String body, String fileName) throws Exception {
        FileDataSource fds = new FileDataSource(fileName);
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
        jpgBody.setDataHandler(new DataHandler(fds));
        jpgBody.setContentID(fds.getName());
        contentMulti.addBodyPart(jpgBody);

        // 将上面"related"型的 MimeMultipart 对象作为邮件的正文
        contentBody.setContent(contentMulti);
        return contentBody;
    }  
 
    /**
     * 
     * <p>Title: createMessage.</p>
     * <p>Description: 根据传入的 Seesion 对象创建混合型的 MIME消息.</p>
     * 
     * @param session
     * @param mailinfo
     * @return
     * @throws Exception
     */
    public MimeMessage createMessage(Session session, MailInfoModel mailinfo) throws Exception {
        if (TextUtils.isEmpty(mailinfo.getSender())) {
            throw new IllegalArgumentException("mail sender can not be null");
        }
        if (TextUtils.isEmpty(mailinfo.getRecipients())) {
            throw new IllegalArgumentException("mail recipients can not be null");
        }
        String from = mailinfo.getSender();
        String to = mailinfo.getRecipients();
        String subject = mailinfo.getSubject();
        String body = mailinfo.getBody();
        ArrayList<String> attachments = mailinfo.getAttachments();
        ArrayList<String> bodyPicList = mailinfo.getContentPicture();

        MimeMessage msg = new MimeMessage(session);
        msg.setFrom(new InternetAddress(from));
        if (to.indexOf(',') > 0)
            msg.setRecipients(Message.RecipientType.TO, InternetAddress.parse(to));
        else
            msg.setRecipient(Message.RecipientType.TO, new InternetAddress(to));
        msg.setSubject(subject);

        MimeMultipart allPart = new MimeMultipart("mixed");
        // 创建邮件的各个 MimeBodyPart 部分
        if (!attachments.isEmpty()) {
            for (int i = 0; i < attachments.size(); i++) {
                if (!attachments.get(i).isEmpty()) {
                    allPart.addBodyPart(createAttachment(attachments.get(i)));
                }
            }
        }
        if (bodyPicList == null || bodyPicList.size() == 0) {
            DataHandler handler = new DataHandler(new ByteArrayDataSource(body.getBytes(), "text/plain"));
            msg.setDataHandler(handler);
        } else {
            body = "<h4>" + mailinfo.getBody() + "</h4> </br>";
            for (int listsize = 0; listsize < bodyPicList.size(); listsize++) {
                FileDataSource fd = new FileDataSource(bodyPicList.get(listsize));
                body = body + "<img src = \"cid:" + fd.getName() + "\"></br>";
                body = body + "<a href = \"#\">" + fd.getName() + "</a></br>";
            }
            for (int listsize = 0; listsize < bodyPicList.size(); listsize++) {
                MimeBodyPart content = createContent(body, bodyPicList.get(listsize));
                allPart.addBodyPart(content);
            }
        }
        // 将上面混合型的 MimeMultipart 对象作为邮件内容并保存
        msg.setContent(allPart);
        msg.saveChanges();
        return msg;
    }  
} 