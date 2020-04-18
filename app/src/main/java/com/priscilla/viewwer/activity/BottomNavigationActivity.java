package com.priscilla.viewwer.activity;

import android.os.Bundle;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;

import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.priscilla.viewwer.Fragment.CreateFragment;
import com.priscilla.viewwer.Fragment.FavoriteFragment;
import com.priscilla.viewwer.Fragment.ProfilFragment;
import com.priscilla.viewwer.Fragment.SearchFragment;
import com.priscilla.viewwer.R;
import com.priscilla.viewwer.model.responseTour;

import java.util.ArrayList;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;
import androidx.navigation.NavController;
import androidx.navigation.NavDestination;
import androidx.navigation.Navigation;
import androidx.navigation.ui.AppBarConfiguration;
import androidx.navigation.ui.NavigationUI;

public class BottomNavigationActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_bottom_navigation);
        BottomNavigationView navView = findViewById(R.id.nav_view);
        // Passing each menu ID as a set of Ids because each
        // menu should be considered as top level destinations.
        AppBarConfiguration appBarConfiguration = new AppBarConfiguration.Builder(
                R.id.navigation_search, R.id.navigation_favorites, R.id.navigation_create, R.id.navigation_posts,R.id.navigation_profile)
                .build();
        NavController navController = Navigation.findNavController(this, R.id.nav_host_fragment);
//        NavigationUI.setupActionBarWithNavController(this, navController, appBarConfiguration);
        NavigationUI.setupWithNavController(navView, navController);

        navController.addOnDestinationChangedListener(new NavController.OnDestinationChangedListener() {
            @Override
            public void onDestinationChanged(@NonNull NavController controller, @NonNull NavDestination destination, @Nullable Bundle arguments) {
                if(destination.getLabel().toString().equals(getString(R.string.title_create))){
                    navView.setVisibility(View.GONE);

                }else{
                    navView.setVisibility(View.VISIBLE);
                }
            }
        });


        Bundle extras = getIntent().getExtras();
        Log.d("exter","iiii");
        if(extras!=null && extras.containsKey("openProfil")) {
            boolean openF2 = extras.getBoolean("openProfil");
            if(openF2){
                //add or replace fragment F2 in container
                Fragment mFragment = null;
                mFragment = new ProfilFragment();
                FragmentManager fragmentManager = getSupportFragmentManager();
                fragmentManager.beginTransaction().replace(R.id.search_fragment, mFragment).commit();
            }
        }


        // ArrayList<responseTour> values = getIntent().get("myObj");
    }
}
