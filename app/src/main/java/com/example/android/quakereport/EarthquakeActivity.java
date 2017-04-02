/*
 * Copyright (C) 2016 The Android Open Source Project
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package com.example.android.quakereport;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.widget.ArrayAdapter;
import android.widget.ListView;

import java.util.ArrayList;

public class EarthquakeActivity extends AppCompatActivity {

    public static final String LOG_TAG = EarthquakeActivity.class.getName();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.earthquake_activity);

        // Create a fake list of earthquake locations.
        ArrayList<Earthquake> earthquakes = new ArrayList<>();
        earthquakes.add( new Earthquake("7.2" ,"San Francisco","Feb 2,2010"));
        earthquakes.add(new Earthquake("8.1","London","Mar 10,2011"));
        earthquakes.add(new Earthquake("5.3","Tokyo","Jan 22,2011"));
        earthquakes.add(new Earthquake("7.7","Mexico City","Apr 29,2012"));
        earthquakes.add(new Earthquake("1.1","Moscow","Dec 10,2013"));
        earthquakes.add(new Earthquake("10.0","Rio de Janeiro","Oct 14,2014"));
        earthquakes.add(new Earthquake("9.2","Paris","Aug 01,2015"));

        // Find a reference to the {@link ListView} in the layout
        ListView earthquakeListView = (ListView) findViewById(R.id.list);


        //create a new Adapter that takes list of earthquake as input
        EarthquakeAdapter adapter=new EarthquakeAdapter(this,earthquakes);

        // Set the adapter on the {@link ListView}
        // so the list can be populated in the user interface
        earthquakeListView.setAdapter(adapter);
    }
}
