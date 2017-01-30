package com.example.faheem.harrodsdemo;

import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Intent;
import android.content.res.Configuration;
import android.graphics.Color;
import android.os.Bundle;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.app.NotificationCompat;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListAdapter;
import android.widget.ListView;
import android.widget.Toast;

public class MainActivity extends AppCompatActivity {

    private Toolbar toolbar;
    private ListView listView;
    private DrawerLayout drawerLayout;
    private ActionBarDrawerToggle actionBarDrawerToggle;
    private ListView mainContentListView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        listView = (ListView)findViewById(R.id.myListDrawId);
        toolbar = (Toolbar)findViewById(R.id.toolbarId);
        drawerLayout=(DrawerLayout)findViewById(R.id.drawerLayoutId);
        mainContentListView = (ListView)findViewById(R.id.mainContentViewId);

        toolbar.setBackgroundColor(Color.BLACK);
        setSupportActionBar(toolbar);

        String items[] = {"HOME","MAGAZINE","STORE GUIDE", "EVENTS", "SERVICES", "SMART SCAN"};

        ListAdapter listAdapter = new ArrayAdapter<String>(this,R.layout.mytextview,items);
        ListAdapter customListAdapter =  new CustomAdapter(this,items);
        listView.setAdapter(listAdapter);
        mainContentListView.setAdapter(customListAdapter );

        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Toast.makeText(MainActivity.this,(parent.getItemAtPosition(position)).toString(), Toast.LENGTH_SHORT).show();
                drawerLayout.closeDrawers();
            }
        });

        mainContentListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Toast.makeText(MainActivity.this,(parent.getItemAtPosition(position)).toString(), Toast.LENGTH_SHORT).show();
                drawerLayout.closeDrawers();
            }
        });

         actionBarDrawerToggle = new ActionBarDrawerToggle(this,drawerLayout,R.string.drawer_open,R.string.drawer_closed);

        drawerLayout.setDrawerListener(actionBarDrawerToggle);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        //getSupportActionBar().setHomeButtonEnabled(true);

        Intent intent_service = new Intent(this,MQService.class);
        startService(intent_service);

    }

    @Override
    protected void onPostCreate(Bundle savedInstanceState) {
        super.onPostCreate(savedInstanceState);
        // Sync the toggle state after onRestoreInstanceState has occurred.
        actionBarDrawerToggle.syncState();
    }

    @Override
    public void onConfigurationChanged(Configuration newConfig) {
        super.onConfigurationChanged(newConfig);
        actionBarDrawerToggle.onConfigurationChanged(newConfig);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Pass the event to ActionBarDrawerToggle, if it returns
        // true, then it has handled the app icon touch event
        if (actionBarDrawerToggle.onOptionsItemSelected(item)) {
            return true;
        }
        // Handle your other action bar items...

        if (item.getTitle().equals("foo")) {
            Toast.makeText(this, "Notified", Toast.LENGTH_SHORT).show();
            NotificationCompat.Builder notificiation_builder = new NotificationCompat.Builder(this);
            notificiation_builder.setContentTitle("Harrod Test Notification Title");
            notificiation_builder.setAutoCancel(true);
            notificiation_builder.setContentIntent(PendingIntent.getActivity(this,0,new Intent(),0));
            notificiation_builder.setContentText("Harrod Test Notification Content");
            notificiation_builder.setSmallIcon(R.mipmap.ic_launcher);
            Notification n = notificiation_builder.build();
            NotificationManager nm = (NotificationManager) getSystemService(NOTIFICATION_SERVICE);
            nm.notify(123,n);
        }

        if(item.getTitle().equals("settings")){
            Intent i = new Intent(this,CustomerDetailActivity.class);
            startActivity(i);
        }

        return super.onOptionsItemSelected(item);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu, menu);
        return true;
    }
}
