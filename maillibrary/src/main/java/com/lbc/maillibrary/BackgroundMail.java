package com.lbc.maillibrary;

import android.app.Fragment;
import android.content.Context;
import android.os.AsyncTask;
import android.support.annotation.ArrayRes;
import android.support.annotation.NonNull;
import android.support.annotation.StringRes;


import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;


public class BackgroundMail {
    String TAG = "BackgroundMail";
    private String username;
    private String password;
    private String mailhost;
    private String port;
    private String mailto;
    private String subject;
    private String body;
    private ArrayList<String> picFilePath;
    private String sendingMessage;
    /*private String sendingMessageSuccess;
    private String sendingMessageError;*/
    private boolean processVisibility = true;
    private ArrayList<String> attachments = new ArrayList<String>();
    private Context mContext;
    private OnSuccessCallback onSuccessCallback;
    private OnFailCallback onFailCallback;

    public interface OnSuccessCallback {
        void onSuccess();
    }

    public interface OnFailCallback {
        void onFail();
    }

    public BackgroundMail(Fragment fragment) {
        this(fragment.getActivity().getApplicationContext());
    }

    public BackgroundMail(Context context) {
        this.mContext = context;
        /*this.sendingMessage = context.getString(R.string.msg_sending_email);
        this.sendingMessageSuccess = context.getString(R.string.msg_email_sent_successfully);
        this.sendingMessageError=context.getString(R.string.msg_error_sending_email);*/
    }

    private BackgroundMail(Builder builder) {
        mContext = builder.context;
        attachments = builder.attachments;
        username = builder.username;
        password = builder.password;
        mailhost = builder.mailhost;
        port = builder.port;
        mailto = builder.mailto;
        subject = builder.subject;
        body = builder.body;
        picFilePath = builder.picFilePathList;
        setSendingMessage(builder.sendingMessage);
        /*setSendingMessageSuccess(builder.sendingMessageSuccess);
        setSendingMessageError(builder.sendingMessageError);*/
        processVisibility = builder.processVisibility;
        setOnSuccessCallback(builder.onSuccessCallback);
        setOnFailCallback(builder.onFailCallback);
    }

    public static Builder newBuilder(Context context) {
        return new Builder(context);
    }

    public static Builder newBuilder(Fragment fragment) {
        return new Builder(fragment.getActivity().getApplicationContext());
    }

    public void setMmailUserName(@NonNull String string) {
        this.username = string;
    }

    public void setMmailUserName(@StringRes int strRes) {
        this.username = mContext.getResources().getString(strRes);
    }

    @NonNull
    public String getMmailUserName() {
        return username;
    }

    public void setMmailPassword(@NonNull String string) {
        this.password = string;
    }

    public void setMmailPassword(@StringRes int strRes) {
        this.password = mContext.getResources().getString(strRes);
    }

    @NonNull
    public String getMmailPassword() {
        return password;
    }

    public void showVisibleProgress(boolean state) {
        this.processVisibility = state;
    }

    public boolean isProgressVisible() {
        return processVisibility;
    }

    public void setMailTo(@NonNull String string) {
        this.mailto = string;
    }

    public void setMailTo(@StringRes int strRes) {
        this.mailto = mContext.getResources().getString(strRes);
    }

    @NonNull
    public String getMailTo() {
        return mailto;
    }

    public void setFormSubject(@NonNull String string) {
        this.subject = string;
    }

    public void setFormSubject(@StringRes int strRes) {
        this.subject = mContext.getResources().getString(strRes);
    }

    @NonNull
    public String getFormSubject() {
        return subject;
    }

    public void setFormBody(@NonNull String string) {
        this.body = string;
    }

    public void setFormBody(@StringRes int strRes) {
        this.body = mContext.getResources().getString(strRes);
    }

    @NonNull
    public String getFormBody() {
        return body;
    }

