# MailLibrary
a background mail send library
##How to use
BackgroundMail.newBuilder(mContext)
                .withUsername(mSenderName)//发件人
                .withPassword(mPassword)//邮箱密码
                .withMailHost(mMailHost)//邮箱服务器
                .withMailPort(mMailPort)//发件端口
                .withProcessVisibility(true)//是否显示进度条
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
## License

    Licensed under the Apache License, Version 2.0 (the "License");
    you may not use this file except in compliance with the License.
    You may obtain a copy of the License at

       http://www.apache.org/licenses/LICENSE-2.0

    Unless required by applicable law or agreed to in writing, software
    distributed under the License is distributed on an "AS IS" BASIS,
    WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
    See the License for the specific language governing permissions and
    limitations under the License.