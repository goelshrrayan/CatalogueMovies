package com.example.shrrmoviecat;

import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Bundle;
import android.support.design.widget.NavigationView;
import android.support.design.widget.TabLayout;
import android.support.v4.view.GravityCompat;
import android.support.v4.view.ViewPager;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.SearchView;
import android.support.v7.widget.Toolbar;
import android.util.Base64;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.TextView;

import com.example.shrrmoviecat.function.FavoriteFragment;
import com.example.shrrmoviecat.function.FindActivity;
import com.example.shrrmoviecat.function.SettingsActivity;
import com.example.shrrmoviecat.service.ATabPager;
import butterknife.BindView;
import butterknife.ButterKnife;
import de.hdodenhof.circleimageview.CircleImageView;

public class MainActivity extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener,
        TabLayout.OnTabSelectedListener {

    @BindView(R.id.toolbar)
    Toolbar toolbar;

    @BindView(R.id.drawer_layout)
    DrawerLayout drawerLayout;


    NavigationView nav_view;

    @BindView(R.id.tab_layout)
    TabLayout tabLayout;

    @BindView(R.id.view_pager)
    ViewPager viewPager;

    TextView edit;

    SharedPreferences sp;
    SharedPreferences.Editor editor;
    TextView name,email;
    CircleImageView image;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        ButterKnife.bind(this);
        nav_view=findViewById(R.id.nav_view);
sp=getSharedPreferences("profile",MODE_PRIVATE);
editor=sp.edit();

        View header = nav_view.getHeaderView(0);
        Log.i("navheader",nav_view.toString()+" yo "+header.toString());
        name=header.findViewById(R.id.nav_header_name);
        email=header.findViewById(R.id.nav_header_email);
        image=header.findViewById(R.id.imageView);
        edit=header.findViewById(R.id.edit_profile);


        edit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i=new Intent(MainActivity.this,ProfileActivity.class);

                startActivity(i);
            }
        });
        name.setText(sp.getString("name","Shrrayan Goel"));
        email.setText(sp.getString("email","goelshrrayan123@gmail.com"));
        Log.i("Imageb",sp.getString("imageurl",""));
        image.setImageURI(Uri.parse(sp.getString("imageurl","goelshrrayan123@gmail.com")));



        setSupportActionBar(toolbar);

        setupDrawer();
        setupTab();

        selectNav(0);
    }

    @Override
    public void onBackPressed() {
        if (drawerLayout.isDrawerOpen(GravityCompat.START)) {
            drawerLayout.closeDrawer(GravityCompat.START);
        } else {
            super.onBackPressed();
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {

        getMenuInflater().inflate(R.menu.main, menu);

        SearchView searchView = (SearchView) menu.findItem(R.id.action_search).getActionView();
        searchView.setQueryHint(getResources().getString(R.string.label_search));
        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                Intent intent = new Intent(getApplicationContext(), FindActivity.class);
                intent.putExtra(FindActivity.MOVIE_TITLE, query);
                startActivity(intent);

                return false;
            }

            @Override
            public boolean onQueryTextChange(String newText) {
                return false;
            }
        });

        return true;
    }



    @SuppressWarnings("StatementWithEmptyBody")
    @Override
    public boolean onNavigationItemSelected(MenuItem item) {

        int id = item.getItemId();

        if (id == R.id.nav_now_playing) selectTab(0);
        if (id == R.id.nav_upcoming) selectTab(1);
        if (id == R.id.nav_favorite)
        {startActivity(new Intent(MainActivity.this, FavoriteFragment.class));}
            //selectTab(2);

        if (id == R.id.action_settings) {
            Intent intent = new Intent(this, SettingsActivity.class);
            startActivity(intent);
            return true;
        }


        drawerLayout.closeDrawer(GravityCompat.START);
        return true;
    }


    @Override
    public void onTabSelected(TabLayout.Tab tab) {
        selectNav(tab.getPosition());
    }


    @Override
    public void onTabUnselected(TabLayout.Tab tab) {

    }


    @Override
    public void onTabReselected(TabLayout.Tab tab) {

    }

    private void setupDrawer() {
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawerLayout, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawerLayout.setDrawerListener(toggle);
        toggle.syncState();
        nav_view.setNavigationItemSelectedListener(this);
    }

    private void setupTab() {
        viewPager.setAdapter(new ATabPager(getSupportFragmentManager()));
        tabLayout.setupWithViewPager(viewPager);

        tabLayout.getTabAt(0).setText(R.string.label_now_playing);
        tabLayout.getTabAt(1).setText(R.string.label_upcoming);

        tabLayout.setOnTabSelectedListener(this);
    }

    private void selectNav(int navNumber) {
        nav_view.getMenu().getItem(navNumber).setChecked(true);
    }

    private void selectTab(int tabNumber) {
        tabLayout.getTabAt(tabNumber).select();
    }

    public Bitmap StringToBitMap(String encodedString){
        try{
            byte [] encodeByte = Base64.decode(encodedString,Base64.DEFAULT);
            Bitmap bitmap = BitmapFactory.decodeByteArray(encodeByte, 0, encodeByte.length);
            return bitmap;
        }
        catch(Exception e){
            e.getMessage();
            return null;
        }
    }
}
