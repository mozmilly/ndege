package com.example.ndege.login.otp;


import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.example.ndege.R;
import com.example.ndege.login.SignUpActivity;
import com.example.ndege.units.corecategories.ViewCoreCategories;
import com.example.ndege.views.MyCountDownTimer;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.android.gms.tasks.TaskExecutors;
import com.google.firebase.FirebaseException;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.PhoneAuthCredential;
import com.google.firebase.auth.PhoneAuthProvider;

import java.util.concurrent.TimeUnit;

public class OtpActivity extends AppCompatActivity {

    private String verificationid;
    private FirebaseAuth mAuth;
    private ProgressBar progressBar;
    private EditText editText;
    private TextView textView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_otp);

        textView = findViewById(R.id.tv_coundown);

        mAuth = FirebaseAuth.getInstance();

        progressBar = findViewById(R.id.progressbar);
        editText = findViewById(R.id.editTextCode);

        String phonenumber = getIntent().getStringExtra("phonenumber");
        sendVerificationCode(phonenumber);

        findViewById(R.id.buttonSignIn).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                String code = editText.getText().toString().trim();

                if ((code.isEmpty() || code.length() < 6)){

                    editText.setError("Enter code...");
                    editText.requestFocus();
                    return;
                }
                verifyCode(code);

            }
        });
        // Create a count down timer which will count 60 seconds and invoke the timer object onTick() method every second.
        final MyCountDownTimer myCountDownTimer = new MyCountDownTimer(60*1000, 1000);
        // Set count down timer source activity.
        myCountDownTimer.setSourceActivity(this);


        myCountDownTimer.start();
    }

    private void verifyCode(String code){
        PhoneAuthCredential credential = PhoneAuthProvider.getCredential(verificationid, code);
        signInWithCredential(credential);

    }

    private void signInWithCredential(PhoneAuthCredential credential) {
        mAuth.signInWithCredential(credential)
                .addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if (task.isSuccessful()) {
                            SharedPreferences sp=getSharedPreferences("pref",0);
                            SharedPreferences.Editor edit= sp.edit();
                            edit.putInt("PREFERENCES_LOGIN", 1);
                            edit.putString("user", getIntent().getStringExtra("pNumber"));
                            edit.apply();
                            if (getIntent().getStringExtra("intent").equals("login")){
                                Intent intent = new Intent(OtpActivity.this, ViewCoreCategories.class);

                                startActivity(intent);
                            } else {
                                Intent intent = new Intent(OtpActivity.this, SignUpActivity.class);
                                intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                                intent.putExtra("phonenumber", getIntent().getStringExtra("phonenumber"));
                                intent.putExtra("pNumber", getIntent().getStringExtra("pNumber"));

                                startActivity(intent);
                            }



                        } else {
                            Toast.makeText(OtpActivity.this, task.getException().getMessage(), Toast.LENGTH_LONG).show();
                        }
                    }

                });
    }

    private void sendVerificationCode(String number){

        PhoneAuthProvider.getInstance().verifyPhoneNumber(
                number,
                60,
                TimeUnit.SECONDS,
                TaskExecutors.MAIN_THREAD,
                mCallBack
        );
    }

    private PhoneAuthProvider.OnVerificationStateChangedCallbacks
            mCallBack = new PhoneAuthProvider.OnVerificationStateChangedCallbacks() {

        @Override
        public void onCodeSent(String s, PhoneAuthProvider.ForceResendingToken forceResendingToken) {
            super.onCodeSent(s, forceResendingToken);
            verificationid = s;
            Toast.makeText(OtpActivity.this, "Code Sent", Toast.LENGTH_SHORT).show();
        }

        @Override
        public void onVerificationCompleted(PhoneAuthCredential phoneAuthCredential) {
            String code = phoneAuthCredential.getSmsCode();
            if (code != null){
                progressBar.setVisibility(View.VISIBLE);
                verifyCode(code);
                Toast.makeText(OtpActivity.this, "Verification complete", Toast.LENGTH_SHORT).show();
            }
        }

        @Override
        public void onVerificationFailed(FirebaseException e) {
            Toast.makeText(OtpActivity.this, e.getMessage(),Toast.LENGTH_LONG).show();

        }
    };

    /* This method will be invoked when CountDownTimer finish. */
    public void onCountDownTimerFinishEvent()
    {
        this.textView.setEnabled(true);
    }

    /* This method will be invoked when CountDownTimer tick event happened.*/
    public void onCountDownTimerTickEvent(long millisUntilFinished)
    {
        // Calculate left seconds.
        long leftSeconds = millisUntilFinished / 1000;

        String sendButtonText = leftSeconds + " (s)";

        if(leftSeconds==0)
        {
            sendButtonText = "refresh";
        }

        // Show left seconds in send button.
        this.textView.setText(sendButtonText);
    }

}
