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


import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.app.Service;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Bundle;
import android.os.IBinder;

public class ClassesService extends Service {

	final static String ACTION = "NotifyServiceAction";
	final static String STOP_SERVICE = "";
	final static int RQS_STOP_SERVICE = 1;

	NotifyServiceReceiver notifyServiceReceiver;

	private static final int MY_NOTIFICATION_ID=1;
	private NotificationManager notificationManager;
	private Notification myNotification;
	String classNum = "ERROR";

	Bundle extras;
	

	
	@Override
	public void onCreate() {
		// TODO Auto-generated method stub
		notifyServiceReceiver = new NotifyServiceReceiver();
		super.onCreate();
		
		
	}

	@SuppressWarnings("deprecation")
	@Override
	public int onStartCommand(Intent intent, int flags, int startId) {
		// TODO Auto-generated method stub
		
		IntentFilter intentFilter = new IntentFilter();
		intentFilter.addAction(ACTION);
		registerReceiver(notifyServiceReceiver, intentFilter);
		
		classNum = (String)intent.getExtras().get("Num");
		
		getNumClass();

		// Send Notification
		notificationManager = (NotificationManager)getSystemService(Context.NOTIFICATION_SERVICE);
		
		
		myNotification = new Notification.Builder(getApplicationContext())
		.setContentTitle(classNum)
		.setContentText("Please click to see the classes!")
		.setSmallIcon(R.drawable.ic_launcher2)
		.setContentIntent(getPendingIntent())
		.setDefaults(Notification.DEFAULT_ALL)
		.setAutoCancel(true)
		.getNotification();
		
		/*myNotification = new Notification(R.drawable.ic_launcher2,"Class Canceled!",System.currentTimeMillis());
		
		Context context = getApplicationContext();
		
		String notificationTitle = classNum;
		
		String notificationText = "Please click to see the classes!";
		
		//Intent myIntent = new Intent();
		
		//PendingIntent pendingIntent = PendingIntent.getActivity(getBaseContext(),0, myIntent,Intent.FLAG_ACTIVITY_NEW_TASK);
		

		myNotification.defaults |= Notification.DEFAULT_SOUND;

		myNotification.flags |= Notification.FLAG_AUTO_CANCEL;

		myNotification.setLatestEventInfo(context,notificationTitle,notificationText, getPendingIntent());*/

		notificationManager.notify(MY_NOTIFICATION_ID, myNotification);

		return START_NOT_STICKY;

	}
	
	
	
	public PendingIntent getPendingIntent() {
		  return PendingIntent.getActivity(this, 0, new Intent(this,
		    CCActivity.class), 0);
		 }
	
	@Override
	public void onDestroy() {
		// TODO Auto-generated method stub
		this.unregisterReceiver(notifyServiceReceiver);
		super.onDestroy();
	}

	@Override
	public IBinder onBind(Intent arg0) {
		// TODO Auto-generated method stub
		return null;
	}

	public class NotifyServiceReceiver extends BroadcastReceiver{

		@Override
		public void onReceive(Context arg0, Intent arg1) {
			// TODO Auto-generated method stub
			int rqs = arg1.getIntExtra("RQS", 0);
			if (rqs == RQS_STOP_SERVICE){
				stopSelf();
			}
		}
	}
	
	public void getNumClass()
	{    
    	
    	if(classNum == "1")
    	{
    		classNum = classNum + " Class is Canceled!";
    	}
    	else
    		classNum = classNum + " Classes are Canceled!";
	}

}