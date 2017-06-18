package io.psych.io2017.architecture;

import android.app.Activity;
import android.arch.lifecycle.Lifecycle;
import android.arch.lifecycle.LifecycleObserver;
import android.arch.lifecycle.OnLifecycleEvent;
import android.os.Bundle;

import io.psych.io2017.R;

// TODO:
public class LifecycleObserverActivity extends Activity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_lifecycle_observer);
    }

    static class MyLifecycleObserver implements LifecycleObserver {

        @OnLifecycleEvent(Lifecycle.Event.ON_CREATE)
        void onCreate() {

        }

        @OnLifecycleEvent(Lifecycle.Event.ON_START)
        void onStart() {

        }

        @OnLifecycleEvent(Lifecycle.Event.ON_RESUME)
        void onResume() {

        }

        @OnLifecycleEvent(Lifecycle.Event.ON_PAUSE)
        void onPause() {

        }

        @OnLifecycleEvent(Lifecycle.Event.ON_STOP)
        void onStop() {

        }

        @OnLifecycleEvent(Lifecycle.Event.ON_DESTROY)
        void onDestroy() {

        }

    }
}
