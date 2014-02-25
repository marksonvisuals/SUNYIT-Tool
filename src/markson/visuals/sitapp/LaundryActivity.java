	/*SUNYIT TOOL Andoid Application
    Copyright (C) 2014  Eric Markson (eric@marksonvisuals.com)

    This program is free software: you can redistribute it and/or modify
    it under the terms of the GNU General Public License as published by
    the Free Software Foundation, either version 3 of the License, or
    (at your option) any later version.

    This program is distributed in the hope that it will be useful,
    but WITHOUT ANY WARRANTY; without even the implied warranty of
    MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
    GNU General Public License for more details.

    You should have received a copy of the GNU General Public License
    along with this program.  If not, see <http://www.gnu.org/licenses/>.*/
	
package markson.visuals.sitapp;

import java.io.*;
import java.lang.reflect.Constructor;
import java.lang.reflect.Method;
import java.net.*;

import com.google.analytics.tracking.android.EasyTracker;

import android.net.ConnectivityManager;
import android.net.wifi.WifiInfo;
import android.net.wifi.WifiManager;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.app.Activity;
import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;

public class LaundryActivity extends Activity {

	String adkn, adks, mhk, ork1, ork2, data,datae = "";
	int adkne, adkse, mhke, ork1e, ork2e = 0;
	TextView adknt, adkst, mhkt, orkt, ork2t;

	// Typeface tf =
	// Typeface.createFromAsset(getAssets(),"fonts/roboto/Roboto-Regular.ttf");

	
	@Override
	  public void onStart() {
	    super.onStart();
	    EasyTracker.getInstance().activityStart(this); // Add this method.
	  }

