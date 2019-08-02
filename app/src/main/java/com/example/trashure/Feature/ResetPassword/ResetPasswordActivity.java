package com.example.trashure.Feature.ResetPassword;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.example.trashure.Feature.Login.LoginActivity;
import com.example.trashure.R;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;

public class ResetPasswordActivity extends AppCompatActivity {

    FirebaseAuth mAuth;
    EditText etEmail;
    Button btnKirimEmail;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_reset_password);
        initialize();

    }

    public void initialize()
    {
        etEmail = (EditText) findViewById(R.id.et_send_email);
        btnKirimEmail = (Button) findViewById(R.id.btn_kirim_email);
        mAuth = FirebaseAuth.getInstance();
        btnKirimEmail.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                resetPasswordUser();

            }
        });
    }

    private void resetPasswordUser() {
        String email = etEmail.getText().toString();
        if(TextUtils.isEmpty(email))
        {
            Toast.makeText(this, "Email Tidak Boleh Kosong", Toast.LENGTH_SHORT).show();
        }
        else
        {
            mAuth.sendPasswordResetEmail(email).addOnCompleteListener(new OnCompleteListener<Void>() {
                @Override
                public void onComplete(@NonNull Task<Void> task) {
                    if(task.isSuccessful())
                    {
                        Toast.makeText(ResetPasswordActivity.this, "Proses Reset sudah dikirim ke email anda", Toast.LENGTH_SHORT).show();
                        Intent loginIntent = new Intent(ResetPasswordActivity.this, LoginActivity.class);
                        startActivity(loginIntent);
                        finish();
                    }
                    else
                    {
                        Toast.makeText(ResetPasswordActivity.this, "Error : " + task.getException().getMessage(), Toast.LENGTH_SHORT).show();
                    }
                }
            });
        }
    }

}
