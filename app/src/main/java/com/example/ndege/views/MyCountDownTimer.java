package com.example.ndege.views;

import android.os.CountDownTimer;

import com.example.ndege.login.otp.OtpActivity;

/**
 * Created by Jerry on 10/31/2017.
 * This is CountDownTimer sub class, which will override it's abstract methods.
 */

public class MyCountDownTimer extends CountDownTimer {



    private OtpActivity otpActivity;

    public void setSourceActivity(OtpActivity otpActivity) {
        this.otpActivity = otpActivity;
    }

    public MyCountDownTimer(long millisInFuture, long countDownInterval) {
        super(millisInFuture, countDownInterval);
    }

    @Override
    public void onTick(long millisUntilFinished) {
         if (this.otpActivity!=null){
            this.otpActivity.onCountDownTimerTickEvent(millisUntilFinished);
        }
    }

    @Override
    public void onFinish() {
        if (this.otpActivity!=null){
            this.otpActivity.onCountDownTimerFinishEvent();
        }
    }
}