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

import java.lang.reflect.Constructor;
import java.lang.reflect.Method;
import java.util.Calendar;

import com.google.analytics.tracking.android.EasyTracker;

import android.app.Activity;
import android.app.AlarmManager;
import android.app.AlertDialog;
import android.app.PendingIntent;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager.NameNotFoundException;
import android.graphics.Typeface;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;


import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.Uri;
import android.net.wifi.*;

/*
 * TODO LIST:
 * Laundry Checklist Notifications
 * Canceled Classes notification each day (3 canceled today)
 * Specific Canceled Class Notification. Checks for it.
 */

public class SitappActivity<ThisApp> extends Activity {
	/** Called when the activity is first created. */
	Intent switchtocc, switchtobw, switchtoang, denied, switchtonews,
			switchtocal = null;
	//private AdView adView;
	String adid = "a14f25fea89b84a";
	TextView vers;

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
		setContentView(R.layout.main);
		// StrictMode.ThreadPolicy policy = new
		// StrictMode.ThreadPolicy.Builder().permitAll().build();
		// StrictMode.setThreadPolicy(policy);
		
		//NOTIFICATION ASYNC
		//new notifyAsync().execute();

		
		vers = (TextView) findViewById(R.id.vers);
		Typeface tf = Typeface.createFromAsset(getAssets(),"fonts/roboto/Roboto-Regular.ttf");
		vers.setTypeface(tf);
		vers.setText("Version: " + getSoftwareVersion());

		if (!CheckInternet()) {
			denied = new Intent(SitappActivity.this, deniedActivity.class);
			startActivity(denied);
		}

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

		//notifications();
		
		View.OnClickListener handler = new View.OnClickListener() {

			public void onClick(View v) {

				switch (v.getId()) {

				case R.id.ccbutton:
					Log.e("n", " CC button clicked");
					switchtocc = new Intent(SitappActivity.this,
							CCActivity.class);
					startActivity(switchtocc);
					break;
				case R.id.lvbutton:
					lvorno();
					break;
				case R.id.bwbutton:
					Log.e("n", " Banner Web Clicked");
					switchtobw = new Intent(SitappActivity.this,
							bannerActivity.class);
					startActivity(switchtobw);
					break;
				case R.id.apbutton:
					Log.e("n", " Angel Portal Clicked");
					switchtoang = new Intent(SitappActivity.this,
							angelActivity.class);
					startActivity(switchtoang);
					break;
				case R.id.newsbutton:
					Log.e("n", " News Clicked");
					switchtonews = new Intent(SitappActivity.this,
							newsActivity.class);
					startActivity(switchtonews);
					break;
				case R.id.calbutton:
					Log.e("n", " Calendar Clicked");
					//switchtocal = new Intent(SitappActivity.this,RSSReader.class);
					switchtocal = new Intent(SitappActivity.this, eventActivity.class);
					startActivity(switchtocal);
					break;
				case R.id.devblogbut:
					Log.e("n", " GitHub Button Clicked");
					/*Intent switchtoblog = new Intent(SitappActivity.this, blogActivity.class);
		        	startActivity(switchtoblog);*/
					Intent intent = new Intent(Intent.ACTION_VIEW, 
						     Uri.parse("https://github.com/marksonvisuals/SUNYIT-Tool"));
						startActivity(intent);
		        	break;

				}

			}

		};

