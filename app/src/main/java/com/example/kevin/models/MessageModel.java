package com.example.kevin.models;

public class MessageModel {

    private String messageId;
    private String senderId;
    private String message;
    private String imageUrl;
    private long timestamp;

    public MessageModel() {
        // Necesario para Firestore
    }

    public MessageModel(String messageId, String senderId, String message, String imageUrl, long timestamp) {
        this.messageId = messageId;
        this.senderId = senderId;
        this.message = message;
        this.imageUrl = imageUrl;
        this.timestamp = timestamp;
    }

    public String getMessageId() { return messageId; }
    public String getSenderId() { return senderId; }
    public String getMessage() { return message; }
    public String getImageUrl() { return imageUrl; }
    public long getTimestamp() { return timestamp; }

    public void setMessageId(String messageId) { this.messageId = messageId; }
    public void setSenderId(String senderId) { this.senderId = senderId; }
    public void setMessage(String message) { this.message = message; }
    public void setImageUrl(String imageUrl) { this.imageUrl = imageUrl; }
    public void setTimestamp(long timestamp) { this.timestamp = timestamp; }
}
