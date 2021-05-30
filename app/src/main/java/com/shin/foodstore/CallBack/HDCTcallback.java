package com.shin.foodstore.CallBack;

import com.shin.foodstore.Model.HDCT;

import java.util.ArrayList;

public interface HDCTcallback {
    void onSuccess(ArrayList<HDCT> lists);
    void onError(String message);
}