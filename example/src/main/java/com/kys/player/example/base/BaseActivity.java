package com.kys.player.example.base;

import android.app.Activity;
import android.content.res.Configuration;
import android.content.res.Resources;

/**
 * Created by bsy on 2016/8/2.
 */
public class BaseActivity extends Activity {
    @Override
    public Resources getResources() {
        Resources res = super.getResources();
        Configuration config = new Configuration();
        config.setToDefaults();
        res.updateConfiguration(config, res.getDisplayMetrics());
        return res;
    }

}
