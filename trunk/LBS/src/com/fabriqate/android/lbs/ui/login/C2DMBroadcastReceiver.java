package com.fabriqate.android.lbs.ui.login;

import android.app.Activity;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.util.Log;

/**
 * Helper class to handle BroadcastReciver behavior.
 * - can only run for a limited amount of time - it must start a real service 
 * for longer activity
 * - must get the power lock, must make sure it's released when all done.
 * 
 */
/**
 * 帮助类，帮忙处理BroadcastReciver过程
 * */
public class C2DMBroadcastReceiver extends BroadcastReceiver {
    
    @Override
    public final void onReceive(Context context, Intent intent) {
        // To keep things in one place.
        C2DMBaseReceiver.runIntentInService(context, intent);
        setResult(Activity.RESULT_OK, null /* data */, null /* extra */);        
    }
}

