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

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.Arrays;
import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.app.Service;
import android.appwidget.AppWidgetManager;
import android.appwidget.AppWidgetProvider;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.IBinder;
import android.util.Log;
import android.widget.RemoteViews;
import android.widget.Toast;

public class widgetCC extends AppWidgetProvider {
	
	public static String ACTION_WIDGET_RECEIVER = "ActionReceiverWidget";
	String classes;

	  public void onUpdate(Context context, AppWidgetManager appWidgetManager, int[] appWidgetIds) {
		  
		  
		  
	    final int N = appWidgetIds.length;
	    Log.i("ExampleWidget",  "Updating widgets " + Arrays.asList(appWidgetIds));
	    // Perform this loop procedure for each App Widget that belongs to this
	    // provider
	    for (int i = 0; i < N; i++) {
	      int appWidgetId = appWidgetIds[i];
	      // Create an Intent to launch ExampleActivity
	      Intent intent = new Intent(context, widgetCC.class);
	      intent.setAction(ACTION_WIDGET_RECEIVER);
          intent.putExtra("msg", "Message for Button 1");
	      
	      PendingIntent pendingIntent = PendingIntent.getActivity(context, 0, intent, 0);
	      // Get the layout for the App Widget and attach an on-click listener
	      // to the button
	      RemoteViews views = new RemoteViews(context.getPackageName(), R.layout.widgetcc);
	      
	      views.setOnClickPendingIntent(R.id.widgetccbut, pendingIntent);
	      // To update a label
	      views.setTextViewText(R.id.widget1label, loadnum());
	      // Tell the AppWidgetManager to perform an update on the current app
	      // widget
	      appWidgetManager.updateAppWidget(appWidgetId, views);
	    }
	  }
	  
	  @SuppressWarnings("deprecation")
	public void onReceive(Context context, Intent intent) {

          // v1.5 fix that doesn't call onDelete Action
          final String action = intent.getAction();
          if (AppWidgetManager.ACTION_APPWIDGET_DELETED.equals(action)) {
              final int appWidgetId = intent.getExtras().getInt(
                      AppWidgetManager.EXTRA_APPWIDGET_ID,
                      AppWidgetManager.INVALID_APPWIDGET_ID);
              if (appWidgetId != AppWidgetManager.INVALID_APPWIDGET_ID) {
                  this.onDeleted(context, new int[] { appWidgetId });
              }
          } else {
              // check, if our Action was called
              if (intent.getAction().equals(ACTION_WIDGET_RECEIVER)) {
                  Toast.makeText(context, "blblbl", Toast.LENGTH_SHORT).show();
                  String msg = "null";
                  try {
                      msg = intent.getStringExtra("msg");

                  //  Toast.makeText(context, "test", Toast.LENGTH_SHORT).show();


                  } catch (NullPointerException e) {
                      Log.e("Error", "msg = null");
                  }
                  Toast.makeText(context, "ttttt", Toast.LENGTH_SHORT).show();

                  PendingIntent contentIntent = PendingIntent.getActivity(context, 0, intent, 0);
                  NotificationManager notificationManager = (NotificationManager)context.getSystemService(Context.NOTIFICATION_SERVICE);
				Notification noty = new Notification(R.drawable.refresh, "Button 1 clicked", System.currentTimeMillis());

                  noty.setLatestEventInfo(context, "Notice", msg, contentIntent);
                  notificationManager.notify(1, noty);

              } else {
                  // do nothing
              }

              super.onReceive(context, intent);
          }
      }
	  
	  public static class UpdateService extends Service {
	        @Override
	        public void onStart(Intent intent, int startId) {
	          //process your click here
	        }

			@Override
			public IBinder onBind(Intent arg0) {
				// TODO Auto-generated method stub
				return null;
			}
	    }
	  
	  @SuppressWarnings("unused")
	private BroadcastReceiver HelloReceiver = new BroadcastReceiver(){

		     @Override
		     public void onReceive(Context context, Intent intent) {
		   
		          //do something on receive
		  
		     }
		  
		};
	  
	  
	  
	  public String loadnum(){
		  
		  
		  String line = null;
		  
		  line = new getnum().doInBackground();
		  
		  
		  
		  if(line != "0")
	        {
	        return (line + " Canceled Classes");
	        }
	      else
	        {
	        return("0 Classes Canceled");
	        }
		  	  
	  }
	  
	  class getnum extends AsyncTask<String, Integer, String> { // 
		    // Called to initiate the background activity
		    protected String doInBackground(String... params) { // 
		    	String line = null;
		    	
		    	try{
			        URL url = new URL("http://marksonvisuals.com/sitapp/ccwidget.php");
			        HttpURLConnection conn = (HttpURLConnection) url.openConnection();
			        BufferedReader rd = new BufferedReader(new InputStreamReader(conn.getInputStream()));

			        line = rd.readLine();
			        
			        rd.close();
			        
			        
			        
				     } catch (MalformedURLException e) {
			            e.printStackTrace();
			         }  catch (IOException e) {
			            e.printStackTrace();
			         }
		    	return line;
		    }
	  }
	  
	}