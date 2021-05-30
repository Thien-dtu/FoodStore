package com.shin.foodstore.CallBack;

import com.shin.foodstore.Model.Categories;

import java.util.ArrayList;

public interface Categoriescallback {
    void onSuccess(ArrayList<Categories> lists);
    void onError(String message);
}
