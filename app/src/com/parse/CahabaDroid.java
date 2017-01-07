package com.parse;

import org.json.JSONException;
import org.json.JSONObject;

import android.app.Activity;
import android.os.Bundle;
import android.widget.TextView;
import com.loopj.android.http.*;
import org.apache.http.Header;
/**
 * Simplistic demo of using JSON - real example would use network
 * connection and a threading solution
 * @author Rachee Singh
 */
public class CahabaDroid extends Activity {
	
	TextView json;
	

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.main);
        try {
			String jsonString = getJsonString();
			JSONObject jsonObject = new JSONObject(jsonString);
			String name = jsonObject.getString("name"); 
			String age = jsonObject.getString("age");
			String address = jsonObject.getString("address");
			String phone = jsonObject.getString("phone");
			String jsonText=name + "\n" + age + "\n" + address + "\n" + phone;
	    	json= (TextView)findViewById(R.id.json);
	    	json.setText(jsonText);
		} catch (JSONException e) {
			e.printStackTrace();
		}
    }



	public void onStart() {
		// called before request is started
	}


	public void onSuccess(int statusCode, Header[] headers, byte[] response) {
		// called when response HTTP status is "200 OK"
	}


	public void onFailure(int statusCode, Header[] headers, byte[] errorResponse, Throwable e) {
		// called when response HTTP status is "4XX" (eg. 401, 403, 404)
	}


	public void onRetry(int retryNo) {
		// called when request is retried
	}

    /** Mock up some JSON data */
	private String getJsonString() {

		AsyncHttpClient client = new AsyncHttpClient();

		client.get("https://www.google.com", new AsyncHttpResponseHandler() {


		});

		JSONObject string = new JSONObject();
		try {
			string.put("name", "John Doe");
			string.put("age", new Integer(25));
			string.put("address", "75 Ninth Avenue 2nd and 4th Floors New York, NY 10011");
			string.put("phone", "8367667829");
		} catch (JSONException e) {
			e.printStackTrace();
		}
		return string.toString();
	}
}

