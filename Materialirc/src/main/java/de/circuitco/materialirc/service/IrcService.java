package de.circuitco.materialirc.service;

import android.app.Service;
import android.content.ComponentName;
import android.content.Intent;
import android.content.ServiceConnection;
import android.os.Binder;
import android.os.IBinder;
import android.support.annotation.Nullable;

import com.google.common.collect.ImmutableList;
import com.google.common.collect.ImmutableSortedSet;

import org.pircbotx.MultiBotManager;
import org.pircbotx.PircBotX;

import java.util.ArrayList;
import java.util.List;

import de.circuitco.materialirc.config.UserConfig;
import de.circuitco.materialirc.config.UserConfigDb;
import de.circuitco.materialirc.connection.IrcConnectionManager;

/**
 * ircservice aims
 *
 * - spawn lots of connections and keep them around
 * - handle notifications
 * - handle channel states - just the basics (ban list,
 * -
 *
 */
public class IrcService extends Service {
    private final LocalBinder binder = new LocalBinder();
    private UserConfigDb db;
    private MultiBotManager manager = new MultiBotManager();

    public ImmutableSortedSet<PircBotX> getConnectedBots() {
        return manager.getBots();
    }

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
        db = new UserConfigDb(this);
        List<UserConfig> config = db.getConfig();

        // Connect all the connections
        connectBots(config);
    }

    private void connectBots(List<UserConfig> config) {
        for (UserConfig c : config) {
            manager.addBot(c.toConfig());
        }
        manager.start();
    }
}
