package com.shin.foodstore.CallBack;

import com.shin.foodstore.Model.Store;

import java.util.ArrayList;

public interface Storecallback {
    void onSuccess(ArrayList<Store> lists);
    void onError(String message);
}
