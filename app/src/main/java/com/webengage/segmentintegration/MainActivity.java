package com.webengage.segmentintegration;

import android.app.Activity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import com.segment.analytics.Analytics;
import com.segment.analytics.Options;
import com.segment.analytics.Properties;
import com.segment.analytics.Traits;

import java.util.UUID;

public class MainActivity extends Activity implements View.OnClickListener {
    Button login, track, setAttr, logout;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        login = (Button) findViewById(R.id.login);
        track = (Button) findViewById(R.id.track);
        setAttr = (Button) findViewById(R.id.setAttribute);
        logout = (Button) findViewById(R.id.logout);
        login.setOnClickListener(this);
        track.setOnClickListener(this);
        setAttr.setOnClickListener(this);
        logout.setOnClickListener(this);

    }

    @Override
    protected void onStart() {
        super.onStart();
        Analytics.with(this.getApplicationContext()).screen("MainScreen", new Properties().putValue("abc", 1).putValue("discount", true));
    }


    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.login:

                Analytics.with(this).identify(UUID.randomUUID().toString());
                break;

            case R.id.track:
                Analytics.with(this.getApplicationContext()).track("BigPictureNotification", new Properties().putValue("price", 200),new Options().setIntegration("Mixpanel",true).setIntegration("KISSmetrics",true));

                Analytics.with(this.getApplicationContext()).track("CheckoutStarted", new Properties().putValue("price", 100).putValue("currency", "INR"));
                Analytics.with(this.getApplicationContext()).track("CheckoutCompleted", new Properties().putValue("price", 100).putValue("discount", 50));

                break;


            case R.id.setAttribute:

                Analytics.with(this.getApplicationContext()).identify(new Traits().putFirstName("alexa").putLastName("board").putGender("male").putPhone("8888888888").putAge(24).putEmail("abc@gmail.com"));
                Analytics.with(this.getApplicationContext()).identify(new Traits().putValue("isA", true).putValue("b", 10));
                break;

            case R.id.logout:

                Analytics.with(this.getApplicationContext()).reset();
                break;
        }
    }
}
