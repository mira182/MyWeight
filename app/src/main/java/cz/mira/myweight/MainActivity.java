package cz.mira.myweight;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.view.Menu;
import android.view.View;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;
import androidx.navigation.ui.AppBarConfiguration;
import androidx.navigation.ui.NavigationUI;

import com.github.clans.fab.FloatingActionButton;
import com.github.clans.fab.FloatingActionMenu;
import com.google.android.material.navigation.NavigationView;

import java.io.IOException;

import cz.mira.myweight.rest.RestUtils;
import cz.mira.myweight.rest.weight.WeightRestService;
import cz.mira.myweight.timer.CheckNewEmailTimer;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class MainActivity extends AppCompatActivity {

    private static final String TAG = "MainActivity";

    private AppBarConfiguration mAppBarConfiguration;

    private FloatingActionMenu actionsFabMenu;

    private boolean doubleBackToExitPressedOnce = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        actionsFabMenu = findViewById(R.id.main_action_menu);
        FloatingActionButton showChartsFab = findViewById(R.id.show_charts_item);
        FloatingActionButton updateWeightReportFab = findViewById(R.id.refresh_item);
        DrawerLayout drawer = findViewById(R.id.drawer_layout);
        NavigationView navigationView = findViewById(R.id.nav_view);
        // Passing each menu ID as a set of Ids because each
        // menu should be considered as top level destinations.
        mAppBarConfiguration = new AppBarConfiguration.Builder(
                R.id.nav_home, R.id.nav_gallery, R.id.nav_slideshow,
                R.id.nav_tools, R.id.nav_share, R.id.nav_send)
                .setDrawerLayout(drawer)
                .build();
        NavController navController = Navigation.findNavController(this, R.id.nav_host_fragment);
        NavigationUI.setupActionBarWithNavController(this, navController, mAppBarConfiguration);
        NavigationUI.setupWithNavController(navigationView, navController);
        ConstraintLayout contentMainLayout = findViewById(R.id.content_main_layout);
        contentMainLayout.setOnClickListener(v -> actionsFabMenu.close(true));

        final WeightRestService weightReportService = RestUtils.getWeightRestService();

        showChartsFab.setOnClickListener(v -> {
            Intent intent = new Intent(this, ChartsActivity.class);
            startActivity(intent);
        });

        new CheckNewEmailTimer(doesNewEmailExist -> {
            if (!doesNewEmailExist) updateWeightReportFab.setVisibility(View.GONE);
            else if (doesNewEmailExist && actionsFabMenu.isOpened()) updateWeightReportFab.setVisibility(View.VISIBLE);
        });

        updateWeightReportFab.setOnClickListener(v -> {
            Call<Boolean> updateWeightReportCall = weightReportService.updateWeightReport();
            updateWeightReportCall.enqueue(new Callback<Boolean>() {
                @Override
                public void onResponse(Call<Boolean> call, Response<Boolean> response) {
                    if (response.isSuccessful()) {
                        final String updatedString = "Weight report was updated successfully.";
                        Toast.makeText(getApplicationContext(), updatedString, Toast.LENGTH_SHORT).show();
                        Log.i(TAG, updatedString);
                    } else {
                        try {
                            Toast.makeText(getApplicationContext(), "Failed to update weight report", Toast.LENGTH_SHORT).show();
                            Log.e(TAG, "Failed to update weight report: " + response.errorBody().string());
                        } catch (IOException e) {
                            e.printStackTrace();
                        }
                    }
                }

                @Override
                public void onFailure(Call<Boolean> call, Throwable t) {
                    Toast.makeText(getApplicationContext(), "Failed to call : " + call, Toast.LENGTH_SHORT).show();
                    Log.e(TAG, "Failed to call : " + call, t);
                }
            });
        });
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.main, menu);
        return true;
    }

    @Override
    public boolean onSupportNavigateUp() {
        NavController navController = Navigation.findNavController(this, R.id.nav_host_fragment);
        return NavigationUI.navigateUp(navController, mAppBarConfiguration)
                || super.onSupportNavigateUp();
    }

    @Override
    protected void onResume() {
        super.onResume();
        if (actionsFabMenu != null) {
            actionsFabMenu.close(true);
        }
    }

    @Override
    public void onBackPressed() {
        if (doubleBackToExitPressedOnce) {
            super.onBackPressed();
            return;
        }

        this.doubleBackToExitPressedOnce = true;
        Toast.makeText(this, "Please click BACK again to exit", Toast.LENGTH_SHORT).show();

        new Handler().postDelayed(new Runnable() {

            @Override
            public void run() {
                doubleBackToExitPressedOnce=false;
            }
        }, 2000);
    }
}
