package com.shin.foodstore.CallBack;

import com.shin.foodstore.Model.Food;

import java.util.ArrayList;

public interface Foodcallback {
    void onSuccess(ArrayList<Food> lists);
    void onError(String message);
}

