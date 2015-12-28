package com.fabriqate.android.lbs.service;

import com.fabriqate.android.lbs.C2DMReceiver;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;

public class MissConnedctionReceiver extends BroadcastReceiver {
    private static final String ACTION = "android.intent.action.BOOT_COMPLETED";
        
    public void onReceive(Context context, Intent intent){
        if (intent.getAction().equals(ACTION)){
            context.startService(new Intent(context,  MissConnectionService.class));
           
        }
    }
}