    public void setSendingMessage(@NonNull String string) {
        this.sendingMessage = string;
    }

    public void setSendingMessage(@StringRes int strRes) {
        this.sendingMessage = mContext.getResources().getString(strRes);
    }

    @NonNull
    public String getSendingMessage() {
        return sendingMessage;
    }

    /*public void setSendingMessageSuccess(@Nullable String string) {
        this.sendingMessageSuccess = string;
    }

    public void setSendingMessageSuccess(@StringRes int strRes) {
        this.sendingMessageSuccess = mContext.getResources().getString(strRes);
    }

    @Nullable
    public String getSendingMessageSuccess() {
        return sendingMessageSuccess;
    }

    public void setSendingMessageError(@Nullable String string) {
        this.sendingMessageError = string;
    }

    public void setSendingMessageError(@StringRes int strRes) {
        this.sendingMessageError = mContext.getResources().getString(strRes);
    }

    @Nullable
    public String getSeningMessageError() {
        return sendingMessageError;
    }*/

    public void addAttachment(@NonNull String attachment) {
        this.attachments.add(attachment);
    }

    public void addAttachment(@StringRes int strRes) {
        this.attachments.add(mContext.getResources().getString(strRes));
    }

    public void addAttachments(@NonNull List<String> attachments) {
        this.attachments.addAll(attachments);
    }

    public void addAttachments(String...attachments) {
        this.attachments.addAll(Arrays.asList(attachments));
    }

    @NonNull
    public List<String> getAttachments() {
        return attachments;
    }

    public void setOnSuccessCallback(OnSuccessCallback onSuccessCallback) {
        this.onSuccessCallback = onSuccessCallback;
    }

    public void setOnFailCallback(OnFailCallback onFailCallback) {
        this.onFailCallback = onFailCallback;
    }

    public void send() {

        /*if (TextUtils.isEmpty(username)) {
            throw new IllegalArgumentException("You didn't set a mail username");
        }
        if (TextUtils.isEmpty(password)) {
            throw new IllegalArgumentException("You didn't set a mail password");
        }
        if (TextUtils.isEmpty(mailto)) {
            throw new IllegalArgumentException("You didn't set a mail recipient");
        }
        if (TextUtils.isEmpty(body)) {
            throw new IllegalArgumentException("You didn't set a body");
        }
        if (TextUtils.isEmpty(subject)) {
            throw new IllegalArgumentException("You didn't set a subject");
        }*/
        new SendEmailTask().execute();
    }

    public class SendEmailTask extends AsyncTask<String, Void, Boolean> {
        private CustomProgressDialog progressDialog;

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            if (processVisibility) {
                progressDialog = new CustomProgressDialog(mContext,sendingMessage);
                progressDialog.setCancelable(false);
                progressDialog.show();
            }
        }

        @Override
        protected Boolean doInBackground(String... arg0) {
            try {
                MailSender sender = new MailSender(username, password,mailhost,port);
                MailInfoModel mailinfo = new MailInfoModel(username, mailto, subject, body, attachments, picFilePath);
                sender.sendMail(mailinfo);
            } catch (Exception e) {
                e.printStackTrace();
                return false;
            }
            return true;
        }

