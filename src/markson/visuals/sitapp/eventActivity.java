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

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.Locale;
import java.util.TimeZone;

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
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ListAdapter;
import android.widget.ListView;
import android.widget.SimpleAdapter;
import android.widget.TextView;

public class eventActivity extends ListActivity {

	ArrayList<HashMap<String, String>> mylist = new ArrayList<HashMap<String, String>>();
	
	JSONObject json;

	String description,title,link,name,date;

	
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
		setContentView(R.layout.eventlist);
		new x2jprogress().execute();
		//json();
		
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

	public void addtocal() throws ParseException {
		 String location=null;
		

		
		DateFormat formatter = new SimpleDateFormat("EEE, dd MMM yyyy HH:mm:ss",Locale.US);
		formatter.setTimeZone(TimeZone.getTimeZone("America/New_York"));
		Date StartTime = formatter.parse(date);
		Log.e("n", String.valueOf(StartTime.getTime()));   
		
		Intent calint = new Intent(Intent.ACTION_INSERT);
		calint.setType("vnd.android.cursor.item/event");
        calint.putExtra("title", name);
        calint.putExtra("description", description);
        calint.putExtra("beginTime", StartTime.getTime());
        //calint.putExtra(Events.EVENT_TIMEZONE, "America/New_York");
        
        if (description.contains("at")) {

			location = description.substring(
					description.lastIndexOf("at") + 3,
					description.length());
			calint.putExtra("eventLocation", location);
		}
        
		startActivity(calint);



				
	}

	public void parse()
	{
		

		try {

			JSONArray earthquakes = json.getJSONArray("items");

			for (int i = 0; i < earthquakes.length(); i++) {
				HashMap<String, String> map = new HashMap<String, String>();
				JSONObject e = earthquakes.getJSONObject(i);

				map.put("id", String.valueOf(i));
				map.put("name", e.getString("title"));
				map.put("description", e.getString("description"));
				map.put("link", e.getString("link"));
				map.put("pubDate", e.getString("pubDate"));
				mylist.add(map);
			}
		} catch (JSONException e) {
			Log.e("log_tag", "Error parsing data " + e.toString());
		}
		json();
	}
	
	public void json() {
		

		

		ListAdapter adapter = new SimpleAdapter(this, mylist,
				R.layout.jsontest, new String[] { "name", "description" },
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
				
				
				date = o.get("pubDate");
				link = o.get("link");
				description = o.get("description");
				name = o.get("name");
				download();
			}
		});
	}

	public void download() {
		AlertDialog alertDialog = new AlertDialog.Builder(eventActivity.this)
				.setTitle(name)
				.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
					public void onClick(DialogInterface dialog, int which) {

						dialog.dismiss();

					}
				})
				.setNeutralButton("Open Link", new DialogInterface.OnClickListener() {
					public void onClick(DialogInterface dialog, int which) {

						Log.i("Opening", link);
						Intent intent = new Intent(Intent.ACTION_VIEW, Uri
								.parse(link));
						startActivity(intent);

					}
				})
				.setPositiveButton("Add to Calendar", new DialogInterface.OnClickListener() {
					public void onClick(DialogInterface dialog, int which) {

						Log.i("Adding", name);
						try {
							addtocal();
						} catch (ParseException e) {
							// TODO Auto-generated catch block
							e.printStackTrace();
						}

					}
				})
				.setMessage(description + "\n\n" + "\n\n What would you like to do?")
				.create();
		/*alertDialog.setTitle(name);
		alertDialog.setMessage(description + "\n\n" + "\n\n What would you like to do?")

		alertDialog.setButton(1,"Cancel", new DialogInterface.OnClickListener() {
			public void onClick(DialogInterface dialog, int which) {

				dialog.dismiss();

			}
		});
		alertDialog.setButton(2,"Open Link", new DialogInterface.OnClickListener() {
					public void onClick(DialogInterface dialog, int which) {

						Log.i("Opening", link);
						Intent intent = new Intent(Intent.ACTION_VIEW, Uri
								.parse(link));
						startActivity(intent);

					}
				});
		alertDialog.setButton(3,"Add to Calendar", new DialogInterface.OnClickListener() {
					public void onClick(DialogInterface dialog, int which) {

						Log.i("Adding", name);
						try {
							addtocal();
						} catch (ParseException e) {
							// TODO Auto-generated catch block
							e.printStackTrace();
						}

					}
				});*/
		

		alertDialog.show();

	}

	class x2jprogress extends AsyncTask<Void, Void, Void> {
		ProgressDialog dialog = null;

		@Override
		protected void onPreExecute() {

			dialog = ProgressDialog.show(eventActivity.this, "PLEASE WAIT",
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
			
			json = JSONfunctions.getJSONfromURL("http://www.marksonvisuals.com/sitapp/x2j.php?url=http://www.sunyit.edu/apps/calendar/export.php?type=rss");
			return null;
		}

	}

}
