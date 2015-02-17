package com.eveningoutpost.dexdrip.UtilityModels;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;
import android.util.Log;

import com.eveningoutpost.dexdrip.Services.DexCollectionService;
import com.eveningoutpost.dexdrip.Services.WixelReader;

import java.util.HashSet;
import java.util.Set;

/**
 * Created by stephenblack on 12/22/14.
 */
public class CollectionServiceStarter {
    private Context mContext;

    private static final String TAG = "CollectionStarter";

    private CollectionServiceStarter() {}

    public static boolean isBTWixel(Context context) {
        SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences(context);
        String collection_method = prefs.getString("dex_collection_method", "BluetoothWixel");
        if(collection_method.compareTo("BluetoothWixel") == 0) {
            return true;
        }
        return false;
    }

    /**
     * Used to determine if the source(s) has changed.
     */
    private static HashSet<String> lastCalculated = null;

    public static synchronized void start(Context context) {
        SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences(context);
        String collection_method = prefs.getString("dex_collection_method", "BluetoothWixel");

        HashSet<String> methods = new HashSet<>();
        methods.add(collection_method);

        if (setsEqual(lastCalculated, methods)) {
            Log.i(TAG, "Same source(s), skipping action.");
            return;
        }

        if (isBTWixel(context)) {
            stopWifWixelThread(context);
            startBtWixelService(context);
        } else {
            stopBtWixelService(context);
            startWifWixelThread(context);
        }

        Log.d(TAG, collection_method);

        lastCalculated = methods;
    }

    private static <T> boolean setsEqual(Set<T> a, Set<T> b) {
        if (a == null || b == null)
            return false;

        if (a.size() != b.size())
            return false;

        for (T it : a) {
            if (!b.contains(it))
                return false;
        }

        return true;
    }

    private static void startBtWixelService(Context context) {
        if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.JELLY_BEAN_MR2) {
            context.startService(new Intent(context, DexCollectionService.class));
        }
    }

    private static void stopBtWixelService(Context context) {
        if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.JELLY_BEAN_MR2) {
            context.stopService(new Intent(context, DexCollectionService.class));
        }
    }

    private static void startWifWixelThread(Context context) {
        WixelReader.sStart(context);
    }

    private static void stopWifWixelThread(Context context) {
        WixelReader.sStop();
    }

}
