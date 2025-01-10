package com.example.vladbistrov;

import android.os.Bundle;
import androidx.appcompat.app.AppCompatActivity;
import androidx.navigation.NavController;
import androidx.navigation.fragment.NavHostFragment;
import androidx.navigation.ui.NavigationUI;
import com.google.android.material.bottomnavigation.BottomNavigationView;

public class MainActivity extends AppCompatActivity {
    private NavController navController;
    private BottomNavigationView bottomNavigationView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        // Инициализация компонентов навигации
        bottomNavigationView = findViewById(R.id.bottom_navigation);
        NavHostFragment navHostFragment = (NavHostFragment) getSupportFragmentManager()
                .findFragmentById(R.id.nav_host_fragment);
        NavController navController = navHostFragment.getNavController();
        NavigationUI.setupWithNavController(bottomNavigationView, navController);

        // Обработка навигационных событий
        navController.addOnDestinationChangedListener((controller, destination, arguments) -> {
            // Дополнительная логика при смене экрана
            updateToolbarTitle(destination.getLabel());
        });
    }

    private void updateToolbarTitle(CharSequence label) {
        if (getSupportActionBar() != null) {
            getSupportActionBar().setTitle(label);
        }
    }
}