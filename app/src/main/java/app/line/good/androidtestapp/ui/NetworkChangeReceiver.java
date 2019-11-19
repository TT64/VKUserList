package app.line.good.androidtestapp.ui;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;

public class NetworkChangeReceiver extends BroadcastReceiver {
    private boolean isConnected = false;
    public ReceiverListener receiverListener;

    @Override
    public void onReceive(Context context, Intent intent) {
        try {
            isConnected = isOnline(context);
        } catch (NullPointerException e) {
            e.printStackTrace();
        }
        receiverListener.onNetworkConnectionChanged(isConnected);
    }

    public boolean isOnline(Context context) {
        try {
            ConnectivityManager cm = (ConnectivityManager) context.getApplicationContext().getSystemService(Context.CONNECTIVITY_SERVICE);
            NetworkInfo netInfo = null;
            if (cm != null) {
                netInfo = cm.getActiveNetworkInfo();
            }
            return (netInfo != null && netInfo.isConnected());
        } catch (NullPointerException e) {
            e.printStackTrace();
            return false;
        }
    }

    public interface ReceiverListener {
        void onNetworkConnectionChanged(boolean isConnected);
    }
}
