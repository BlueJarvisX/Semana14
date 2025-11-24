package com.example.kevin.models;

public class Message {

    private String id;
    private String chatId;
    private String senderId;
    private String senderName;
    private String receiverId;
    private String body;      // mensaje de texto
    private String imageUrl;  // si es imagen
    private long timestamp;
    private boolean seen;
    private boolean delivered;

    public Message() {
        // Necesario para Firestore
    }

    public Message(String id, String chatId, String senderId, String senderName,
                   String receiverId, String body, String imageUrl,
                   long timestamp, boolean seen, boolean delivered) {

        this.id = id;
        this.chatId = chatId;
        this.senderId = senderId;
        this.senderName = senderName;
        this.receiverId = receiverId;
        this.body = body;
        this.imageUrl = imageUrl;
        this.timestamp = timestamp;
        this.seen = seen;
        this.delivered = delivered;
    }

    // =================
    //      GETTERS
    // =================

    public String getId() { return id; }
    public String getChatId() { return chatId; }
    public String getSenderId() { return senderId; }
    public String getSenderName() { return senderName; }
    public String getReceiverId() { return receiverId; }
    public String getBody() { return body; }

    // Alias necesario para el Adapter:
    public String getText() { return body; }

    public String getImageUrl() { return imageUrl; }
    public long getTimestamp() { return timestamp; }
    public boolean isSeen() { return seen; }
    public boolean isDelivered() { return delivered; }
}
