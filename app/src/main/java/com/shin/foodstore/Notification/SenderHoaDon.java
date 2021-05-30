package com.shin.foodstore.Notification;

public class SenderHoaDon {
    public DataHoaDon data;
    public String to;

    public SenderHoaDon() {
    }

    public SenderHoaDon(DataHoaDon data, String to) {
        this.data = data;
        this.to = to;
    }
}
