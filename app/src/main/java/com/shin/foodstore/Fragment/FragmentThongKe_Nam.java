package com.shin.foodstore.Fragment;

import android.graphics.Color;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.github.mikephil.charting.charts.BarChart;
import com.github.mikephil.charting.components.Description;
import com.github.mikephil.charting.components.XAxis;
import com.github.mikephil.charting.components.YAxis;
import com.github.mikephil.charting.data.BarData;
import com.github.mikephil.charting.data.BarDataSet;
import com.github.mikephil.charting.data.BarEntry;
import com.github.mikephil.charting.formatter.IndexAxisValueFormatter;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.shin.foodstore.Database.DatabaseHDCT;
import com.shin.foodstore.Model.HDCT;
import com.shin.foodstore.Model.Order;
import com.shin.foodstore.R;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.Date;
import java.util.HashMap;
import java.util.Locale;
import java.util.Objects;
import java.util.TreeMap;

public class FragmentThongKe_Nam extends Fragment {
    BarChart thongkenam;
    ArrayList<HDCT> arrayList;
    FirebaseUser firebaseUser;
    DatabaseHDCT daoHDCT;
    String nam;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragmenttk_nam, container, false);
        thongkenam = view.findViewById(R.id.thongkengay);
        firebaseUser = FirebaseAuth.getInstance().getCurrentUser();
        daoHDCT = new DatabaseHDCT(getActivity());
        String currentDate = new SimpleDateFormat("dd-MM-yyyy", Locale.getDefault()).format(new Date());
        nam = currentDate.substring(6, 10);
        DatabaseReference databaseReference = FirebaseDatabase.getInstance().getReference("HDCT");
        arrayList = new ArrayList<>();
        databaseReference.addValueEventListener(new ValueEventListener() {
             final String[] labelName = {"01", "02", "03","04","05","06","07","08","09","10","11","12"};
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                arrayList.clear();
                ArrayList<Order> orderArrayList = new ArrayList<>();
                ArrayList<BarEntry> entryArrayList = new ArrayList<>();
                HashMap<String, ArrayList<Order>> mapOder = new HashMap<>();

                for (DataSnapshot dataSnapshot : snapshot.getChildren()) {
                    HDCT hdct = dataSnapshot.getValue(HDCT.class);
                    arrayList.add(hdct);
                }
                Log.d("VH21", "size " + labelName.length +"/" + arrayList.size());
                for (int positionLabel = 0; positionLabel < labelName.length ; positionLabel++) {
                    ArrayList<Order> listOrder = new ArrayList<>();
                    for (int i = 0; i < arrayList.size(); i++) {
                        Log.d("VH23", "thang " + arrayList.size() +"/" + arrayList.toString());
                        orderArrayList.clear();
                        String thangtk = arrayList.get(i).getNgay().substring(3, 5);
                        Log.d("VH22", "thang " + thangtk + "/" );
                        if (arrayList.get(i).isCheck() && thangtk.equals(labelName[positionLabel])) {
                            orderArrayList.addAll(arrayList.get(i).getOrderArrayList());
                            Log.d("VH0", "onDataChange: " + orderArrayList.toString());
                            for (int j = 0; j < orderArrayList.size(); j++) {
                                if (orderArrayList.get(j).getFood().getTokenstore().equalsIgnoreCase(firebaseUser.getUid())) {
                                    listOrder.add(orderArrayList.get(j));
                                }
                            }
                        }
                    }
                    mapOder.put(labelName[positionLabel], listOrder);
                }
                for (String key : mapOder.keySet()) {
                    int total = 0;
                    for (Order order : Objects.requireNonNull(mapOder.get(key))) {
                        total += order.getSoluongmua() * order.getFood().getGia();
                    }
                    entryArrayList.add(new BarEntry(Integer.parseInt(key), total));
                }

                Log.e("entryArrayList", "onDataChange: " + entryArrayList.toString());

                BarDataSet barDataSet = new BarDataSet(entryArrayList, "Tháng");
                thongkenam.notifyDataSetChanged();
                barDataSet.setColors(Color.CYAN);
                Description des = new Description();
                des.setText("");
                thongkenam.setDescription(des);
                BarData barData = new BarData(barDataSet);
                thongkenam.setData(barData);
                thongkenam.setTouchEnabled(true);
                thongkenam.resetViewPortOffsets();
                thongkenam.setFitBars(true);
                YAxis rightAxis = thongkenam.getAxisRight();
                rightAxis.setEnabled(false);
                XAxis xAxis = thongkenam.getXAxis();
                xAxis.setLabelCount(12);
//                xAxis.setValueFormatter(new IndexAxisValueFormatter(labelName));
                xAxis.setPosition(XAxis.XAxisPosition.BOTTOM);
                xAxis.setDrawAxisLine(false);
                xAxis.setDrawGridLines(false);
                thongkenam.setVisibleXRange(1, 12);
                thongkenam.setDrawBarShadow(false);
                thongkenam.setDrawGridBackground(false);
                thongkenam.setDrawValueAboveBar(false);
                thongkenam.animateY(1000);
                thongkenam.setDrawValueAboveBar(true);
                thongkenam.invalidate();
                databaseReference.removeEventListener(this);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
        return view;
    }
}
