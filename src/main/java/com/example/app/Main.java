package com.example.app;

import android.app.Activity;
import android.os.Bundle;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;
import com.jayway.jsonpath.JsonPath;
import com.jayway.jsonpath.PathNotFoundException;
import com.jayway.jsonpath.ReadContext;
import com.jjoe64.graphview.GraphView;
import com.jjoe64.graphview.series.DataPoint;
import com.jjoe64.graphview.series.LineGraphSeries;

import org.json.JSONObject;
//import com.android.volley.RequestQueue;
/**
 * Main activity.
 */
public class Main extends Activity {
    /**
     * Initialize view.
     * @param savedInstanceState - You know ;)
     */
    private RequestQueue queue;
    private TextView mTextView;
    private ReadContext ctx;

  @Override
  public void onCreate(final Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    setContentView(R.layout.main);
    mTextView = (TextView) findViewById(R.id.tMain);
    queue = Volley.newRequestQueue(this);
//      fetchResults("02422500");
//      fetchResults("02423130");
//      fetchResults("02423160");
//      fetchResults("02423380");
//      fetchResults("02423397");
//      fetchResults("02423400");
//      fetchResults("02423414");
//      fetchResults("02423425");
//      fetchResults("02423496");

      addGraph(findViewById(R.id.lMain));
      //addGraph(findViewById(R.id.lMain));
//      fetchResults("02423500");
//      fetchResults("0242354750");
//      fetchResults("02423555");
//      fetchResults("02423630");
//      fetchResults("02424000");
//      fetchResults("02424590");
//      fetchResults("02425000");
  }

  public void fetchResults(String siteCode) {
      String requestUrl = "https://waterservices.usgs.gov/nwis/iv/?format=json&sites=" + siteCode + "&parameterCd=00060,00065,00010&siteStatus=all";
      JsonObjectRequest jsObjRequest = new JsonObjectRequest(requestUrl, null, successListener, failListener);
      queue.add(jsObjRequest);
  }

  final Response.Listener<JSONObject> successListener = new Response.Listener<JSONObject>() {
    @Override
    public void onResponse(JSONObject response) {


        //mTextView.append("----------------- \n");
        String responseString = response.toString();
        ctx = JsonPath.parse(responseString);

        String siteName = tryGetString("['value']['timeSeries'][0]['sourceInfo']['siteName']", "Site Name Unavailable");
        mTextView.append("\n " + siteName + "\n");

        String currMeasurement0 = tryGetString("$.['value']['timeSeries'][0]['variable']['variableName']", "err");
        currMeasurement0 = currMeasurement0.replace("&#179;"," ");
        currMeasurement0 = currMeasurement0.replace("&#176;"," deg ");
        String currVal0 = tryGetString("$.['value']['timeSeries'][0]['values'][0]['value'][0]['value']", "err");
        String currLabel0 = tryGetString("$.['value']['timeSeries'][0]['variable']['unit']['unitCode']", "err");

        mTextView.append(currMeasurement0 + ": " + currVal0 + "  " + currLabel0 + " \n");

        String currMeasurement1 = tryGetString("$.['value']['timeSeries'][1]['variable']['variableName']", "err");
        currMeasurement1 = currMeasurement1.replace("&#179;"," ");
        currMeasurement1 = currMeasurement1.replace("&#176;"," deg ");
        String currVal1 = tryGetString("$.['value']['timeSeries'][1]['values'][0]['value'][0]['value']", "err");
        String currLabel1 = tryGetString("$.['value']['timeSeries'][1]['variable']['unit']['unitCode']", "err");

        mTextView.append(currMeasurement1 + ": " + currVal1 + "  " + currLabel1 + " \n");

        if (nodeExists("$.['value']['timeSeries'][2]['variable']['variableName']")) {

            String currMeasurement2 = tryGetString("$.['value']['timeSeries'][2]['variable']['variableName']", "err");
            currMeasurement2 = currMeasurement2.replace("&#179;"," ");
            currMeasurement2 = currMeasurement2.replace("&#176;"," deg ");
            String currVal2 = tryGetString("$.['value']['timeSeries'][2]['values'][0]['value'][0]['value']", "err");
            String currLabel2 = tryGetString("$.['value']['timeSeries'][2]['variable']['unit']['unitCode']", "err");

            mTextView.append(currMeasurement2 + ": " + currVal2 + "  " + currLabel2 + " \n");

        }


    }
  };

    private void addGraph(View v) {

        View view = v; // returns base view of the fragment
        if (view == null)
            return;
        if (!(view instanceof ViewGroup))
            return;

        ViewGroup viewGroup = (ViewGroup) view;
        GraphView graph = (GraphView) View.inflate(this, R.layout.graphwidget, viewGroup);

        //GraphView graph = (GraphView) popup;

        //GraphView graph = (GraphView) findViewById(R.id.graph);
        LineGraphSeries<DataPoint> series = new LineGraphSeries<>(new DataPoint[] {
            new DataPoint(0, 1),
            new DataPoint(1, 5),
            new DataPoint(2, 3),
            new DataPoint(3, 2),
            new DataPoint(4, 6)
        });
        graph.addSeries(series);
    }

    private String tryGetString(String jsonPath, String defaultValue) {
        try {
            return ctx.read(jsonPath);
        }
        catch(PathNotFoundException e) {
            return defaultValue;
        }
    }

    private Boolean nodeExists(String jsonPath) {
        try {
            ctx.read(jsonPath);
            return true;
        }
        catch(PathNotFoundException e) {
            return false;
        }

    }

  final Response.ErrorListener failListener = new Response.ErrorListener() {
    @Override
    public void onErrorResponse(VolleyError error) {
      mTextView.setText("That didn't work!\n" +
        "Error: " + error + "\n" +
        "Detail:" + error.getMessage() + "\n" +
        "Cause: " + error.getCause());
      error.printStackTrace();
    }
  };


}
