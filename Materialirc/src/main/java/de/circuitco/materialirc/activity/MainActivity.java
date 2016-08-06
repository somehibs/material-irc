package de.circuitco.materialirc.activity;

import android.os.Bundle;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.widget.RecyclerView;
import android.view.View;

import com.google.common.collect.ImmutableSortedSet;

import org.pircbotx.PircBotX;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.SortedSet;
import java.util.TreeSet;

import de.circuitco.materialirc.R;
import de.circuitco.materialirc.activity.ServiceActivity;
import de.circuitco.materialirc.activity.UserConfigActivity;
import de.circuitco.materialirc.adapters.ConnectionAdapter;
import de.circuitco.materialirc.config.UserConfigDb;
import de.circuitco.materialirc.ui.Drawer;

public class MainActivity extends ServiceActivity {

    UserConfigDb configDb = null;
    boolean configured = false;
    List<String> connectionNames = new ArrayList<>();
    List<String> rooms = new ArrayList<>();
    private ConnectionAdapter connectionAdapter = null;
    private RecyclerView connectionView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        if ( configDb == null ) {
            configDb = new UserConfigDb(this);
        }

        new Drawer((DrawerLayout) findViewById(R.id.drawer));
    }

    @Override
    protected void onResume() {
        super.onResume();
        // Do we have any configs?
        if ( !configDb.hasConfig() ) {
            // Show blank config activity.
            // If you're a vendor, you would want to use UserConfigActivity to provide a system
            // config which would let us show the user some ui.
            startActivity(UserConfigActivity.get(this, null, true));
        }
    }

    @Override
    public void onService() {
        if ( configDb.hasConfig() ) {
            // Interrogate the service for the names of all the connections
            ImmutableSortedSet<PircBotX> bots = irc.getConnectedBots();

            for ( PircBotX bot : bots ) {
                if (!connectionNames.contains(bot.getNick())) {
                    connectionNames.add(bot.getNick());
                }
            }
            Collections.sort(connectionNames);
            if ( connectionAdapter == null ) {
                connectionAdapter = new ConnectionAdapter(connectionNames);
            } else {
                connectionAdapter.addAll(connectionNames);
            }

            connectionView = (RecyclerView) findViewById(R.id.recycler_view);
            connectionView.setAdapter(connectionAdapter);

            // Register for interesting updates from the service

            // Show all the services

        }
    }

    @Override
    public int getLayoutId() {
        return R.layout.activity_main;
    }

    @Override
    public int getMenuId() {
        return R.menu.main_activity;
    }
}
