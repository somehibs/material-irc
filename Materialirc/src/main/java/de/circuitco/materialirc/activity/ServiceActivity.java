package de.circuitco.materialirc.activity;

import android.content.ComponentName;
import android.content.Intent;
import android.content.ServiceConnection;
import android.os.Bundle;
import android.os.IBinder;
import android.support.v7.app.AppCompatActivity;

import de.circuitco.materialirc.service.IrcService;

/**
 * Created by alex on 17/04/16.
 */
public abstract class ServiceActivity extends UiActivity {
    protected IrcService irc;
    protected ServiceActivityConnection serviceBinding;

    // Stuff you'll always want

    /**
     * onService is called whenever both of the following are true
     * - the ui is unpaused
     * - the service is alive.
     *
     * this means switching apps, and switching back will cause another onService call
     */
    public abstract void onService();

    // stuff you might want
    //nothing

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        serviceBinding = new ServiceActivityConnection();
        bindService(new Intent(this, IrcService.class), serviceBinding, BIND_AUTO_CREATE);
        super.onCreate(savedInstanceState);
    }

    @Override
    protected void onResume() {
        super.onResume();
        if ( serviceBinding.isConnected ) {
            onService();
        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        unbindService(serviceBinding);
    }

    public class ServiceActivityConnection implements ServiceConnection {
        public boolean isConnected = false;

        @Override
        public void onServiceConnected(ComponentName name, IBinder service) {
            IrcService.LocalBinder binder = ((IrcService.LocalBinder)service);
            irc = binder.getService();
            isConnected = true;
            if ( !paused ) {
                onService();
            }
        }

        @Override
        public void onServiceDisconnected(ComponentName name) {
            isConnected = false;
            irc = null;
        }
    }
}
