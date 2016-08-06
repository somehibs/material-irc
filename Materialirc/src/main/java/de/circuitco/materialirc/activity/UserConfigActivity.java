package de.circuitco.materialirc.activity;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.util.Pair;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.List;

import de.circuitco.materialirc.R;
import de.circuitco.materialirc.adapters.UserConfigAdapter;
import de.circuitco.materialirc.config.UserConfig;
import de.circuitco.materialirc.config.UserConfigDb;

/**
 * Created by alex on 12/06/16.
 */
public class UserConfigActivity extends UiActivity {
    private static final String LOG_TAG = "UserConfigActivity";

    private String name = null;
    private boolean advanced = false;

    private UserConfig configuration = null;
    //private ScrollView scrollView = null;
    private RecyclerView recyclerView = null;
    private UserConfigAdapter configAdapter;

    private UserConfigDb database;
    private List<Pair<String, String>> configValues;

    public static Intent get(Activity ctx, String existingConfig, boolean advancedSettings) {
        Intent intent = new Intent(ctx, UserConfigActivity.class);
        if ( existingConfig != null ) {
            intent.putExtra("name", existingConfig);
        }
        intent.putExtra("advanced", advancedSettings);
        return intent;
    }

    public static Intent get(Activity ctx, UserConfig serialisedConfig) {
        Intent intent = new Intent(ctx, UserConfigActivity.class);
        intent.putExtra("existing_config", serialisedConfig);
        return intent;
    }

    @Override
    public int getLayoutId() {
        return R.layout.activity_user_config;
    }

    // Lifecycle methods
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        getSupportActionBar().setTitle(R.string.user_config_title);

        database = new UserConfigDb(this);

        name = getIntent().getStringExtra("name");
        advanced = getIntent().getBooleanExtra("advanced", false);

        parseIntent();
        // config is invalid or wasn't provided, create new
        if ( configuration == null ) {
            configuration = new UserConfig();
        }

        refreshConfigValues();

        configAdapter = new UserConfigAdapter(configuration, configValues);

        recyclerView = (RecyclerView) findViewById(R.id.recycler_view);
        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        recyclerView.setAdapter(configAdapter);
    }

    private void parseIntent() {
        if ( name != null ) {
            configuration = database.getConfig(name);
            if ( configuration == null ) {
                // name is not in db, clear name from args
                name = null;
            }
        } else if (getIntent().hasExtra("existing_config")) {
            // Confirm you want this config added dialog.
            Toast.makeText(this, "Not implemented", Toast.LENGTH_LONG).show();
        }
    }

    private void refreshConfigValues() {
        if ( configValues == null ) {
            configValues = new ArrayList<>();

            configValues.add(new Pair<>("HEADER", getString(R.string.general_title)));
            configValues.add(new Pair<>(UserConfig.CONFIG_NAME, getString(R.string.config_name_hint)));
            configValues.add(new Pair<>(UserConfig.USERNAME, getString(R.string.username_hint)));
            configValues.add(new Pair<>("HEADER", getString(R.string.server_title)));
            configValues.add(new Pair<>(UserConfig.SERVER_ADDRESS, getString(R.string.server_address_hint)));
            configValues.add(new Pair<>(UserConfig.SERVER_PORT, getString(R.string.server_port_hint)));

            if (advanced) {
                configValues.add(new Pair<>(UserConfig.ALTERNATE_SERVER_ADDRESS, getString(R.string.secondary_server_address_hint)));
                configValues.add(new Pair<>(UserConfig.ALTERNATE_SERVER_PORT, getString(R.string.secondary_server_port_hint)));
            }
        }
    }

    @Override
    public int getMenuId() {
        return R.menu.user_config_activity;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if ( item.getItemId() == R.id.save ) {
            // Save the document and close the activity.
            if ( save() ) {
                finish();
            } else {
                Toast.makeText(this, "Could not save - check your inputs", Toast.LENGTH_SHORT).show();
            }
        }
        return true;
    }

    private boolean save() {
        configAdapter.refreshConfig();

        UserConfig config = configAdapter.config;

        // Check the config is okay. It must have a server, a port, and a nick.
        if ( config.username.equals("") || config.serverAddress.equals("") ) {
            return false;
        }

        if ( config.serverPort == UserConfigDb.INVALID_ID ) {
            config.serverPort = 6667;
        }

        if ( configAdapter.config.id == UserConfigDb.INVALID_ID || name == null ) {
            // insert config
            return database.insertConfig(configAdapter.config) > 0;
        } else {
            // update config
            return database.updateConfig(configAdapter.config) > 0;
        }
    }

    @Override
    protected void onResume() {
        super.onResume();
    }
}
