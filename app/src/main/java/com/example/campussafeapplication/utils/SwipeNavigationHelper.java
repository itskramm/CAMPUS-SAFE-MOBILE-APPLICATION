package com.example.campussafeapplication.utils;

import android.content.Intent;
import android.view.GestureDetector;
import android.view.MotionEvent;
import android.view.View;
import androidx.appcompat.app.AppCompatActivity;
import com.example.campussafeapplication.MainActivity;
import com.example.campussafeapplication.MyReportsActivity;
import com.example.campussafeapplication.NearbyReportsActivity;
import com.example.campussafeapplication.ReportHazardActivity;
import com.example.campussafeapplication.SettingsActivity;

public final class SwipeNavigationHelper {
    private static final int SWIPE_DISTANCE_THRESHOLD = 150;
    private static final int SWIPE_VELOCITY_THRESHOLD = 150;

    private static final Class<?>[] NAVIGATION_ORDER = new Class<?>[] {
            MainActivity.class,
            MyReportsActivity.class,
            ReportHazardActivity.class,
            NearbyReportsActivity.class,
            SettingsActivity.class
    };

    public enum Screen {
        HOME(0),
        REPORTS(1),
        ADD(2),
        MAPS(3),
        SETTINGS(4);

        private final int index;

        Screen(int index) {
            this.index = index;
        }

        int index() {
            return index;
        }
    }

    private SwipeNavigationHelper() {
    }

    public static void attach(AppCompatActivity activity, Screen currentScreen) {
        View rootContent = activity.findViewById(android.R.id.content);
        if (rootContent == null) {
            return;
        }

        GestureDetector detector = new GestureDetector(activity, new GestureDetector.SimpleOnGestureListener() {
            @Override
            public boolean onDown(MotionEvent e) {
                return true;
            }

            @Override
            public boolean onFling(MotionEvent e1, MotionEvent e2, float velocityX, float velocityY) {
                if (e1 == null || e2 == null) {
                    return false;
                }

                float deltaX = e2.getX() - e1.getX();
                float deltaY = e2.getY() - e1.getY();
                if (Math.abs(deltaX) <= Math.abs(deltaY)) {
                    return false;
                }
                if (Math.abs(deltaX) < SWIPE_DISTANCE_THRESHOLD || Math.abs(velocityX) < SWIPE_VELOCITY_THRESHOLD) {
                    return false;
                }

                int targetIndex = deltaX < 0 ? currentScreen.index() + 1 : currentScreen.index() - 1;
                return navigateTo(activity, targetIndex, currentScreen.index());
            }
        });

        rootContent.setOnTouchListener((v, event) -> detector.onTouchEvent(event));
    }

    private static boolean navigateTo(AppCompatActivity activity, int targetIndex, int currentIndex) {
        if (targetIndex < 0 || targetIndex >= NAVIGATION_ORDER.length || targetIndex == currentIndex) {
            return false;
        }

        Class<?> destination = NAVIGATION_ORDER[targetIndex];
        if (activity.getClass().equals(destination)) {
            return false;
        }

        Intent intent = new Intent(activity, destination);
        intent.addFlags(Intent.FLAG_ACTIVITY_REORDER_TO_FRONT);
        activity.startActivity(intent);
        return true;
    }
}
