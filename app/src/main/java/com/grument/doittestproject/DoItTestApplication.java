package com.grument.doittestproject;

import android.annotation.TargetApi;
import android.support.multidex.MultiDex;
import android.support.multidex.MultiDexApplication;
import android.util.Log;

import com.grument.doittestproject.util.FakeCrashLibrary;

import timber.log.Timber;


import static android.os.Build.VERSION_CODES.LOLLIPOP_MR1;


public class DoItTestApplication extends MultiDexApplication{


    @Override
    public void onCreate() {
        super.onCreate();
        MultiDex.install(this);

        plantTimber();

    }


    @TargetApi(LOLLIPOP_MR1)
    private void plantTimber(){
        if (BuildConfig.DEBUG) {
            Timber.plant(new Timber.DebugTree());
        } else {
            Timber.plant(new CrashReportingTree());
        }
    }

    /**
     * A tree which logs important information for crash reporting.
     */
    private static class CrashReportingTree extends Timber.Tree {

        @Override
        protected void log(int priority, String tag, String message, Throwable t) {
            if (priority == Log.VERBOSE || priority == Log.DEBUG) {
                return;
            }

            FakeCrashLibrary.log(priority, tag, message);

            if (t != null) {
                if (priority == Log.ERROR) {
                    FakeCrashLibrary.logError(t);
                } else if (priority == Log.WARN) {
                    FakeCrashLibrary.logWarning(t);
                }
            }
        }
    }
}


