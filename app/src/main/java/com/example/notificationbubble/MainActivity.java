package com.example.notificationbubble;

import androidx.appcompat.app.AppCompatActivity;

import android.app.NotificationManager;
import android.app.Service;
import android.content.Intent;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.provider.Settings;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import com.nex3z.notificationbadge.NotificationBadge;
import com.txusballesteros.bubbles.BubbleLayout;
import com.txusballesteros.bubbles.BubblesManager;
import com.txusballesteros.bubbles.OnInitializedCallback;

public class MainActivity extends AppCompatActivity {

    private BubblesManager bubblesManager;
    private NotificationBadge notificationBadge;
    private Button button;
    private final static int Request_Code=1;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        
        initBubble();
        button=findViewById(R.id.addBubbleBtn);
        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                addNewBubble();
            }
        });

        //check permission
        if(Build.VERSION.SDK_INT>=Build.VERSION_CODES.M )
        {
            if(!Settings.canDrawOverlays(MainActivity.this))
            {
                Intent intent=new Intent(Settings.ACTION_MANAGE_OVERLAY_PERMISSION, Uri.parse("package:"+getPackageName()));
                startActivityForResult(intent,Request_Code);
            }
        }
        else
        {
            Intent intent=new Intent(MainActivity.this, Service.class);
            startService(intent);
        }
    }

    private void initBubble() {
        bubblesManager=new BubblesManager.Builder(this)
                .setTrashLayout(R.layout.remove)
                .setInitializationCallback(new OnInitializedCallback() {
                    @Override
                    public void onInitialized() {
                        addNewBubble();
                    }
                }).build();
        bubblesManager.initialize();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        bubblesManager.recycle();
    }

    private void addNewBubble() {
        BubbleLayout bubbleView=(BubbleLayout) LayoutInflater.from(MainActivity.this).inflate(R.layout.bubblelayout,null,false);
        notificationBadge=(NotificationBadge) bubbleView.findViewById(R.id.badge);
        notificationBadge.setNumber(88);

        bubbleView.setOnBubbleRemoveListener(new BubbleLayout.OnBubbleRemoveListener() {
            @Override
            public void onBubbleRemoved(BubbleLayout bubble) {
                Toast.makeText(MainActivity.this, "Removed!", Toast.LENGTH_SHORT).show();
            }
        });

        bubbleView.setOnBubbleClickListener(new BubbleLayout.OnBubbleClickListener() {
            @Override
            public void onBubbleClick(BubbleLayout bubble) {
                Toast.makeText(MainActivity.this, "Clicked!", Toast.LENGTH_SHORT).show();
            }
        });

        bubbleView.setShouldStickToWall(true);
        bubblesManager.addBubble(bubbleView,60,20);
    }
}