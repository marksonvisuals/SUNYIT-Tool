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

import com.google.analytics.tracking.android.EasyTracker;

import android.app.Activity;
import android.os.Bundle;
import android.util.Log;
import android.view.KeyEvent;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.webkit.WebView;
import android.webkit.WebViewClient;

public class blogActivity extends Activity {
		WebView mWebView;
		
		
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
	    protected void onCreate(Bundle savedInstanceState) {
	        super.onCreate(savedInstanceState);	        
			setContentView(R.layout.blogweb);
			
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
			
			mWebView = (WebView) findViewById(R.id.blogview);
		    mWebView.getSettings().setJavaScriptEnabled(true);
		    mWebView.loadUrl("http://www.marksonvisuals.com/blog/");
		    
		    mWebView.setWebViewClient(new WebViewClient()
		    {
		    	@Override
			    public boolean shouldOverrideUrlLoading(WebView view, String url) {
			        view.loadUrl(url);
			        return true;
			    }
		    });
		}

		
		@Override
		public boolean onKeyDown(int keyCode, KeyEvent event) {
		    if ((keyCode == KeyEvent.KEYCODE_BACK) && mWebView.canGoBack()) {
		        mWebView.goBack();
		        return true;
		    }
		    return super.onKeyDown(keyCode, event);
		}
		
		@Override
		public boolean onCreateOptionsMenu(Menu menu) {
		    MenuInflater inflater = getMenuInflater();
		    inflater.inflate(R.layout.bmenu, menu);
		    Log.e("n", " Blog Menu Created");
		    return true;
		}
		

		@Override
		public boolean onOptionsItemSelected(MenuItem item) {
		    switch (item.getItemId()) {
		        case R.id.bback:
		        	finish();
		            break;
		        case R.id.bhome:
		        	mWebView.loadUrl("http://www.marksonvisuals.com/blog/");
		            break;
		    }
		    return true;
		}
}
