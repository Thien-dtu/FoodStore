package com.shin.foodstore.Fragment;

import android.app.DatePickerDialog;
import android.graphics.Color;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.DatePicker;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.github.mikephil.charting.charts.BarChart;
import com.github.mikephil.charting.data.BarData;
import com.github.mikephil.charting.data.BarDataSet;
import com.github.mikephil.charting.data.BarEntry;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.shin.foodstore.Model.HDCT;
import com.shin.foodstore.Model.Order;
import com.shin.foodstore.R;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;

public class FragmentThongKe_Ngay extends Fragment {
    BarChart thongkengay;
    String ngay;
    ArrayList<HDCT> arrayList;
    ArrayList<HDCT> arrayListHDCT;
    FirebaseUser firebaseUser;
    Button btnngaydau,btnngaycuoi;
    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragmenttk_ngay,container,false);
        thongkengay = view.findViewById(R.id.thongkengay);
        btnngaydau = view.findViewById(R.id.btnngaydau);
        btnngaycuoi = view.findViewById(R.id.btnngaycuoi);
        String currentDate = new SimpleDateFormat("dd-MM-yyyy", Locale.getDefault()).format(new Date());
        ngay = currentDate.substring(0, 2);
        arrayListHDCT = new ArrayList<>();
        firebaseUser = FirebaseAuth.getInstance().getCurrentUser();
        btnngaydau.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                final Calendar calendar = Calendar.getInstance();
                DatePickerDialog datePickerDialog = new DatePickerDialog(getContext(), new DatePickerDialog.OnDateSetListener() {
                    @Override
                    public void onDateSet(DatePicker view, int year, int month, int dayOfMonth) {
                        SimpleDateFormat simpleDateFormat= new SimpleDateFormat("dd-MM-yyyy");
                        calendar.set(year,month,dayOfMonth);
                        String date = simpleDateFormat.format(calendar.getTime());
                        btnngaydau.setText(date);
                    }
                },calendar.get(Calendar.YEAR),calendar.get(Calendar.MONTH),calendar.get(Calendar.DAY_OF_MONTH));
                datePickerDialog.show();
            }
        });
        btnngaycuoi.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                final Calendar calendar = Calendar.getInstance();
                DatePickerDialog datePickerDialog = new DatePickerDialog(getContext(), new DatePickerDialog.OnDateSetListener() {
                    @Override
                    public void onDateSet(DatePicker view, int year, int month, int dayOfMonth) {
                        SimpleDateFormat simpleDateFormat= new SimpleDateFormat("dd-MM-yyyy");
                        calendar.set(year,month,dayOfMonth);
                        String date = simpleDateFormat.format(calendar.getTime());
                        btnngaycuoi.setText(date);
                        String ngaydau1 = btnngaydau.getText().toString().trim();
                        //String ngaycuoi1 = txtngaycuoi.getText().toString().trim();

                        DatabaseReference database = FirebaseDatabase.getInstance().getReference("HDCT");

                        database.orderByChild("ngay").startAt(ngaydau1).endAt(date).addListenerForSingleValueEvent(new ValueEventListener() {

                            ArrayList<Order> orderArrayList3 = new ArrayList<>();
                            @Override
                            public void onDataChange(@NonNull DataSnapshot snapshot) {
                                arrayListHDCT.clear();
                                String uidstore = "";

                                ArrayList<Order> orderArrayList = new ArrayList<>();
                                orderArrayList.clear();
                                for (DataSnapshot dataSnapshot : snapshot.getChildren()) {
                                    HDCT hdct = dataSnapshot.getValue(HDCT.class);
                                    arrayListHDCT.add(hdct);
                                    for (int i = 0; i < arrayListHDCT.size(); i++) {
                                        if (arrayList.get(i).isCheck() == true) {
                                            orderArrayList.addAll(arrayListHDCT.get(i).getOrderArrayList());

                                        }
                                        for (int j = 0; j < orderArrayList.size(); j++) {
                                            if (orderArrayList.get(j).getStore().getTokenstore().equalsIgnoreCase(firebaseUser.getUid())) {
                                                uidstore = orderArrayList.get(j).getStore().getTokenstore();
                                            }
                                        }
                                    }
                                    double tongngay=0;
                                    if ( hdct.isCheck() == true && uidstore.equalsIgnoreCase(firebaseUser.getUid())) {
                                        orderArrayList3.addAll(hdct.getOrderArrayList());
                                        for (Order order : orderArrayList3) {
                                            tongngay += order.getSoluongmua() * order.getFood().getGia();

                                        }
                                        ArrayList<BarEntry> barEntries = new ArrayList<>();
                                        barEntries.add(new BarEntry(0, (float) tongngay));
                                        BarDataSet barDataSet = new BarDataSet(barEntries, "Ngày");
                                        barDataSet.setColor(Color.RED);
                                        BarData barData = new BarData();
                                        barData.setBarWidth(0.20f);
                                        barData.addDataSet(barDataSet);
                                        thongkengay.setFitBars(true);
                                        thongkengay.getDescription().setText("Food App");
                                        thongkengay.setData(barData);
                                        thongkengay.animateY(2000);
                                        thongkengay.invalidate();
                                    }

                                }

                            }

                            @Override
                            public void onCancelled(@NonNull DatabaseError error) {

                            }
                        });

                    }
                },calendar.get(Calendar.YEAR),calendar.get(Calendar.MONTH),calendar.get(Calendar.DAY_OF_MONTH));

                datePickerDialog.show();
            }
        });
        DatabaseReference databaseReference = FirebaseDatabase.getInstance().getReference("HDCT");
        arrayList = new ArrayList<>();
        databaseReference.addListenerForSingleValueEvent(new ValueEventListener() {

            ArrayList<Order> listOrder = new ArrayList<>();
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                arrayList.clear();
                String uidstore = "";

                ArrayList<Order> orderArrayList = new ArrayList<>();
                for (DataSnapshot dataSnapshot : snapshot.getChildren()) {
                    HDCT hdct = dataSnapshot.getValue(HDCT.class);
                    arrayList.add(hdct);
                    String ngaytk = hdct.getNgay().substring(0, 2);
                    for (int i = 0; i < arrayList.size(); i++) {
                        if (arrayList.get(i).isCheck()) {
                            orderArrayList.addAll(arrayList.get(i).getOrderArrayList());
                        }
                        Log.d("VH11", "onDataChange: " + orderArrayList.toString());
                        for (int j = 0; j < orderArrayList.size(); j++) {
                            if (orderArrayList.get(j).getFood().getTokenstore().equalsIgnoreCase(firebaseUser.getUid())) {
                                listOrder.add(orderArrayList.get(j));
                            }
                        }
                    }
                    Log.d("VH11", "onDataChange: " + listOrder.size()  + listOrder.toString());
                    int tongngay=0;
                    if (ngay.matches(ngaytk) && hdct.isCheck()) {
                        for (Order order : listOrder) {
                            tongngay += order.getSoluongmua() * order.getFood().getGia();
                        }
                        Log.e("VH111", "onDataChange: " + tongngay );
                        ArrayList<BarEntry> barEntries = new ArrayList<>();
                        barEntries.add(new BarEntry(0, tongngay));
                        BarDataSet barDataSet = new BarDataSet(barEntries, "Ngày");
                        barDataSet.setColor(Color.RED);
                        BarData barData = new BarData();
                        barData.setBarWidth(0.20f);
                        barData.addDataSet(barDataSet);
                        thongkengay.setFitBars(true);
                        thongkengay.getDescription().setText("Food App");
                        thongkengay.setData(barData);
                        thongkengay.animateY(2000);
                        thongkengay.invalidate();
                    }
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
        return view;
    }

}
