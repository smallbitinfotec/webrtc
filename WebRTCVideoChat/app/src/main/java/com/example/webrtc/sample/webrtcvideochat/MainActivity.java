package com.example.webrtc.sample.webrtcvideochat;

import android.Manifest;
import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.AppCompatButton;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.FrameLayout;
import android.widget.TextView;

import com.opentok.android.OpentokError;
import com.opentok.android.Publisher;
import com.opentok.android.PublisherKit;
import com.opentok.android.Session;
import com.opentok.android.Stream;
import com.opentok.android.Subscriber;

import butterknife.BindView;
import butterknife.ButterKnife;
import pub.devrel.easypermissions.AfterPermissionGranted;
import pub.devrel.easypermissions.EasyPermissions;


public class MainActivity extends AppCompatActivity implements Session.SessionListener, PublisherKit.PublisherListener, View.OnClickListener {
    String[] permissions = new String[]{
            Manifest.permission.CAMERA,
            Manifest.permission.INTERNET,
            Manifest.permission.RECORD_AUDIO,
            Manifest.permission.MODIFY_AUDIO_SETTINGS,
            Manifest.permission.BLUETOOTH,
            Manifest.permission.BROADCAST_STICKY,
            Manifest.permission.READ_PHONE_STATE
    };
    private final int MULTIPLE_PERMISSIONS = 123;
    private static String API_KEY = "46331702";
    private static String SESSION_ID = "2_MX40NjMzMTcwMn5-MTU1ODMzNTIyMDI2OH5XN1gwcDVUMVlSM2NoSHVHMG5LNndrOTN-fg";
    private static String TOKEN = "T1==cGFydG5lcl9pZD00NjMzMTcwMiZzaWc9MGQ0MWEzMzgyOTVmYzk5N2I2ZjAwZGNmOTg5NjRjZTFjMGVlNzhiNDpzZXNzaW9uX2lkPTJfTVg0ME5qTXpNVGN3TW41LU1UVTFPRE16TlRJeU1ESTJPSDVYTjFnd2NEVlVNVmxTTTJOb1NIVkhNRzVMTm5kck9UTi1mZyZjcmVhdGVfdGltZT0xNTU4MzM1MjQ1Jm5vbmNlPTAuMDQ4MjM2NjgwNjAwMzI5Mzc0JnJvbGU9cHVibGlzaGVyJmV4cGlyZV90aW1lPTE1NTg5NDAwNDYmaW5pdGlhbF9sYXlvdXRfY2xhc3NfbGlzdD0=";
    private static final String LOG_TAG = MainActivity.class.getSimpleName();
    private static final int RC_SETTINGS_SCREEN_PERM = 123;
    private static final int RC_VIDEO_APP_PERM = 124;
    private Publisher mPublisher;
    private Subscriber mSubscriber;
    private Session mSession;
    private FrameLayout mPublisherViewContainer;
    private FrameLayout mSubscriberViewContainer;

    @BindView(R.id.btn_end_room)
    AppCompatButton btnEndCall;
    @BindView(R.id.btn_join_room)
    AppCompatButton btnStartCall;
    @BindView(R.id.camera_preview)
    FrameLayout preview;
    ProgressDialog progressDialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        ButterKnife.bind(this);
        requestPermissions();
        btnStartCall.setOnClickListener(this);
        btnEndCall.setOnClickListener(this);
        progressDialog = new ProgressDialog(this,
                R.style.AppTheme_Dark_Dialog);
        progressDialog.setIndeterminate(true);
        progressDialog.setMessage("Please wait...");

    }

    @AfterPermissionGranted(RC_VIDEO_APP_PERM)
    private void requestPermissions() {
        String[] perms = permissions;
        if (EasyPermissions.hasPermissions(this, perms)) {
            // initialize view objects from your layout
            mPublisherViewContainer = (FrameLayout) findViewById(R.id.publisher_container);
            mSubscriberViewContainer = (FrameLayout) findViewById(R.id.subscriber_container);
            mSession = new Session.Builder(this, API_KEY, SESSION_ID).build();
            mSession.setSessionListener(this);


            // initialize and connect to the session
        } else {
            EasyPermissions.requestPermissions(this, "This app needs access to your camera and mic to make video calls", RC_VIDEO_APP_PERM, perms);
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, String permissions[], int[] grantResults) {
        EasyPermissions.onRequestPermissionsResult(requestCode, permissions, grantResults, this);
    }


    public void startStopCall() {
        if (preview.getVisibility() == View.GONE) {
            progressDialog.show();
            preview.setVisibility(View.VISIBLE);
            btnStartCall.setVisibility(View.GONE);
        } else {
            preview.setVisibility(View.GONE);
            btnStartCall.setVisibility(View.VISIBLE);
        }
    }

    @Override
    public void onConnected(Session session) {
        progressDialog.dismiss();
        Log.i(LOG_TAG, "Session Connected");
        mPublisher = new Publisher.Builder(this).build();
        mPublisher.setPublisherListener(this);
        mPublisherViewContainer.addView(mPublisher.getView());
        mSession.publish(mPublisher);
    }

    @Override
    public void onDisconnected(Session session) {

    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        mSession.disconnect();
        finish();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        mSession.disconnect();
    }

    @Override
    public void onStreamReceived(Session session, Stream stream) {
        Log.i(LOG_TAG, "Stream Received");

        if (mSubscriber == null) {
            mSubscriber = new Subscriber.Builder(this, stream).build();
            mSession.subscribe(mSubscriber);
            mSubscriberViewContainer.addView(mSubscriber.getView());
        }
    }

    @Override
    public void onStreamDropped(Session session, Stream stream) {
        Log.i(LOG_TAG, "Stream Dropped");

        if (mSubscriber != null) {
            mSubscriber = null;
            mSubscriberViewContainer.removeAllViews();
        }
    }

    @Override
    public void onError(Session session, OpentokError opentokError) {

    }

    @Override
    public void onStreamCreated(PublisherKit publisherKit, Stream stream) {

    }

    @Override
    public void onStreamDestroyed(PublisherKit publisherKit, Stream stream) {

    }

    @Override
    public void onError(PublisherKit publisherKit, OpentokError opentokError) {

    }

    @Override
    public void onClick(View v) {

        switch (v.getId()) {
            case R.id.btn_end_room:
                mSession.disconnect();
                startStopCall();
                break;
            case R.id.btn_join_room:
                mSession.connect(TOKEN);
                startStopCall();
                break;
        }
    }
}
