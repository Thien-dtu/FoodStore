package com.shin.foodstore.Fragment;

import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.RecyclerView;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.shin.foodstore.Model.HDCT;
import com.shin.foodstore.Model.Order;
import com.shin.foodstore.R;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Date;
import java.util.Locale;

public class FragmentThongKe_TopSanPham extends Fragment {
    String ngay, thang, nam;
    Button btntim;
    ArrayList<HDCT> datahdct ;
    RecyclerView rcvbestbook;
    EditText tvsearch;
    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragmenttk_topsanpham,container,false);
        tvsearch = view.findViewById(R.id.tvsearch);
        btntim = view.findViewById(R.id.btntim);
        rcvbestbook= view.findViewById(R.id.rcvbestbook);

        String currentDate = new SimpleDateFormat("dd/MM/yyyy", Locale.getDefault()).format(new Date());

        ngay = currentDate.substring(0, 2);
        thang = currentDate.substring(3, 5);
        nam = currentDate.substring(6, currentDate.length());
        datahdct = new ArrayList<>();
        btntim.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                FirebaseDatabase.getInstance().getReference("HDCT").addValueEventListener(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                        datahdct.clear();
                        for (DataSnapshot data : dataSnapshot.getChildren()) {

                            HDCT hoadonchitiet = data.getValue(HDCT.class);
                            String ngaymua = hoadonchitiet.getNgay();
                            Log.i("ngay",ngaymua);
                            String thang = ngaymua.substring(3, 5);
                            if (thang.equals(tvsearch.getText().toString())) {
                                datahdct.add(hoadonchitiet);
                            } else {

                            }
                        }
                        if (datahdct.size() == 0) {
                            Toast.makeText(getActivity(), "Không Có Thông Tin ", Toast.LENGTH_SHORT).show();
                        }else {
                            ArrayList<Order> orders = new ArrayList<>();
                            if (datahdct.size() > 10) {
                                for (int i = 0; i < datahdct.size(); i++) {
                                    orders= datahdct.get(i).getOrderArrayList();

                                }
                                for (int k =0;k<orders.size();k++){
                                    int sl1 = orders.get(k).getSoluongmua();
                                    for (int j = k + 1; j < orders.size() - 1; j++) {
                                        int sl2 = orders.get(j).getSoluongmua();
                                        if (sl1 < sl2) {
                                            Order hoadonchitiet = orders.get(k);
                                            orders.set(k, orders.get(j));
                                            orders.set(j, hoadonchitiet);
                                        }
                                        Toast.makeText(getActivity(), "sizeorder"+orders.size(), Toast.LENGTH_SHORT).show();
                                    }
                                }
                            }

                        }
                        Collections.reverse(datahdct);
                        Toast.makeText(getActivity(), "size"+datahdct.size(), Toast.LENGTH_SHORT).show();

                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError databaseError) {

                    }
                });
            }
        });
        return view;
    }

}
