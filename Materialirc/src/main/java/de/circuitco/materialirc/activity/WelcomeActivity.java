package de.circuitco.materialirc.activity;

import android.os.Bundle;

import de.circuitco.materialirc.R;

/**
 * Created by alex on 03/07/16.
 */
public class WelcomeActivity extends UiActivity {
    @Override
    public int getLayoutId() {
        return R.layout.activity_welcome;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        // Show some nice text to warn users about the app.
    }
}
