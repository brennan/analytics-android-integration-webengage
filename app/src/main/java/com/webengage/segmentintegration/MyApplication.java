package com.webengage.segmentintegration;

import android.app.Application;

import com.segment.analytics.Analytics;
import com.segment.analytics.android.integrations.webengage.WebEngageIntegration;

/**
 * Created by shahrukhimam on 25/11/16.
 */

public class MyApplication extends Application {

    @Override
    public void onCreate() {
        super.onCreate();
        Analytics analytics = new Analytics.Builder(this, "YOUR_WRITE_KEY")
                .trackApplicationLifecycleEvents() // Enable this to record certain application events automatically!
                .logLevel(Analytics.LogLevel.VERBOSE)
                .use(WebEngageIntegration.FACTORY)
                .build();
        Analytics.setSingletonInstance(analytics);
    }
}
