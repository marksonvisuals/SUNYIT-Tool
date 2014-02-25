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
import java.net.URL;
import java.net.URLConnection;

import com.google.analytics.tracking.android.EasyTracker;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Intent;
import android.graphics.Typeface;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.*;

public class ClassesActivity extends Activity {
	String ccget="";
	TextView t;
    
	
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
        
        setContentView(R.layout.classes);
        
        
        setcc();
        

        
        
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
        
        final Button button = (Button) findViewById(R.id.ccbutton1);
        button.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                setcc();
            }
       });
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
	        	setcc();
	            break;
	        case R.id.clist:
	        	/*Intent switchtoccset = new Intent(ClassesActivity.this, ccTabActivity.class);
	        	startActivity(switchtoccset);
	        	break;*/
	        	Intent switchtoccset = new Intent(ClassesActivity.this, settingActivity.class);
	        	startActivity(switchtoccset);
	        	break;
	    }
	    return true;
	}
	
	public void setcc()
	{
		Log.e("n", " Setting CC");
		t = new TextView(this); 
        t=(TextView)findViewById(R.id.cclist);
        Typeface tf = Typeface.createFromAsset(getAssets(),"fonts/roboto/Roboto-Regular.ttf");
		t.setTypeface(tf);
    	new ccprogress().execute();
    	
	}
	
	
	
	
	public String getClasses(){
		String name="";
	  try {
		URL site = new URL("http://www.marksonvisuals.com/sitapp/cc.php");
		URLConnection yc = site.openConnection();
		BufferedReader out = new BufferedReader(new InputStreamReader(yc.getInputStream()));
		String inputLine;
		while ((inputLine = out.readLine()) != null) {
			name+= inputLine+"\n";
		}
		out.close();
	  } catch (Exception e) {
			e.printStackTrace();
		}
		return name.trim();

	}
	
	class ccprogress extends AsyncTask<Void,Void,Void>
    {
           ProgressDialog dialog=null;
           

       @Override
       protected void onPreExecute() 
       {


               dialog=ProgressDialog.show(ClassesActivity.this,"PLEASE WAIT","LOADING CONTENTS  ..",true);
       }

        @Override
        protected void onPostExecute(Void result) 
        {
                if(dialog.isShowing())
                        {
                           dialog.dismiss();
                           t.setText(ccget);
                        }           
        }

        @Override
        protected Void doInBackground(Void... params) 
        {
        	
        	ccget = getClasses();	
			return null;
        }
        
    }

}




