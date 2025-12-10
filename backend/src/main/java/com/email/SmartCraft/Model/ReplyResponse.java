package com.email.SmartCraft.Model;

public class ReplyResponse {
    private String replyText;
    private boolean success;

    public ReplyResponse() {
    }

    public ReplyResponse(String replyText, boolean success) {
        this.replyText = replyText;
        this.success = success;
    }

    public String getReplyText() {
        return replyText;
    }

    public void setReplyText(String replyText) {
        this.replyText = replyText;
    }

    public boolean isSuccess() {
        return success;
    }

    public void setSuccess(boolean success) {
        this.success = success;
    }
}