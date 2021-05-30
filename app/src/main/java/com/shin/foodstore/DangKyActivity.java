package com.shin.foodstore;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.shin.foodstore.CallBack.Storecallback;
import com.shin.foodstore.Database.DatabaseStore;
import com.shin.foodstore.Model.Store;

import java.util.ArrayList;

public class DangKyActivity extends AppCompatActivity {
    EditText emailsignup, passsignup, nhaplaipass;
    Button btnsignup;
    DatabaseStore databaseStore;
    ArrayList<Store> datastore;
    private FirebaseAuth mAuth;
    FirebaseDatabase database;
    DatabaseReference databaseReference;
    private ProgressBar progressBar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_dang_ky);

        emailsignup = findViewById(R.id.emailsignup);
        passsignup = findViewById(R.id.passsignup);
        nhaplaipass = findViewById(R.id.nhaplaipass);
        btnsignup = findViewById(R.id.signup);
        database =FirebaseDatabase.getInstance();
        databaseReference = database.getReference("Store");
        mAuth = FirebaseAuth.getInstance();
        progressBar = findViewById(R.id.profile_progressBar);
        btnsignup.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String email = emailsignup.getText().toString().trim();
                String pass = passsignup.getText().toString().trim();
                String nhappass = nhaplaipass.getText().toString().trim();
                if(!email.matches("^[a-zA-Z][a-z0-9_\\.]{4,32}@[a-z0-9]{2,}(\\.[a-z0-9]{2,4}){1,2}$")){
                    Toast.makeText(getApplicationContext(), "Email Không Hợp Lệ", Toast.LENGTH_SHORT).show();
                }else {
                    if (email.isEmpty() || pass.isEmpty() || nhappass.isEmpty()){
                        Toast.makeText(getApplicationContext(), "Vui lòng nhập đầy đủ các trường", Toast.LENGTH_SHORT).show();
                    }else if (pass.length()<6){ Toast.makeText(getApplicationContext(), "Mật khẩu phải có ít nhất 6 ký tự!",
                            Toast.LENGTH_SHORT).show();}
                    else if (!(pass.matches(nhappass))){
                        nhaplaipass.setError("Mật Khẩu Không Trùng Khớp");
                    }else {
                        databaseStore = new DatabaseStore(getApplicationContext());
                        datastore = new ArrayList<>();
                        databaseStore.getAll(new Storecallback() {
                            @Override
                            public void onSuccess(ArrayList<Store> lists) {
                                datastore.clear();
                                datastore.addAll(lists);
                            }

                            @Override
                            public void onError(String message) {

                            }
                        });
                        progressBar.setVisibility(View.VISIBLE);
                        //create user
                        mAuth.createUserWithEmailAndPassword(email,pass)
                                .addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                                    @Override
                                    public void onComplete(@NonNull Task<AuthResult> task) {
                                        progressBar.setVisibility(View.GONE);
                                        if (task.isSuccessful()) {
                                            for (int i = 0; i < datastore.size(); i++) {
                                                if (datastore.get(i).getEmail().equalsIgnoreCase(email.toString()) && datastore.get(i).getPass().equalsIgnoreCase(pass.toString())) {
                                                    Toast.makeText(getApplicationContext(), "Đăng Ký Thành Công", Toast.LENGTH_SHORT).show();
                                                    Intent is = new Intent(getApplicationContext(), MainActivity.class);
                                                    startActivity(is);
                                                    break;
                                                } else {
                                                    Toast.makeText(getApplicationContext(), "Login Thất Bại", Toast.LENGTH_SHORT).show();
                                                }
                                            }
                                        } else {
                                            Toast.makeText(getApplicationContext(), "Login Thất Bại", Toast.LENGTH_SHORT).show();
                                        }
                                    }
                                });
                    }
                }

            }
        });
    }
}