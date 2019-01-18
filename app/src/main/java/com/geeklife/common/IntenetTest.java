package com.geeklife.common;

import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;

public class IntenetTest {

    public static boolean testConnection( Context context ) {
        int[] networkTypes = {ConnectivityManager.TYPE_MOBILE,
                ConnectivityManager.TYPE_WIFI};
        try {
            ConnectivityManager connectivityManager =
                    ( ConnectivityManager ) context.getSystemService( Context.CONNECTIVITY_SERVICE );
            for ( int networkType : networkTypes ) {
                NetworkInfo activeNetworkInfo = connectivityManager.getActiveNetworkInfo();
                if ( activeNetworkInfo != null &&
                        activeNetworkInfo.getType() == networkType )
                    return true;
            }
        } catch ( Exception e ) {
            return false;
        }
        return false;
    }

    public void InternetTest() {


    }
}

