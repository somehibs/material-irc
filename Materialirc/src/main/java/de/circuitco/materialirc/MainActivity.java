package de.circuitco.materialirc;

import de.circuitco.materialirc.activity.ServiceActivity;
import de.circuitco.materialirc.activity.UserConfigActivity;
import de.circuitco.materialirc.config.UserConfig;
import de.circuitco.materialirc.config.UserConfigDb;

public class MainActivity extends ServiceActivity {

    UserConfigDb database = null;

    @Override
    public void onService() {
        if ( database == null ) {
            database = new UserConfigDb(this);
        }
        // Do we have any configs?
        if ( database.hasConfig() ) {
            // They're experienced, show some interesting ui
        } else {
            // Show blank config activity.
            // If you're a vendor, you would want to use UserConfigActivity to provide a system
            // config which would let us show the user some ui.
            //
            // if you toggle advanced on when
            startActivity(UserConfigActivity.get(this, null, true));
        }
    }

    @Override
    public int getLayoutId() {
        return R.layout.activity_main;
    }
}
