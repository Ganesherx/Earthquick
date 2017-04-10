package com.example.android.quakereport;


import android.content.AsyncTaskLoader;
import android.content.Context;
import android.util.Log;

import java.util.List;

public class EarthquakeLoader extends AsyncTaskLoader<List<Earthquake>> {
    private static final String LOG_TAG = EarthquakeLoader.class.getName();
    private String mUrl;

    public EarthquakeLoader(Context context, String url) {
        super(context);
        this.mUrl = url;
    }

    @Override
    protected void onStartLoading() {
        Log.i(LOG_TAG, "TEST: onStartLoading called...");
        forceLoad();
    }

    /**
     * this is backgroung Thread
     */

    @Override
    public List<Earthquake> loadInBackground() {
        Log.i(LOG_TAG, "TEST: loadInBackground() called...");
        if(mUrl == null){
            return null;
        }
        Log.e(LOG_TAG,"mURL:"+mUrl);
        List<Earthquake> earthquakes= QueryUtils.fetchEarthquakeData(mUrl);
        Log.e(LOG_TAG,"earthquakes:"+earthquakes);
        return earthquakes;
    }
}
