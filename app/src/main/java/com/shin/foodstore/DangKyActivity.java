package com.shin.foodstore;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
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
    private DatabaseStore databaseStore;
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
                final String email = emailsignup.getText().toString().trim();
                String pass = passsignup.getText().toString().trim();
                String nhappass = nhaplaipass.getText().toString().trim();
                if (!email.matches("^[a-zA-Z][a-z0-9_\\.]{4,32}@[a-z0-9]{2,}(\\.[a-z0-9]{2,4}){1,2}$")) {
                    emailsignup.setError("Email không hợp lệ.");
                    return;
                }
                if (TextUtils.isEmpty(pass)) {
                    passsignup.setError("Bắt buộc");
                    return;
                }
                if (TextUtils.isEmpty(email)) {
                    emailsignup.setError("Bắt buộc");
                    return;
                }
                if (TextUtils.isEmpty(nhappass)) {
                    nhaplaipass.setError("Bắt buộc");
                    return;
                }
                if (pass.length() < 6) {
                    passsignup.setError("Mật khẩu phải lớn hơn 6 ký tự");
                    return;
                }
                if (!pass.equals(nhappass)) {
                    nhaplaipass.setError("Mật khẩu không khớp");
                }
                progressBar.setVisibility(View.VISIBLE);
                mAuth.createUserWithEmailAndPassword(email, pass)
                        .addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                            @Override
                            public void onComplete(@NonNull Task<AuthResult> task) {
                                progressBar.setVisibility(View.GONE);
                                if (!task.isSuccessful()) {
//                                    Toast.makeText(getApplicationContext(), "Đăng ký Thất Bại.",
//                                            Toast.LENGTH_SHORT).show();
                                } else {
                                    databaseStore = new DatabaseStore(getApplicationContext());
                                    Store store = new Store(email, pass, null, null, null, null, null, mAuth.getUid());
                                    databaseStore.insert(store);
//                                    Toast.makeText(getApplicationContext(), "Đăng Ký Thành Công.", Toast.LENGTH_SHORT).show();
                                    startActivity(new Intent(getApplicationContext(), DangNhapActivity.class));
                                    finish();
                                }
                            }
                        });

                    }
        });
    }
}