	  @Override
	  public void onStop() {
	    super.onStop();
	    EasyTracker.getInstance().activityStop(this); // Add this method.
	  }
	
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.laundry);

		try {
			Class<?> strictModeClass = Class.forName("android.os.StrictMode",
					true, Thread.currentThread().getContextClassLoader());

			Class<?> threadPolicyClass = Class.forName(
					"android.os.StrictMode$ThreadPolicy", true, Thread
							.currentThread().getContextClassLoader());

			Class<?> threadPolicyBuilderClass = Class.forName(
					"android.os.StrictMode$ThreadPolicy$Builder", true, Thread
							.currentThread().getContextClassLoader());

			Method setThreadPolicyMethod = strictModeClass.getMethod(
					"setThreadPolicy", threadPolicyClass);

			Method detectAllMethod = threadPolicyBuilderClass
					.getMethod("detectAll");
			Method penaltyMethod = threadPolicyBuilderClass
					.getMethod("penaltyLog");
			Method buildMethod = threadPolicyBuilderClass.getMethod("build");

			Constructor<?> threadPolicyBuilderConstructor = threadPolicyBuilderClass
					.getConstructor();
			Object threadPolicyBuilderObject = threadPolicyBuilderConstructor
					.newInstance();

			Object obj = detectAllMethod.invoke(threadPolicyBuilderObject);

			obj = penaltyMethod.invoke(obj);
			Object threadPolicyObject = buildMethod.invoke(obj);
			setThreadPolicyMethod.invoke(strictModeClass, threadPolicyObject);

		} catch (Exception ex) {
			Log.w("n", "Strict not enabled....");
		}

		Log.e("n", " LV Activity Started");

		getLv();

		final Button button = (Button) findViewById(R.id.button1);
		button.setOnClickListener(new View.OnClickListener() {
			public void onClick(View v) {
				getLv();
			}
		});
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		MenuInflater inflater = getMenuInflater();
		inflater.inflate(R.layout.lvmenu, menu);
		Log.e("n", " LV Menu Created");
		return true;
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		switch (item.getItemId()) {
		case R.id.back:
			Log.e("n", " LV Menu Back Button Pressed");
			finish();
			break;
		case R.id.refresh:
			getLv();
			break;
		}
		return true;
	}

	/*
	 * public String lv(){ String name=""; try { URL site = new
	 * URL("http://www.laundryview.com/lvs.php"); URLConnection yc =
	 * site.openConnection(); BufferedReader out = new BufferedReader(new
	 * InputStreamReader(yc.getInputStream())); String inputLine; while
	 * ((inputLine = out.readLine()) != null) { name+= inputLine+"\n"; }
	 * out.close(); } catch (Exception e) { e.printStackTrace(); } return
	 * name.trim();
	 * 
	 * }
	 */

	public String loaddata() {
		String name = "";
		lvorno();
		try {
			URL site = new URL("http://www.laundryview.com");
			URLConnection yc = site.openConnection();
			BufferedReader out = new BufferedReader(new InputStreamReader(
					yc.getInputStream()));
			String inputLine;
			while ((inputLine = out.readLine()) != null) {
				name += inputLine + "\n";
			}
			name = getStringBetween(name, "<div id=\"campus1\">",
					"<script type=\"text/javascript\">");
			out.close();
		} catch (Exception e) {
			e.printStackTrace();
		}
		data = name;
		return data;
	}

	private void lvorno() {
		String student = "Student";
		boolean tolv = false;
		ConnectivityManager conMgr = (ConnectivityManager) getSystemService(Activity.CONNECTIVITY_SERVICE);
		boolean wifi = conMgr.getNetworkInfo(ConnectivityManager.TYPE_WIFI)
				.isConnected();

		WifiManager wifiManager = (WifiManager) getSystemService(Context.WIFI_SERVICE);
		WifiInfo info = wifiManager.getConnectionInfo();

		if (wifi == true) {
			if (info.getSSID().equals(student)) {

				Log.e("n", " Student Network at SUNYIT");
				tolv = true;
			}

		}

		if (tolv == false) {
			popup();
			
		} else {
			Log.e("n", " Continue to load");
			

		}
	}
	
	private void popup() {

		AlertDialog.Builder helpBuilder = new AlertDialog.Builder(this);
		helpBuilder.setTitle("FEATURE ISN'T AVAILBLE");
		helpBuilder
				.setMessage("You need to be connected to the campus network \"Student\" in order to view this." +
						"\n\n This may change in a future update!");
		helpBuilder.setPositiveButton("Ok",
				new DialogInterface.OnClickListener() {

					public void onClick(DialogInterface dialog, int which) {
						Intent switchtohome = new Intent(LaundryActivity.this,
								SitappActivity.class);
						Log.e("n", " Disconnected button clicked");
						startActivity(switchtohome);
					}
				});

		// Remember, create doesn't show the dialog
		AlertDialog helpDialog = helpBuilder.create();
		helpDialog.show();
	}

	public void getLv() {
		adknt = new TextView(this);
		adkst = new TextView(this);
		mhkt = new TextView(this);
		orkt = new TextView(this);
		ork2t = new TextView(this);

		mhkt = (TextView) findViewById(R.id.textView2);
		adknt = (TextView) findViewById(R.id.TextView02);
		adkst = (TextView) findViewById(R.id.TextView04);
		orkt = (TextView) findViewById(R.id.TextView06);
		ork2t = (TextView) findViewById(R.id.TextView08);
		/*
		 * adknt.setTypeface(tf); adkst.setTypeface(tf); mhkt.setTypeface(tf);
		 * orkt.setTypeface(tf); ork2t.setTypeface(tf);
		 */

		new lvprogress().execute();

	}
	
	public String loaderror() {
		String err = " ";
		try {
			URL site = new URL("http://www.laundryview.com/about.php");
			URLConnection yc = site.openConnection();
			BufferedReader out = new BufferedReader(new InputStreamReader(
					yc.getInputStream()));
			String inputLine;
			while ((inputLine = out.readLine()) != null) {
				err += inputLine + "\n";
			}
			out.close();
		} catch (Exception e) {
			e.printStackTrace();
		}

		return err;
	}

	public String adkn(String name, String namee) {
		String out="";	
		namee = getStringBetween(namee,
				"<a href=\"laundry_room.php?lr=407062\"", "<br>");
		
		if(namee.contains("OFFLINE"))
		{
			out="(OFFLINE)";
		}
		else
		{
		try {
				name = getStringBetween(name,
						"<a href=\"laundry_room.php?lr=407062\"", "<br>");
				out = getStringBetween(name, "user-avail\">", "</span>");
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
		return out.trim();
	}

	public String adks(String name, String namee) {
		String out="";
		namee = getStringBetween(namee,
				"<a href=\"laundry_room.php?lr=407063\"", "<br>");
		if(namee.contains("OFFLINE"))
		{
			out="(OFFLINE)";
		}
		else
		{
			try {
				name = getStringBetween(name,
						"<a href=\"laundry_room.php?lr=407063\"", "<br>");
				out = getStringBetween(name, "user-avail\">", "</span>");
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
			return out.trim();
	}

	public String mhk(String name, String namee) {
		String out="";
		namee = getStringBetween(namee,
				"<a href=\"laundry_room.php?lr=407061\"", "<br>");
		if(namee.contains("OFFLINE"))
		{
			out="(OFFLINE)";
		}
		else
		{
			try {
				name = getStringBetween(name,
						"<a href=\"laundry_room.php?lr=407061\"", "<br>");
				out = getStringBetween(name, "user-avail\">", "</span>");
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
			return out.trim();
	}

	public String ork1(String name, String namee) {
		String out="";
		namee = getStringBetween(namee,
				"<a href=\"laundry_room.php?lr=407064\"", "<br>");
		if(namee.contains("OFFLINE"))
		{
			out="(OFFLINE)";
		}
		else
		{
			try {
				name = getStringBetween(name,
						"<a href=\"laundry_room.php?lr=407064\"", "<br>");
				out = getStringBetween(name, "user-avail\">", "</span>");
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
			return out.trim();

	}

	public String ork2(String name, String namee) {
		String out="";
		namee = getStringBetween(namee,
				"<a href=\"laundry_room.php?lr=407065\"", "<br>");
		if(namee.contains("OFFLINE"))
		{
			out="(OFFLINE)";
		}
		else
		{
			try {
				name = getStringBetween(name,
						"<a href=\"laundry_room.php?lr=407065\"", "<br>");
				out = getStringBetween(name, "user-avail\">", "</span>");
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
			return out.trim();

	}

	public static String getStringBetween(String content, String start,
			String end) {
		return content.substring(content.indexOf(start) + start.length(),
				content.indexOf(end, content.indexOf(start) + start.length()));
	}

	class lvprogress extends AsyncTask<Void, Void, Void> {
		ProgressDialog dialog = null;

		@Override
		protected void onPreExecute() {

			dialog = ProgressDialog.show(LaundryActivity.this, "PLEASE WAIT",
					"LOADING CONTENTS  ..", true);
		}

		@Override
		protected void onPostExecute(Void result) {
			if (dialog.isShowing()) {
				adknt.setText(adkn);
				adkst.setText(adks);
				mhkt.setText(mhk);
				orkt.setText(ork1);
				ork2t.setText(ork2);
				dialog.dismiss();

			}
		}

		@Override
		protected Void doInBackground(Void... params) {
			data=loaddata();
			datae=loaderror();
			adkn = adkn(data,datae);
			adks = adks(data,datae);
			mhk = mhk(data,datae);
			ork1 = ork1(data,datae);
			ork2 = ork2(data,datae);
			return null;
		}

	}

}
