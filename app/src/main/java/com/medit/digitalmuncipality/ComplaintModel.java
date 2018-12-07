package com.medit.digitalmuncipality;

public class ComplaintModel {

    String complaint;
    String topic;

    public ComplaintModel(String complaint, String topic) {
        this.complaint = complaint;
        this.topic = topic;
    }

    public ComplaintModel() {
    }

    public String getTopic() {
        return topic;
    }

    public void setTopic(String topic) {
        this.topic = topic;
    }

    public String getComplaint() {

        return complaint;
    }

    public void setComplaint(String complaint) {
        this.complaint = complaint;
    }
}