        @Override
        protected void onPostExecute(Boolean result) {
            super.onPostExecute(result);
            if (processVisibility) {
                progressDialog.dismiss();
                if (result) {
                    //add toast if need
                    if (onSuccessCallback != null) {
                        onSuccessCallback.onSuccess();
                    }
                }else {
                    if (onFailCallback != null) {
                        onFailCallback.onFail();
                    }
                }
            }
        }
    }

    public static final class Builder {
        private Context context;
        private String username;
        private String password;
        private String mailhost;
        private String port;
        private String mailto;
        private String subject;
        private String body;
        private ArrayList<String> attachments = new ArrayList<String>();
        private String sendingMessage;
        /*private String sendingMessageSuccess;
        private String sendingMessageError;*/
        private boolean processVisibility;
        private OnSuccessCallback onSuccessCallback;
        private OnFailCallback onFailCallback;
        private ArrayList<String> picFilePathList;

        private Builder(Context context) {
            this.context = context;
        }

        public Builder withUsername(@NonNull String username) {
            this.username = username;
            return this;
        }

        public Builder withUsername(@StringRes int usernameRes) {
            this.username = context.getResources().getString(usernameRes);
            return this;
        }

        public Builder withPassword(@NonNull String password) {
            this.password = password;
            return this;
        }

        public Builder withPassword(@StringRes int passwordRes) {
            this.password = context.getResources().getString(passwordRes);
            return this;
        }

        public Builder withMailHost(@NonNull String mailhost){
            this.mailhost = mailhost;
            return this;
        }
        public Builder withMailPort(@NonNull String port){
            this.port = port;
            return this;
        }
        public Builder withMailto(@NonNull String mailto) {
            this.mailto = mailto;
            return this;
        }

        public Builder withMailto(@StringRes int mailtoRes) {
            this.mailto = context.getResources().getString(mailtoRes);
            return this;
        }

        public Builder withSubject(@NonNull String subject) {
            this.subject = subject;
            return this;
        }

        public Builder withSubject(@StringRes int subjectRes) {
            this.subject = context.getResources().getString(subjectRes);
            return this;
        }

        public Builder withBody(@NonNull String body) {
            this.body = body;
            return this;
        }

        public Builder withBody(@StringRes int bodyRes) {
            this.body = context.getResources().getString(bodyRes);
            return this;
        }

        public Builder withAttachments(@NonNull ArrayList<String> attachments) {
            this.attachments.addAll(attachments);
            return this;
        }

        public Builder withAttachments(String...attachments) {
            this.attachments.addAll(Arrays.asList(attachments));
            return this;
        }

        public Builder withAttachments(@ArrayRes int attachmentsRes) {
            this.attachments.addAll(Arrays.asList(context.getResources().getStringArray(attachmentsRes)));
            return this;
        }

        public Builder withSendingMessage(@NonNull String sendingMessage) {
            this.sendingMessage = sendingMessage;
            return this;
        }

        public Builder withSendingMessage(@StringRes int sendingMessageRes) {
            this.sendingMessage = context.getResources().getString(sendingMessageRes);
            return this;
        }

        /*public Builder withSendingMessageSuccess(@Nullable String sendingMessageSuccess) {
            this.sendingMessageSuccess = sendingMessageSuccess;
            return this;
        }

        public Builder withSendingMessageSuccess(@StringRes int sendingMessageSuccessRes) {
            this.sendingMessageSuccess = context.getResources().getString(sendingMessageSuccessRes);
            return this;
        }

        public Builder withSendingMessageError(@Nullable String sendingMessageError) {
            this.sendingMessageError = sendingMessageError;
            return this;
        }

        public Builder withSendingMessageError(@StringRes int sendingMessageError) {
            this.sendingMessageError = context.getResources().getString(sendingMessageError);
            return this;
        }*/

        public Builder withProcessVisibility(boolean processVisibility) {
            this.processVisibility = processVisibility;
            return this;
        }

        public Builder withPicContent(@NonNull ArrayList<String> filePathList){
            this.picFilePathList = filePathList;
            return this;
        }
        public Builder withOnSuccessCallback(OnSuccessCallback onSuccessCallback) {
            this.onSuccessCallback = onSuccessCallback;
            return this;
        }

        public Builder withOnFailCallback(OnFailCallback onFailCallback) {
            this.onFailCallback = onFailCallback;
            return this;
        }

        public BackgroundMail build() {
            return new BackgroundMail(this);
        }

        public BackgroundMail send() {
            BackgroundMail backgroundMail = build();
            backgroundMail.send();
            return backgroundMail;
        }
    }
}
