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
import java.util.ArrayList;
import java.util.HashMap;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import com.google.analytics.tracking.android.EasyTracker;

import android.app.AlertDialog;
import android.app.ListActivity;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Typeface;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ListAdapter;
import android.widget.ListView;
import android.widget.SimpleAdapter;
import android.widget.TextView;

public class CCActivity extends ListActivity {

	ArrayList<HashMap<String, String>> mylist = new ArrayList<HashMap<String, String>>();
	
	JSONObject json;

	String cclass,course,num,crn,instructor, pubDate, calid,date,time,fdate;

	
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
	
	/** Called when the activity is first created. */
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.classlist);
		new x2jprogress().execute();
		//json();
		Log.e("n", " CC Activity Started");
        try {
            Class<?> strictModeClass = Class.forName("android.os.StrictMode", true, Thread.currentThread()
                    .getContextClassLoader());

            Class<?> threadPolicyClass = Class.forName("android.os.StrictMode$ThreadPolicy", true, Thread
                    .currentThread().getContextClassLoader());

            Class<?> threadPolicyBuilderClass = Class.forName("android.os.StrictMode$ThreadPolicy$Builder", true,
                    Thread.currentThread().getContextClassLoader());

            Method setThreadPolicyMethod = strictModeClass.getMethod("setThreadPolicy", threadPolicyClass);

            Method detectAllMethod = threadPolicyBuilderClass.getMethod("detectAll");
            Method penaltyMethod = threadPolicyBuilderClass.getMethod("penaltyLog");
            Method buildMethod = threadPolicyBuilderClass.getMethod("build");

            Constructor<?> threadPolicyBuilderConstructor = threadPolicyBuilderClass.getConstructor();
            Object threadPolicyBuilderObject = threadPolicyBuilderConstructor.newInstance();

            Object obj = detectAllMethod.invoke(threadPolicyBuilderObject);

            obj = penaltyMethod.invoke(obj);
            Object threadPolicyObject = buildMethod.invoke(obj);
            setThreadPolicyMethod.invoke(strictModeClass, threadPolicyObject);

        } catch (Exception ex) {
            Log.w("n", "Strict not enabled....");
        }
		
		View.OnClickListener handler = new View.OnClickListener() {

			public void onClick(View v) {

				switch (v.getId()) {

				case R.id.eventbut:
					Log.e("n", " reload events");
					new x2jprogress().execute();
					break;

				}

			}

		};

		findViewById(R.id.eventbut).setOnClickListener(handler);


	}
	
	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
	    MenuInflater inflater = getMenuInflater();
	    inflater.inflate(R.layout.ccmenu, menu);
	    Log.e("n", " CC Menu Created");
	    return true;
	}
	

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
	    switch (item.getItemId()) {
	        case R.id.ccback:
	        	Log.e("n", " CC Menu Back Button Pressed");
	        	finish();
	            break;
	        case R.id.ccrefresh:
	        	new x2jprogress().execute();
	            break;
	        case R.id.clist:
	        	/*Intent switchtoccset = new Intent(ClassesActivity.this, ccTabActivity.class);
	        	startActivity(switchtoccset);
	        	break;*/
	        	Intent switchtoccset = new Intent(CCActivity.this, settingActivity.class);
	        	startActivity(switchtoccset);
	        	break;
	    }
	    return true;
	}

	public void parse()
	{
		

		try {

			JSONArray classes = json.getJSONArray("items");

			for (int i = 0; i < classes.length(); i++) {
				HashMap<String, String> map = new HashMap<String, String>();
				JSONObject e = classes.getJSONObject(i);

				map.put("id", String.valueOf(i));
				map.put("crn", e.getString("crn"));
				map.put("num", e.getString("num"));
				map.put("cclass", e.getString("title"));
				map.put("date", e.getString("pubDate"));
				map.put("instructor", e.getString("instructor"));
				map.put("time", e.getString("time"));
				map.put("fdate", e.getString("fdate"));
				mylist.add(map);
			}
		} catch (JSONException e) {
			Log.e("log_tag", "Error parsing data " + e.toString());
		}
		json();
	}
	
	public void json() {
		

		

		ListAdapter adapter = new SimpleAdapter(this, mylist,
				R.layout.jsontest, new String[] { "cclass", "fdate" },
				new int[] { R.id.item_title, R.id.item_subtitle });

		setListAdapter(adapter);

		final ListView lv = getListView();
		lv.setTextFilterEnabled(true);
		lv.setOnItemClickListener(new OnItemClickListener() {
			public void onItemClick(AdapterView<?> parent, View view,
					int position, long id) {
				@SuppressWarnings("unchecked")
				HashMap<String, String> o = (HashMap<String, String>) lv
						.getItemAtPosition(position);
				// Toast.makeText(eventActivity.this,"ID '" + o.get("link") +
				// "' was clicked.",Toast.LENGTH_SHORT).show();
				
				TextView title = (TextView) findViewById(R.id.item_title);
				Typeface tf = Typeface.createFromAsset(getAssets(),"fonts/roboto/Roboto-Regular.ttf");
				title.setTypeface(tf);
				
				TextView subtitle = (TextView) findViewById(R.id.item_subtitle);
				subtitle.setTypeface(tf);
				
				
				date = o.get("date");
				cclass = o.get("cclass");
				num = o.get("num");
				instructor = o.get("instructor");
				crn = o.get("crn");
				fdate = o.get("date");
				time = o.get("time");
				download();
			}
		});
	}

	@SuppressWarnings("deprecation")
	public void download() {
		AlertDialog alertDialog = new AlertDialog.Builder(CCActivity.this)
				.create();
		alertDialog.setTitle(cclass);
		alertDialog.setMessage("Class: " + num + "\n" + "Professor: " + instructor + "\n" + "Time: " + time + "\n" + "Date: " + date + "\n"  + "CRN: " + crn + "\n");
		
		alertDialog.setButton("Close", new DialogInterface.OnClickListener() {
			public void onClick(DialogInterface dialog, int which) {

				dialog.dismiss();

			}
		});

		alertDialog.show();

	}

	class x2jprogress extends AsyncTask<Void, Void, Void> {
		ProgressDialog dialog = null;

		@Override
		protected void onPreExecute() {

			dialog = ProgressDialog.show(CCActivity.this, "PLEASE WAIT",
					"LOADING CONTENTS  ..", true);
		}

		@Override
		protected void onPostExecute(Void result) {
			if (dialog.isShowing()) {
				dialog.dismiss();
				parse();

			}
		}

		@Override
		protected Void doInBackground(Void... params) {

			// ccget = getClasses();
			
			if(!mylist.isEmpty())
			{
				mylist.clear();
			}
			
			json = JSONfunctions.getJSONfromURL("http://marksonvisuals.com/sitapp/cc2.php");
			return null;
		}

	}

}
