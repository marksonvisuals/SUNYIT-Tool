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
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.net.Uri;
import android.net.http.SslError;
import android.os.Bundle;
import android.os.Vibrator;
import android.util.Log;
import android.view.KeyEvent;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.MotionEvent;
import android.view.View;
import android.webkit.SslErrorHandler;
import android.webkit.WebView;
import android.webkit.WebViewClient;


public class angelActivity extends Activity {
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
		setContentView(R.layout.angelweb);

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
		
		mWebView = (WebView) findViewById(R.id.angelview);
		mWebView.getSettings().setJavaScriptEnabled(true);
		mWebView.setLongClickable(false);

		registerForContextMenu(findViewById(R.id.angelview));

		mWebView.setWebViewClient(new WebViewClient() {

			public boolean shouldOverrideUrlLoading(WebView view, String url) {

				// Log.i("MAIN", "Processing webview url click... ");
				view.loadUrl(url);

				return true;
			}
			
			public void onReceivedSslError (WebView view, SslErrorHandler handler, SslError error) {

		    	 handler.proceed() ;

		    	 }
			
		});

		/*
		 * mWebView.setWebViewClient(new HelloWebViewClient() { public void
		 * onLoadResource (WebView view, String url) { if (url.endsWith(".pdf")
		 * || url.endsWith(".zip") || url.endsWith(".doc")||
		 * url.endsWith(".docx")|| url.endsWith(".ppt")) { Intent myIntent = new
		 * Intent(Intent.ACTION_VIEW, Uri.parse(url));
		 * 
		 * startActivity(myIntent); } else super.onLoadResource(view,url); } }
		 * );
		 */

		mWebView.loadUrl("https://sunyit.sln.suny.edu/display/default.asp?ts=0&title=SUNYIT+TOOL&mode=PDA");

		mWebView.setOnTouchListener(new View.OnTouchListener() {
			private boolean toggle = false;

			public boolean onTouch(View v, MotionEvent event) {
				Vibrator vib = (Vibrator) getSystemService(Context.VIBRATOR_SERVICE);
				WebView.HitTestResult hr = ((WebView) v).getHitTestResult();
				if (!toggle && event.getDownTime() + 1000 < event.getEventTime()) {
					toggle = true;
					return true;
				}

				if (event.getAction() == MotionEvent.ACTION_UP && toggle) {

					if (hr.getType() == WebView.HitTestResult.SRC_ANCHOR_TYPE) {
						
						vib.vibrate(500);
						String fnurl = hr.getExtra().toString();
						int fn = fnurl.lastIndexOf("/");
						

						String filename = fnurl.substring(fn).replace("/", "");

						if (hr.getExtra().contains("AngelUploads/Content")) {
							download(filename, hr.getExtra());
						}

					}

					toggle = false;
					return true;
				}

				return false;

			}

		});

	}

	public void download(String filename, final String url) {
		AlertDialog alertDialog = new AlertDialog.Builder(angelActivity.this)
		.setTitle("Download File?")
		.setMessage("You are trying to download the file:"+ filename + "\n\n Would you like to continue?")
		.setPositiveButton("Yes", new DialogInterface.OnClickListener() {
			public void onClick(DialogInterface dialog, int which) {

				Log.i("DOWNLOADING", url);
				Intent intent = new Intent(Intent.ACTION_VIEW, Uri.parse(url));
				startActivity(intent);

			}
		})
		.setNegativeButton("No", new DialogInterface.OnClickListener() {
			public void onClick(DialogInterface dialog, int which) {

				dialog.dismiss();

			}
		})
		.create();
		/*alertDialog.setTitle("Download File?");
		alertDialog.setMessage("You are trying to download the file:"+ filename + "\n\n Would you like to continue?");

		alertDialog.setButton(2,"Yes", new DialogInterface.OnClickListener() {
			public void onClick(DialogInterface dialog, int which) {

				Log.i("DOWNLOADING", url);
				Intent intent = new Intent(Intent.ACTION_VIEW, Uri.parse(url));
				startActivity(intent);

			}
		});
		alertDialog.setButton(1,"No", new DialogInterface.OnClickListener() {
			public void onClick(DialogInterface dialog, int which) {

				dialog.dismiss();

			}
		});*/
		
		

		alertDialog.show();
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
		inflater.inflate(R.layout.angelmenu, menu);
		Log.e("n", " Angel Menu Created");
		return true;
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		switch (item.getItemId()) {
		case R.id.angback:
			mWebView.loadUrl("https://sunyit.sln.suny.edu/signon/logout.asp");
			mWebView.loadUrl("https://sunyit.sln.suny.edu/display/default.asp?mode=HI");
			finish();
			break;
		case R.id.anghome:
			mWebView.loadUrl("https://sunyit.sln.suny.edu/profile/default.asp");
			break;
		}
		return true;
	}
}
