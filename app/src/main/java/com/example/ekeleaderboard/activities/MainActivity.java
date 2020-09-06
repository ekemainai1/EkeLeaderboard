package com.example.ekeleaderboard.activities;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;
import androidx.navigation.fragment.NavHostFragment;
import androidx.navigation.ui.AppBarConfiguration;
import androidx.navigation.ui.NavigationUI;
import androidx.work.BackoffPolicy;
import androidx.work.Constraints;
import androidx.work.ExistingPeriodicWorkPolicy;
import androidx.work.NetworkType;
import androidx.work.PeriodicWorkRequest;
import androidx.work.WorkManager;
import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Build;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.RelativeLayout;
import android.widget.Toast;

import com.example.ekeleaderboard.R;
import com.example.ekeleaderboard.services.LoadLearnersWorkManager;
import com.example.ekeleaderboard.services.LoadSkillIqWorkManager;
import com.google.android.material.appbar.AppBarLayout;
import com.google.android.material.appbar.CollapsingToolbarLayout;
import java.util.concurrent.TimeUnit;


public class MainActivity extends AppCompatActivity {

    private static final String TAG_SYNC_DATA = "SKILLIQ URL";
    private static final String SYNC_DATA_WORK_NAME = "SKILLIQ DATA";
    private static final String TAG_SYNC_DATA_VOD = "LEARNERS URL";
    private static final String SYNC_DATA_WORK_NAME_VOD = "LEARNERS DATA";
    private static final int PERMISSIONS_REQUEST_CODE = 200;
    private AppBarConfiguration mAppBarConfiguration;
    Toolbar toolbar;
    AppBarLayout appBarLayout;
    CollapsingToolbarLayout collapsingToolbarLayout;
    RelativeLayout relativeLayout;
    public Button submitButton;
    NavController navController;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        // If All permission is enabled successfully then this block will execute.
        if(CheckingPermissionIsEnabledOrNot()) {
            Toast.makeText(MainActivity.this, "Permission Granted",
                    Toast.LENGTH_LONG).show();
        } else {
            //Calling method to enable permission.
            requestMultiplePermission();
        }

        toolbar = (Toolbar) findViewById(R.id.toolbar);
        appBarLayout = (AppBarLayout) findViewById(R.id.mainScreenAppBar);
        collapsingToolbarLayout = (CollapsingToolbarLayout) findViewById(R.id.main_collapsing);
        relativeLayout = (RelativeLayout) findViewById(R.id.main_relative);
        submitButton = (Button) findViewById(R.id.submitButton);

        setSupportActionBar(toolbar);

        // Schedule background work
        scheduleLearnersNetworkDataFetch();
        scheduleSkillIqNetworkDataFetch();

        // Passing each menu ID as a set of Ids because each
        // menu should be considered as top level destinations.
        mAppBarConfiguration = new AppBarConfiguration.Builder(
                R.id.localViewPagerFragment,
                R.id.learningLeadersFragment, R.id.skillIQLeadersFragment)
                .build();

        NavHostFragment navHostFragment =
                (NavHostFragment) getSupportFragmentManager()
                        .findFragmentById(R.id.nav_host_fragment);
        assert navHostFragment != null;
        navController = navHostFragment.getNavController();

        //navController = Navigation.findNavController(this, R.id.nav_host_fragment);
        NavigationUI.setupActionBarWithNavController(this, navController, mAppBarConfiguration);
        NavigationUI.setupWithNavController(toolbar, navController, mAppBarConfiguration);

        submitButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(MainActivity.this, LeadersPostActivity.class);
                startActivity(intent);
            }
        });
    }

    @Override
    public boolean onSupportNavigateUp() {
        navController = Navigation.findNavController(this, R.id.nav_host_fragment);
        return NavigationUI.navigateUp(navController, mAppBarConfiguration)
                || super.onSupportNavigateUp();
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        navController = Navigation.findNavController(this, R.id.nav_host_fragment);
        return NavigationUI.onNavDestinationSelected(item, navController)
                || super.onOptionsItemSelected(item);
    }

    // Request all app permissions at once
    private void requestMultiplePermission() {

        // Creating String Array with Permissions.
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.Q) {
            ActivityCompat.requestPermissions(MainActivity.this, new String[]{
                    Manifest.permission.READ_EXTERNAL_STORAGE,
                    Manifest.permission.WRITE_EXTERNAL_STORAGE,
            }, PERMISSIONS_REQUEST_CODE);
        }

    }

    // Calling override method.
    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions,
                                           @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if (requestCode == PERMISSIONS_REQUEST_CODE) {
            if (grantResults.length > 0) {

                boolean ReadExternalStoragePermission = grantResults[0] == PackageManager.PERMISSION_GRANTED;
                boolean WriteExternalStoragePermission = grantResults[1] == PackageManager.PERMISSION_GRANTED;


                if ( ReadExternalStoragePermission && WriteExternalStoragePermission ) {

                    Toast.makeText(MainActivity.this, "Permission Granted",
                            Toast.LENGTH_LONG).show();
                } else {
                    Toast.makeText(MainActivity.this, "Permission Denied",
                            Toast.LENGTH_LONG).show();

                }
            }
        }
    }

    // Check if permission is granted or not
    public boolean CheckingPermissionIsEnabledOrNot() {

        int SecondPermissionResult = ContextCompat.checkSelfPermission(getApplicationContext(),
                Manifest.permission.READ_EXTERNAL_STORAGE);
        int ThirdPermissionResult = ContextCompat.checkSelfPermission(getApplicationContext(),
                Manifest.permission.WRITE_EXTERNAL_STORAGE);

        return  SecondPermissionResult == PackageManager.PERMISSION_GRANTED &&
                ThirdPermissionResult == PackageManager.PERMISSION_GRANTED;

    }


    public void scheduleLearnersNetworkDataFetch() {

        Constraints constraints = new Constraints.Builder()
                .setRequiresBatteryNotLow(true)
                .setRequiredNetworkType(NetworkType.CONNECTED)
                .setRequiresCharging(false)
                .setRequiresStorageNotLow(true)
                .build();

        WorkManager workManagerVod = WorkManager.getInstance(getApplicationContext());
        PeriodicWorkRequest periodicSyncDataWorkVod =
                new PeriodicWorkRequest.Builder(LoadLearnersWorkManager.class,
                        15, TimeUnit.MINUTES)
                        .addTag(TAG_SYNC_DATA_VOD)
                        .setConstraints(constraints)
                        // setting a backoff on case the work needs to retry
                        .setBackoffCriteria(BackoffPolicy.LINEAR,
                                PeriodicWorkRequest.MIN_BACKOFF_MILLIS, TimeUnit.MILLISECONDS)
                        .build();
        workManagerVod.enqueueUniquePeriodicWork(
                SYNC_DATA_WORK_NAME_VOD,
                ExistingPeriodicWorkPolicy.KEEP, //Existing Periodic Work policy
                periodicSyncDataWorkVod //work request
        );

    }

    public void scheduleSkillIqNetworkDataFetch() {

        Constraints constraint = new Constraints.Builder()
                .setRequiresBatteryNotLow(true)
                .setRequiredNetworkType(NetworkType.CONNECTED)
                .setRequiresCharging(false)
                .setRequiresStorageNotLow(true)
                .build();

        WorkManager workManager = WorkManager.getInstance(getApplicationContext());
        PeriodicWorkRequest periodicSyncDataWork =
                new PeriodicWorkRequest.Builder(LoadSkillIqWorkManager.class,
                        15, TimeUnit.MINUTES)
                        .addTag(TAG_SYNC_DATA)
                        .setConstraints(constraint)
                        // setting a backoff on case the work needs to retry
                        .setBackoffCriteria(BackoffPolicy.LINEAR,
                                PeriodicWorkRequest.MIN_BACKOFF_MILLIS, TimeUnit.MILLISECONDS)
                        .build();
        workManager.enqueueUniquePeriodicWork(
                SYNC_DATA_WORK_NAME,
                ExistingPeriodicWorkPolicy.KEEP, //Existing Periodic Work policy
                periodicSyncDataWork //work request
        );
    }


}