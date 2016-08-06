package de.circuitco.materialirc.service;

import android.app.Service;
import android.content.ComponentName;
import android.content.Intent;
import android.content.ServiceConnection;
import android.os.Binder;
import android.os.IBinder;
import android.support.annotation.Nullable;

/**
 * Created by alex on 17/04/16.
 */
public class IrcService extends Service {
    private final LocalBinder binder = new LocalBinder();
    public class LocalBinder extends Binder {
        public IrcService getService() {
            // Return this instance of LocalService so clients can call public methods
            return IrcService.this;
        }
    }

    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        return binder;
    }

    @Override
    public void onCreate() {
        super.onCreate();

        // Load all available services from config.

        // Create all the necessary bot wrappers and add them to a connection map.

        // Connect all the connections using the bot wrappers
    }
}
