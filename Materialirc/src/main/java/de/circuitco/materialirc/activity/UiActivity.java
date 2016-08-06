package de.circuitco.materialirc.activity;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;

import de.circuitco.materialirc.R;

/**
 * Created by alex on 03/07/16.
 */
public abstract class UiActivity extends AppCompatActivity {
    protected boolean paused = true;

    // Stuff you have to do
    public abstract int getLayoutId();

    // Stuff you might want to do/use
    public int getMenuId() {
        return 0;
    }
    public Toolbar toolbar = null;

    // Stuff UiActivity cares about
    // nothing yet i guess

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(getLayoutId());
        toolbar = (Toolbar)findViewById(R.id.toolbar);
        if ( toolbar != null ) {
            setSupportActionBar(toolbar);
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        if ( getMenuId() == 0 ) {
            return super.onCreateOptionsMenu(menu);
        } else {
            getMenuInflater().inflate(getMenuId(), menu);
            return true;
        }
    }

    @Override
    protected void onResume() {
        super.onResume();
        paused = false;
    }

    @Override
    protected void onPause() {
        super.onPause();
        paused = true;
    }
}
