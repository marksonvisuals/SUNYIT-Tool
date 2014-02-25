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


import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.widget.Toast;
import android.os.Bundle;
 
public class NotificationActivity extends BroadcastReceiver {
 
	
	
	
 @Override
 public void onReceive(Context context, Intent intent) {
	 try {
	     Bundle bundle = intent.getExtras();
	     String message = bundle.getString("alarm_message");
	     Intent newIntent = new Intent(context, ClassesService.class);
	     newIntent.putExtra("alarm_message", message);
	     newIntent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
	     context.startActivity(newIntent);
	    } catch (Exception e) {
	     Toast.makeText(context, "There was an error somewhere, but we still received an alarm", Toast.LENGTH_SHORT).show();
	     e.printStackTrace();
	    }
 
 }
}
	

