package com.lbc.maillibrary;

import java.util.ArrayList;

/**
 * A sample model. Replace this with your own.
 */
public class MailInfoModel {

    private String mSender;
    private String mRecipients;
    private String mSubject;
    private String mBody;
    private ArrayList<String> mAttachments;
    private ArrayList<String> mPictureList;

    public MailInfoModel(String mSender, String mRecipients, String mSubject, String mBody,
            ArrayList<String> mAttachments, ArrayList<String> mPictureList) {
        this.mSender = mSender;
        this.mRecipients = mRecipients;
        this.mSubject = mSubject;
        this.mBody = mBody;
        this.mAttachments = mAttachments;
        this.mPictureList = mPictureList;
    }

    public String getSender() {
        return mSender;
    }

    public String getRecipients() {
        return mRecipients;
    }
    
    public String getSubject() {
        return mSubject;
    }

    public String getBody() {
        return mBody;
    }
    
    public ArrayList<String> getAttachments() {
        return mAttachments;
    }

    public ArrayList<String> getContentPicture() {
        return mPictureList;
    }  

}