		findViewById(R.id.ccbutton).setOnClickListener(handler);
		findViewById(R.id.lvbutton).setOnClickListener(handler);
		findViewById(R.id.bwbutton).setOnClickListener(handler);
		findViewById(R.id.apbutton).setOnClickListener(handler);
		findViewById(R.id.newsbutton).setOnClickListener(handler);
		findViewById(R.id.calbutton).setOnClickListener(handler);
		findViewById(R.id.devblogbut).setOnClickListener(handler);
		

	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
	    MenuInflater inflater = getMenuInflater();
	    inflater.inflate(R.layout.mainmenu, menu);
	    Log.e("n", " Main Menu Created");
	    return true;
	}
	

	public void notifications(){
		 // get a Calendar object with current time
		
		Intent intent = new Intent(SitappActivity.this, ClassesService.class);
		PendingIntent pi = PendingIntent.getService(SitappActivity.this, 0, intent, 0);
		
		
		 Calendar calendar = Calendar.getInstance();
		 
         calendar.set(Calendar.HOUR_OF_DAY, 07);
         calendar.set(Calendar.MINUTE, 00);
         calendar.set(Calendar.SECOND, 00);
		 // add 5 minutes to the calendar object
		 //cal.add(Calendar.SECOND, 10);
		 Log.e("Testing", "Calender Set time:"+calendar.getTime());
		 
		 //Log.e("Testing", "Intent created");
		 //intent.putExtra("alarm_message", "O'Doyle Rules!");
		 // In reality, you would want to have a static variable for the request code instead of 192837
		 
		 AlarmManager alarm_manager = (AlarmManager)getSystemService(Context.ALARM_SERVICE);
	      alarm_manager.setRepeating(AlarmManager.RTC, calendar.getTimeInMillis(),24*60*60*1000, pi);
	}
	
	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
	    switch (item.getItemId()) {
	        case R.id.blog:
	        	Intent switchtoblog = new Intent(SitappActivity.this, blogActivity.class);
	        	startActivity(switchtoblog);
	        	break;
	        /*case R.id.cloud:
	        	Intent switchtocloud = new Intent(SitappActivity.this, Push.class);
	        	startActivity(switchtocloud);
	        	break;*/
	    }
	    return true;
	}
	
	public boolean isConnected() {
		ConnectivityManager cm = (ConnectivityManager) getSystemService(Activity.CONNECTIVITY_SERVICE);
		NetworkInfo info = cm.getActiveNetworkInfo();
		return (info != null && info.isConnected());
	}

	public boolean CheckInternet() {
		ConnectivityManager connec = (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);
		android.net.NetworkInfo wifi = connec
				.getNetworkInfo(ConnectivityManager.TYPE_WIFI);
		android.net.NetworkInfo mobile = connec
				.getNetworkInfo(ConnectivityManager.TYPE_MOBILE);

		// Here if condition check for wifi and mobile network is available or
		// not.
		// If anyone of them is available or connected then it will return true,
		// otherwise false;

		if (wifi.isConnected()) {
			return true;
		} else if (!mobile.isConnected()) {
			return false;
		} else if (mobile.isConnected()) {
			return true;
		}
		return false;
	}

	private void lvorno() {
		String student = "Student";
		boolean tolv = false;
		ConnectivityManager conMgr = (ConnectivityManager) getSystemService(Activity.CONNECTIVITY_SERVICE);
		boolean wifi = conMgr.getNetworkInfo(ConnectivityManager.TYPE_WIFI).isConnected();

		WifiManager wifiManager = (WifiManager) getSystemService(Context.WIFI_SERVICE);
		WifiInfo info = wifiManager.getConnectionInfo();
		if (wifi) {
			if (info.getSSID().equals(student)) {
				
				Log.e("n", " Student Network at SUNYIT");
				tolv = true;
			}

		}

		if (tolv) {
			Intent switchtolv = new Intent(SitappActivity.this,
					LaundryActivity.class);
			Log.e("n", " LV button clicked");
			startActivity(switchtolv);
		} else {
			Log.e("n", " Not connected to SUNYIT Network");
			popup();

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
						// Do nothing but close the dialog
					}
				});

		// Remember, create doesn't show the dialog
		AlertDialog helpDialog = helpBuilder.create();
		helpDialog.show();
	}

	/*
	 * public String gotoLV(){ String doit; WifiManager wifiManager =
	 * (WifiManager) getSystemService(Context.WIFI_SERVICE); WifiInfo info =
	 * wifiManager.getConnectionInfo(); String ssid = info.getSSID();
	 * 
	 * if (ssid == "Student") { doit = "yes"; } else doit = "no";
	 * 
	 * return doit;
	 * 
	 * }
	 */

	@Override
	protected void onDestroy() {
		super.onDestroy();

		unbindDrawables(findViewById(R.id.RelativeLayout1));
		System.gc();
	}

	private void unbindDrawables(View view) {
		if (view.getBackground() != null) {
			view.getBackground().setCallback(null);
		}
		if (view instanceof ViewGroup) {
			for (int i = 0; i < ((ViewGroup) view).getChildCount(); i++) {
				unbindDrawables(((ViewGroup) view).getChildAt(i));
			}
			((ViewGroup) view).removeAllViews();
		}
	}

	private String getSoftwareVersion() {
		PackageInfo pi;
		try {
			pi = getPackageManager().getPackageInfo(getPackageName(), 0);
			return pi.versionName;
		} catch (final NameNotFoundException e) {
			return "na";
		}
	}
	
//	class notifyAsync extends AsyncTask<Void, Void, Void> {
//		ProgressDialog dialog = null;
//
//		@Override
//		protected void onPreExecute() {
//
//			dialog = ProgressDialog.show(SitappActivity.this, "PLEASE WAIT",
//					"LOADING CONTENTS  ..", true);
//		}
//
//		@Override
//		protected void onPostExecute(Void result) {
//			if (dialog.isShowing()) {
//				dialog.dismiss();
//
//			}
//		}
//
//		@Override
//		protected Void doInBackground(Void... params) {
//
//			notifications();
//			return null;
//		}
//
//	}

}
