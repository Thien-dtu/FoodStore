package com.shin.foodstore.Fragment;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.shin.foodstore.Adapter.XacnhanAdapter;
import com.shin.foodstore.CallBack.HDCTcallback;
import com.shin.foodstore.Database.DatabaseHDCT;
import com.shin.foodstore.Model.HDCT;
import com.shin.foodstore.Model.Order;
import com.shin.foodstore.R;

import java.util.ArrayList;

public class FragmentGDThatBai extends Fragment {
    XacnhanAdapter xacnhanAdapter;
    DatabaseHDCT daoHDCT;
    RecyclerView rcvthanhcong;
    ArrayList<HDCT> arrayList;
    FirebaseUser firebaseUser;
    public static String uidstore1;
    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragmentgdtb,container,false);
        rcvthanhcong= view.findViewById(R.id.rcvthanhcong);
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(getActivity(),RecyclerView.VERTICAL,false);
        rcvthanhcong.setLayoutManager(linearLayoutManager);
        daoHDCT = new DatabaseHDCT(getActivity());
        arrayList=new ArrayList<>();
        firebaseUser = FirebaseAuth.getInstance().getCurrentUser();

        daoHDCT.getAll(new HDCTcallback() {
            @Override
            public void onSuccess(ArrayList<HDCT> lists) {
                String uidstore = "";
                arrayList.clear();
                ArrayList<Order> orderArrayList = new ArrayList<>();
                orderArrayList.clear();
                for (int i =0;i<lists.size();i++){
                    if ( lists.get(i).isCheck() == false) {

                        orderArrayList.addAll(lists.get(i).getOrderArrayList());


//
//
                    }
//                    for (int j = 0; j < orderArrayList.size(); j++) {
//                        if (orderArrayList.get(j).getStore().getTokenstore().equalsIgnoreCase(firebaseUser.getUid())) {
//                            uidstore = orderArrayList.get(j).getStore().getTokenstore();
//
//
//
//                        }
//                    }

//

                }
                for (int k = 0; k < lists.size(); k++) {
                    if (uidstore.equalsIgnoreCase(firebaseUser.getUid()) && lists.get(k).isCheck() == false) {
                        arrayList.add(lists.get(k));
                        xacnhanAdapter = new XacnhanAdapter(arrayList, getActivity());
                        rcvthanhcong.setAdapter(xacnhanAdapter);
                    }
                }
            }


            @Override
            public void onError(String message) {

            }
        });
        return view;
    }
}
