package com.example.ndege.firebase_push;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;

import com.example.ndege.units.corecategories.ViewCoreCategories;


public class CallBroadcastReceiver extends BroadcastReceiver {

    @Override
    public void onReceive(Context context, Intent intent) {
        Intent i = new Intent();
        i.setClassName(ViewCoreCategories.class.getPackage().getName(), ViewCoreCategories.class.getName());
        i.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        context.startActivity(i);
    }


}