package com.example.wenda.model;



public class MessageResponse extends Message{
    private int messageCounts;
    private int notReadMessageCounts;

    public MessageResponse(Message message) {
        super(message);
    }

    public int getMessageCounts() {
        return messageCounts;
    }

    public void setMessageCounts(int messageCounts) {
        this.messageCounts = messageCounts;
    }

    public int getNotReadMessageCounts() {
        return notReadMessageCounts;
    }

    public void setNotReadMessageCounts(int notReadMessageCounts) {
        this.notReadMessageCounts = notReadMessageCounts;
    }
}
