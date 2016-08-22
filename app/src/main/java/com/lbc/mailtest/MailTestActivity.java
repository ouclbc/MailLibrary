package com.lbc.mailtest;

import android.content.Context;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import com.lbc.maillibrary.BackgroundMail;

import java.io.File;
import java.util.ArrayList;

public class MailTestActivity extends AppCompatActivity {
    private static final String TAG = MailTestActivity.class.getSimpleName();
    /**
     * 发件人
     */
    private String mSenderName ="";
    private String mPassword ="";
    /**
     * 发件服务器
     */
    private String mMailHost ="";
    /**
     * 发件端口
     */
    private String mMailPort ="";
    /**
     * 收件人
     */
    private String mRecipients = "";
    private String mSubject ="";
    private String mBody ="";
    /**
     * 图片列表
     */
    private ArrayList<String> mPicturePathlist;
    /**
     * 附件列表
     */
    private ArrayList<String> mAttachments;
    /**
     *
     */
    private Context mContext;

    EditText from;
    EditText password;
    EditText mailhost;
    EditText mailport;
    EditText to;
    EditText subject;
    EditText body;
    EditText picpath;
    EditText attachements;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mContext = this;
        mPicturePathlist = new ArrayList<>();
        mAttachments = new ArrayList<>();
        setContentView(R.layout.activity_mail_test);
        from = (EditText) findViewById(R.id.from);
        password = (EditText) findViewById(R.id.password);
        mailhost = (EditText) findViewById(R.id.mailhost);
        mailport = (EditText) findViewById(R.id.mailport);
        to = (EditText) findViewById(R.id.to);
        subject = (EditText) findViewById(R.id.suject);
        body = (EditText) findViewById(R.id.body);
        picpath = (EditText) findViewById(R.id.picpath);
        attachements = (EditText) findViewById(R.id.attachment);
        findViewById(R.id.send).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                mSenderName = from.getText().toString();
                mPassword = password.getText().toString();
                mMailHost = mailhost.getText().toString();
                mMailPort = mailport.getText().toString();
                mRecipients = to.getText().toString();
                File directory = new File(picpath.getText().toString());
                if (directory.exists()) {
                    for (File f : directory.listFiles()) {
                        mPicturePathlist.add(f.getAbsolutePath());
                    }
                }
                File attachdirectory = new File(attachements.getText().toString());
                if (directory.exists()) {
                    for (File f : directory.listFiles()) {
                        mAttachments.add(f.getAbsolutePath());
                    }
                }
                sendMailByApacheJar();
            }
        });

    }
    public void sendMailByApacheJar(){
        BackgroundMail.newBuilder(mContext)
                .withUsername(mSenderName)
                .withPassword(mPassword)
                .withMailHost(mMailHost)
                .withMailPort(mMailPort)
                .withProcessVisibility(true)
                .withSendingMessage(mContext.getString(R.string.msg_sending_email))
                .withMailto(mRecipients)//收件人
                .withSubject(mSubject)//主题
                .withBody(mBody)//正文内容是否需要
                .withPicContent(mPicturePathlist)//图片路径
                .withAttachments(mAttachments)//邮件附件
                .withOnSuccessCallback(new BackgroundMail.OnSuccessCallback() {
                    @Override
                    public void onSuccess() {
                        Log.d(TAG, "onSuccess: send mail sucess");
                        Toast.makeText(mContext,"发送邮件成功",Toast.LENGTH_LONG).show();
                        //ToastUtil.toastshow(mContext, R.string.msg_email_sent_successfully,R.drawable.ic_toast_successed_90x90);
                    }
                })
                .withOnFailCallback(new BackgroundMail.OnFailCallback() {
                    @Override
                    public void onFail() {
                        Log.d(TAG, "onFail: send mail fail");
                        Toast.makeText(mContext,"发送邮件失败",Toast.LENGTH_LONG).show();
                        //ToastUtil.toastshow(mContext, R.string.msg_error_sending_email,R.drawable.ic_toast_fail_90x90);
                    }
                })
                .send();
    }
}
