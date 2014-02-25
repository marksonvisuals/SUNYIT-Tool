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

import com.google.analytics.tracking.android.EasyTracker;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.os.Bundle;
import android.provider.Settings;
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;
import android.widget.Toast;

public class deniedActivity extends Activity {

	
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
        setContentView(R.layout.notauth);
        
        View.OnClickListener handler = new View.OnClickListener(){
        	
            public void onClick(View v) {
            
                    
                    switch (v.getId()) {

                    case R.id.wifisettings: 
                        Log.e("n", " WIFI button clicked");
                    	startActivity(new Intent(Settings.ACTION_WIFI_SETTINGS));  
                        break;
                    case R.id.mobilesettings:
                    	Log.e("n", " Mobile button clicked");
                    	roamingmenu();
                    	break;
                    case R.id.recheck:
                    	Log.e("n", " Re-Checking Internet Connection");
                    	yesorno();
                    	break;
                    
                    
           }                

          }
            
        };
        
        
        findViewById(R.id.wifisettings).setOnClickListener(handler);
        findViewById(R.id.mobilesettings).setOnClickListener(handler);
        findViewById(R.id.recheck).setOnClickListener(handler);
	}
	
	public void roamingmenu()
	{
		try
		{
			startActivity(new Intent(Settings.ACTION_DATA_ROAMING_SETTINGS));
		}
		catch (Exception e) {      
			   
            e.printStackTrace();
            
            startActivity(new Intent(Settings.ACTION_SETTINGS));
   
            Toast.makeText(this, "Android will not allow this menu to be called from applications!",Toast.LENGTH_SHORT).show();
		}
	}
	
	public void yesorno()
	{
		if(!CheckInternet())
		{
			Toast.makeText(this, "You do not have internet access!\nPlease activate it and try again!",Toast.LENGTH_SHORT).show();
		}
		else
		{
			finish();
		}
	}
	
	public boolean CheckInternet() 
    {
        ConnectivityManager connec = (ConnectivityManager)getSystemService(Context.CONNECTIVITY_SERVICE);
        android.net.NetworkInfo wifi = connec.getNetworkInfo(ConnectivityManager.TYPE_WIFI);
        android.net.NetworkInfo mobile = connec.getNetworkInfo(ConnectivityManager.TYPE_MOBILE);

        // Here if condition check for wifi and mobile network is available or not.
        // If anyone of them is available or connected then it will return true, otherwise false;

        if (wifi.isConnected()) {
            return true;
        } else if (!mobile.isConnected()) {
            return false;
        } else if (mobile.isConnected()) {
            return true;
        }
        return false;
    }
	
	@Override
	public boolean onKeyDown(int keyCode, KeyEvent event) {
	    if (keyCode == KeyEvent.KEYCODE_BACK) {
	        moveTaskToBack(true);
	        return true;
	    }
	    return super.onKeyDown(keyCode, event);
	}
}
