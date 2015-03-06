package com.fenlisproject.elf.core.net;

import android.content.Context;
import android.net.ConnectivityManager;

public class NetworkConnectionInfo {

    private ConnectivityManager mConnectivityManager;

    private NetworkConnectionInfo(Context context) {
        mConnectivityManager = (ConnectivityManager)
                context.getApplicationContext().getSystemService(Context.CONNECTIVITY_SERVICE);
    }

    public static NetworkConnectionInfo from(Context context) {
        return new NetworkConnectionInfo(context);
    }

    public boolean isConnected() {
        if (mConnectivityManager.getActiveNetworkInfo() == null) {
            return false;
        } else {
            return true;
        }
    }
}
