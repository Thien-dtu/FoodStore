package com.shin.foodstore;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.util.DisplayMetrics;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.FrameLayout;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.bottomsheet.BottomSheetBehavior;
import com.google.android.material.bottomsheet.BottomSheetDialog;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.shin.foodstore.CallBack.Storecallback;
import com.shin.foodstore.Database.DatabaseStore;
import com.shin.foodstore.Model.Store;

import java.util.ArrayList;

public class DangNhapActivity extends AppCompatActivity implements FirebaseAuth.AuthStateListener {
    Button mloginBtn,btnLoginPhone,btnLoginGoogle,btnLoginFacebok;
    TextView btndangky,btnquenmatkhau;
    EditText email,pass;
    ProgressBar progressBar;
    DatabaseStore databaseStore;
    ArrayList<Store> datastore;
    private FirebaseAuth mAuth;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_dang_nhap);

        btndangky = findViewById(R.id.btndangky);
        email = findViewById(R.id.Email);
        pass = findViewById(R.id.password);
        mloginBtn = findViewById(R.id.loginBtn);
        btnLoginPhone = findViewById(R.id.btnLoginPhone);
        btnquenmatkhau = findViewById(R.id.btnquenmatkhau);
        btnLoginGoogle = findViewById(R.id.btnLoginGoogle);
        btnLoginFacebok = findViewById(R.id.btnLoginFacebook);
        progressBar = findViewById(R.id.progressBar);
        mAuth = FirebaseAuth.getInstance();

        mloginBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                login();
            }
        });

        btndangky.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(getApplicationContext(),DangKyActivity.class);
                startActivity(i);
            }
        });
        btnquenmatkhau.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Toast.makeText(getApplicationContext(),"Quên mật khẩu kệ mẹ mày, đéo thân! :v", Toast.LENGTH_SHORT).show();
            }
        });
        btnLoginPhone.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Toast.makeText(getApplicationContext(),"Không đăng nhập như này được!", Toast.LENGTH_SHORT).show();
            }
        });
        btnLoginGoogle.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Toast.makeText(getApplicationContext(),"Không đăng nhập như này được!", Toast.LENGTH_SHORT).show();
            }
        });
        btnLoginFacebok.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Toast.makeText(getApplicationContext(),"Không đăng nhập như này được!", Toast.LENGTH_SHORT).show();
            }
        });

    }

    private void login() {
        final String ussername = email.getText().toString().trim();
        final String pass1 = pass.getText().toString().trim();
        if (ussername.isEmpty() || pass1.isEmpty()) {
            email.setError("Bắt buộc");
            pass.setError("Bắt buộc");
            Toast.makeText(getApplicationContext(), "Vui Lòng Nhập Đầy Đủ 2 Trường", Toast.LENGTH_SHORT).show();
            return;
        } else if (pass1.length() < 6) {
            pass.setError("Mật khẩu phải lớn hơn 6 ký tự");
            return;
        } else if (!ussername.matches("^[a-zA-Z][a-z0-9_\\.]{4,32}@[a-z0-9]{2,}(\\.[a-z0-9]{2,4}){1,2}$")) {
            Toast.makeText(getApplicationContext(), "Email Không Hợp Lệ", Toast.LENGTH_SHORT).show();
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
            mAuth.signInWithEmailAndPassword(ussername,pass1)
                    .addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                        @Override
                        public void onComplete(@NonNull Task<AuthResult> task) {
                            if (task.isSuccessful()) {
                                for (int i = 0; i < datastore.size(); i++) {
                                    if (datastore.get(i).getEmail().equalsIgnoreCase(email.getText().toString()) && datastore.get(i).getPass().equalsIgnoreCase(pass.getText().toString())) {
                                        Toast.makeText(getApplicationContext(), "Login Thành Công", Toast.LENGTH_SHORT).show();
                                        Intent is = new Intent(getApplicationContext(), MainActivity.class);
                                        is.putExtra("email", email.getText().toString());
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
    private void setupRatio(BottomSheetDialog bottomSheetDialog) {
        FrameLayout bottomSheet = (FrameLayout)
                bottomSheetDialog.findViewById(R.id.design_bottom_sheet);
        BottomSheetBehavior behavior = BottomSheetBehavior.from(bottomSheet);
        ViewGroup.LayoutParams layoutParams = bottomSheet.getLayoutParams();
        layoutParams.height = getBottomSheetDialogDefaultHeight();
        bottomSheet.setLayoutParams(layoutParams);
        behavior.setState(BottomSheetBehavior.STATE_EXPANDED);
    }
    private int getBottomSheetDialogDefaultHeight() {
        return getWindowHeight() * 85 / 100;
    }
    private int getWindowHeight() {
        // Calculate window height for fullscreen use
        DisplayMetrics displayMetrics = new DisplayMetrics();
        ((Activity) getApplicationContext()).getWindowManager().getDefaultDisplay().getMetrics(displayMetrics);
        return displayMetrics.heightPixels;
    }

    @Override
    protected void onStart() {
        super.onStart();
        mAuth.addAuthStateListener(this);
    }

    @Override
    protected void onStop() {
        super.onStop();
        mAuth.removeAuthStateListener(this);
    }

    @Override
    public void onAuthStateChanged(@NonNull FirebaseAuth firebaseAuth) {
        FirebaseUser user = firebaseAuth.getCurrentUser();
        if(user !=null){
            Intent intent = new Intent(DangNhapActivity.this, MainActivity.class);
            startActivity(intent);
        };
    }
}
