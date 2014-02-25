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
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.lang.reflect.Constructor;
import java.lang.reflect.Method;
import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.entity.BufferedHttpEntity;
import org.apache.http.impl.client.DefaultHttpClient;

import com.google.analytics.tracking.android.EasyTracker;

import android.app.Activity;
import android.app.PendingIntent;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup.LayoutParams;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.Toast;


public class settingActivity extends Activity {
	String[] crns = new String[10];
	int num = 0;
	String crntext;
	EditText mEdit;
	final int requestID = (int) System.currentTimeMillis();
	
	
	
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
        setContentView(R.layout.settings);
        
        new notifyAsync().execute();
        
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
    
        
        File file = this.getFileStreamPath("settings.dat");
        
        if(file.exists())
        {
        	loadpop();
        }
        
        
        final EditText mEdit   = (EditText)findViewById(R.id.editText1);
        
        View.OnClickListener handler = new View.OnClickListener(){
        	
            public void onClick(View v) {
            
                    
                    switch (v.getId()) {

                    case R.id.crnbut:
                    	
                    	if (mEdit.getText().length() > 3)
                    	{
                    	addnum(mEdit.getText().toString());	
                    	mEdit.setText("");
                    	startws();
                    	}
                    	break;
                    	
           }                

          }
            
        };
        
        
        findViewById(R.id.crnbut).setOnClickListener(handler);
	}
	
	public void full()
	{
		Button but = (Button)findViewById(R.id.crnbut);
		
		if(num < 8)
		{	
			but.setClickable(true);
			but.setAlpha(1);
			
		}
		else
		{
			but.setClickable(false);
			but.setAlpha((float) .5);
		}
		
		
	}
	
	@SuppressWarnings("deprecation")
	public void addnum(String crn)
	{
		crns[num]=crn;
		num++;
		full();
		Button myButton = new Button(this);
		myButton.setText(crn);
		LinearLayout layout = (LinearLayout) findViewById(R.id.crnlayout);
		myButton.setLayoutParams(new LayoutParams(
				android.view.ViewGroup.LayoutParams.FILL_PARENT,
				android.view.ViewGroup.LayoutParams.WRAP_CONTENT));
		myButton.setOnClickListener(new Button.OnClickListener() {
		    public void onClick(View v) {
		    	Button b = (Button)v;
		        String buttonText = b.getText().toString();
		        rembut(buttonText);
		        //v.setEnabled(false);
		        v.setVisibility(View.GONE);
	            
		    }
		});
		layout.addView(myButton);	
		
	}
	
	public void startws()
	{
		
		String dataf = "";
		int w = 0;
		while(crns[w] != null)
		{
			w++;
		}
   	 
   	 for(int i=0; i<w; i++)
   	 {

   			 dataf = dataf + crns[i] + ",";
   	 }
   	 
		WriteSettings(this,dataf);
		//Toast.makeText(this,crns.length,Toast.LENGTH_SHORT).show();
		
	}
	
	public void rembut(String text)
	{
		//Toast.makeText(this,crns.length,Toast.LENGTH_SHORT).show();
		
		int place = -1;
		
		for(int w = 0; w<crns.length; w++)
		{
			if(crns[w] == text)
			{
				place = w;
				break;
			}
		}
		
		
		
		if (place >= 0)
		{
			for(int i=place+1; i<num+1; i++)
			{
				crns[i-1] = crns[i];
			}
				
		}
		
		num--;
		//Toast.makeText(this, "CRN " + text + " removed.",Toast.LENGTH_SHORT).show();
		startws();
		
		full();
		
	}
	
	
	
	public void loadpop()
	{
		
		
		String[] data = ReadSettings(this).split(",");
		
		for(int i = 0; i<data.length-1; i++)
		{
				addnum(data[i].trim());
			
		}
		
	}
	 
	// Save settings
	 
	     public void WriteSettings(Context context, String data){
	 
	    	 
	         FileOutputStream fOut = null;
	 
	         OutputStreamWriter osw = null;
	 
	         
	 
	         try{

	        	 
	 
	          fOut = openFileOutput("settings.dat",MODE_PRIVATE);      
	 
	          osw = new OutputStreamWriter(fOut);
	          
	 
	          osw.write(data);
	 
	          osw.flush();
	 
	          //Toast.makeText(context, "Classes Saved",Toast.LENGTH_SHORT).show();
	 
	          }
	 
	          catch (Exception e) {      
	 
	          e.printStackTrace();
	 
	          Toast.makeText(context, "Settings not saved",Toast.LENGTH_SHORT).show();
	 
	          }
	 
	          finally {
	 
	             try {
	 
	                    osw.close();
	 
	                    fOut.close();
	 
	                    } catch (IOException e) {
	 
	                    e.printStackTrace();
	 
	                    }
	 
	          }
	 
	     }
	     
	  // Read settings
	   
	       public String ReadSettings(Context context){
	   
	           FileInputStream fIn = null;
	   
	           InputStreamReader isr = null;
	   
	           
	   
	           char[] inputBuffer = new char[255];
	   
	           String data = null;
	   
	           
	           
	   
	           try{
	   
	            fIn = openFileInput("settings.dat");
	            
	   
	            isr = new InputStreamReader(fIn);
	   
	            isr.read(inputBuffer);
	   
	            data = new String(inputBuffer);
	   
	            //Toast.makeText(context, "Classes Loaded",Toast.LENGTH_SHORT).show();
	   
	            }
	   
	            catch (Exception e) {      
	   
	            e.printStackTrace();
	   
	            Toast.makeText(context, "Settings not read",Toast.LENGTH_SHORT).show();
	   
	            }
	   
	            finally {
	   
	               try {
	   
	                      isr.close();
	   
	                      fIn.close();
	   
	                      } catch (IOException e) {
	   
	                      e.printStackTrace();
	   
	                      }
	   
	            }
	           
	           		
	                return data;
	   
	       }
	   
	       public void notifications(){
	  		 // get a Calendar object with current time
	  		
	  		Intent intent = new Intent(getApplicationContext(), ClassesService.class);
	  		//intent.addFlags(Intent.FLAG_ACTIVITY_SINGLE_TOP | Intent.FLAG_ACTIVITY_CLEAR_TOP);
	  		PendingIntent pi = PendingIntent.getService(getApplicationContext(), 0, intent, PendingIntent.FLAG_UPDATE_CURRENT);
	  		
	  		try
	  		{
	  			pi.send();
	  		}
	  		catch (PendingIntent.CanceledException e) {  
	  		  // the stack trace isn't very helpful here.  Just log the exception message.  
	  		  System.out.println( "Sending Notification failed " );  
	  		  } 
	  		 /*Calendar calendar = Calendar.getInstance();
	  		 
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
	  	      alarm_manager.setRepeating(AlarmManager.RTC, calendar.getTimeInMillis(),24*60*60*1000, pi);*/
	  	}
	       
	       public static String getCancelNum()
	       {
	    	   String classNum = "0";
	    	   
	   		DefaultHttpClient client = new DefaultHttpClient();
	        try {
	           HttpGet httpGet = new HttpGet("http://marksonvisuals.com/sitapp/ccnum.php");
	           HttpResponse response = client.execute(httpGet);
	           HttpEntity ht = response.getEntity();

	           BufferedHttpEntity buf = new BufferedHttpEntity(ht);

	           InputStream is = buf.getContent();


	           BufferedReader r = new BufferedReader(new InputStreamReader(is));

	           StringBuilder total = new StringBuilder();
	           String line;

				while ((line = r.readLine()) != null) {
				       total.append(line + "\n");
				   }
				classNum = total.toString().replaceAll("[^0-9]","");
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
	           
	        
	       	
	       	return classNum;
	       }
	   
	       class notifyAsync extends AsyncTask<Void, Void, Void> {
	   		ProgressDialog dialog = null;
	   		PendingIntent pi;
	   		Intent intent;

	   		@Override
	   		protected void onPreExecute() {

	   			dialog = ProgressDialog.show(settingActivity.this, "PLEASE WAIT",
	   					"LOADING CONTENTS  ..", true);
	   			
	   			
	   			
	   			intent = new Intent(getApplicationContext(), ClassesService.class);
		  		
		  		
	   		}

	   		@Override
	   		protected void onPostExecute(Void result) {
	   			if (dialog.isShowing()) {
	   				dialog.dismiss();
	   			}
	   		}

	   		@Override
	   		protected Void doInBackground(Void... params) {

   				String cancelNum = settingActivity.getCancelNum();
   				
   				intent.putExtra("Num", cancelNum);
   				

	   				startService(intent);
	   			return null;
	   		}

	   	}
	
}
