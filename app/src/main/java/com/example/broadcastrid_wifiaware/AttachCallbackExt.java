package com.example.broadcastrid_wifiaware;

import android.annotation.TargetApi;

import android.content.Context;
import android.net.wifi.aware.AttachCallback;
import android.net.wifi.aware.DiscoverySessionCallback;
import android.net.wifi.aware.PeerHandle;
import android.net.wifi.aware.PublishConfig;
import android.net.wifi.aware.PublishDiscoverySession;
import android.net.wifi.aware.SubscribeConfig;
import android.net.wifi.aware.SubscribeDiscoverySession;
import android.net.wifi.aware.WifiAwareManager;
import android.net.wifi.aware.WifiAwareSession;
import android.os.Build;
import androidx.annotation.RequiresApi;
import android.util.Log;

import java.util.List;

public class AttachCallbackExt extends AttachCallback {

    // Variables
    private static final String AWARE_FILE_SHARE_SERVICE_NAME = "RemoteIDTest"; // Name for subscribe service
    private byte [] messageReceived = null;
    private MainActivity mActivity;

    public AttachCallbackExt(Context activity) {
        this.mActivity = (MainActivity) activity;
        Log.d("Object Created", "AttachCallback object created");
    }

    /**
     * Called when Aware attach operation
     * {@link WifiAwareManager#attach(AttachCallback, android.os.Handler)}
     * is completed and that we can now start discovery sessions or connections.
     *
     * @param session The Aware object on which we can execute further Aware operations - e.g.
     *                discovery, connections.
     */
    @TargetApi(Build.VERSION_CODES.O)
    @RequiresApi(api = Build.VERSION_CODES.O)
    @Override
    public void onAttached(final WifiAwareSession mAwaresession) {
        Log.d("Method called", "Attach operation completed and can now start discovery sessions");
        final byte[] messageSend = new byte[1];
        messageSend[0] = 104;

        // Start Subscribe Session
        SubscribeConfig config = new SubscribeConfig.Builder()
                .setServiceName(AWARE_FILE_SHARE_SERVICE_NAME)
                .build();

        mAwaresession.subscribe(config, new DiscoverySessionCallback() {

            SubscribeDiscoverySession mainSession;

            @Override
            public void onSubscribeStarted(SubscribeDiscoverySession session) {
                Log.d("Method Called", "onSubscribeStarted");
                mainSession = session;
            }
            @Override
            public void onServiceDiscovered(PeerHandle peerHandle, byte[] serviceSpecificInfo,
                                            List<byte[]> matchFilter) {
                Log.d("Method Called", "onServiceDiscovered");
                mainSession.sendMessage(peerHandle, 1, messageSend);
            }
            @Override
            public void onMessageReceived(PeerHandle peerHandle, byte[] messageFromPub) {
                Log.d("Method Called", "onMessageReceived");
                Log.d("Messaged Received is", new String(messageFromPub));
                messageReceived = messageFromPub;
            }}, null);
    }

    /**
     * Called when Aware attach operation
     * {@link WifiAwareManager#attach(AttachCallback, android.os.Handler)} failed.
     */
    @Override
    public void onAttachFailed() {
        Log.d("Method called", "Attach operation failed");
    }

    public byte[] getMessageReceived() {
        return messageReceived;
    }
}
