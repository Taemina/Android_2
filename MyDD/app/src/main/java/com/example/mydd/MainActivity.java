package com.example.mydd;

import android.os.Bundle;

import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.snackbar.Snackbar;

import android.view.View;

import androidx.core.view.GravityCompat;
import androidx.appcompat.app.ActionBarDrawerToggle;

import android.view.MenuItem;

import com.google.android.material.navigation.NavigationView;

import androidx.drawerlayout.widget.DrawerLayout;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import android.view.Menu;
import android.widget.TextView;

public class MainActivity extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        DrawerLayout drawer = findViewById(R.id.drawer_layout);
        NavigationView navigationView = findViewById(R.id.nav_view);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.addDrawerListener(toggle);
        toggle.syncState();
        navigationView.setNavigationItemSelectedListener(this);
    }

    @Override
    public void onBackPressed() {
        DrawerLayout drawer = findViewById(R.id.drawer_layout);
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);
        } else {
            super.onBackPressed();
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();
        DrawerLayout drawerLayout = findViewById(R.id.drawer_layout);


        if (id == R.id.action_settings) {
            Snackbar.make(drawerLayout, "action_settings", Snackbar.LENGTH_LONG)
                    .show();
            return true;
        }
        if (id == R.id.action_info) {
            Snackbar.make(drawerLayout, "action_info", Snackbar.LENGTH_LONG)
                    .show();
            return true;
        }
        if (id == R.id.action_plus) {
            Snackbar.make(drawerLayout, "action_plus", Snackbar.LENGTH_LONG)
                    .show();
            return true;
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    public boolean onNavigationItemSelected(MenuItem item) {
        TextView textView = findViewById(R.id.content_text);
        int id = item.getItemId();

        if (id == R.id.nav_home) {
            textView.setText("nav_home");
        } else if (id == R.id.nav_gallery) {
            textView.setText("nav_gallery");
        } else if (id == R.id.nav_tools) {
            textView.setText("nav_tools");
        } else if (id == R.id.nav_send) {
            textView.setText("nav_send");
        } else if (id == R.id.nav_info) {
            textView.setText("nav_info");
        }


        DrawerLayout drawer = findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }
}
