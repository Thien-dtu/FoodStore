package com.shin.foodstore.Adapter;

import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.RecyclerView;

import com.google.gson.Gson;
import com.shin.foodstore.Model.Order;
import com.shin.foodstore.R;
import com.squareup.picasso.Callback;
import com.squareup.picasso.Picasso;

import java.text.DecimalFormat;
import java.text.NumberFormat;
import java.util.ArrayList;
import java.util.Locale;

public class CartHDCTAdapter extends RecyclerView.Adapter<CartHDCTAdapter.MyViewHolder> {

    ArrayList<Order> cartList;
    Context context;

    String _subtotal, _price, _quantity;

    Gson gson;

    public CartHDCTAdapter(ArrayList<Order> cartList, Context context) {
        this.cartList = cartList;
        this.context = context;
    }


    @NonNull
    @Override
    public MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int i) {
        View itemView;

        itemView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.row_carthdct, parent, false);


        return new MyViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(@NonNull final MyViewHolder holder, final int position) {
        final DecimalFormat decimalFormat = (DecimalFormat) NumberFormat.getInstance(Locale.US);
        decimalFormat.applyPattern("#,###,###,###");
        final Order cart = cartList.get(position);

        gson = new Gson();
        holder.title.setText(cart.getFood().getNamefood());
        holder.attribute.setText(cart.getFood().getMatheloai());

        holder.quantity.setText(String.valueOf(decimalFormat.format(cart.getSoluongmua())));
        holder.price.setText(String.valueOf(decimalFormat.format(cart.getFood().getGia())));

        _subtotal = String.valueOf(decimalFormat.format(cart.getSoluongmua() * cart.getFood().getGia()));

        holder.subTotal.setText(_subtotal);
        Picasso.get()
                .load(cart.getFood().getImage())
                .into(holder.imageView, new Callback() {
                    @Override
                    public void onSuccess() {
                        holder.progressBar.setVisibility(View.GONE);
                    }

                    @Override
                    public void onError(Exception e) {
                        Log.d("Error : ", e.getMessage());
                    }
                });
    }

    @Override
    public int getItemCount() {

        return cartList.size();
    }

    public class MyViewHolder extends RecyclerView.ViewHolder {
        ImageView imageView;
        TextView title;
        ProgressBar progressBar;
        CardView cardView;
        TextView offer, currency, price, quantity, attribute, addToCart, subTotal;
        TextView plus, minus;
        Button delete;

        public MyViewHolder(@NonNull View itemView) {
            super(itemView);

            imageView = itemView.findViewById(R.id.product_image);
            title = itemView.findViewById(R.id.product_title);
            progressBar = itemView.findViewById(R.id.progressbar);
            quantity = itemView.findViewById(R.id.quantity);
            currency = itemView.findViewById(R.id.product_currency);
            attribute = itemView.findViewById(R.id.product_attribute);
//            plus = itemView.findViewById(R.id.quantity_plus);
            minus = itemView.findViewById(R.id.quantity_minus);
            subTotal = itemView.findViewById(R.id.sub_total);
            price = itemView.findViewById(R.id.product_price);
        }
    }
}
