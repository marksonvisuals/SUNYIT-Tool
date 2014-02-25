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

import android.accounts.Account;
import android.accounts.AccountManager;
import android.app.ListActivity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import java.util.ArrayList;

import com.google.analytics.tracking.android.EasyTracker;


public class AccountSelector extends ListActivity {
    @Override
    public void onCreate(Bundle icicle) {
        super.onCreate(icicle);
        setContentView( R.layout.accounts );
        AccountManager accountManager = AccountManager.get( this );
        Account[] googleAccounts = accountManager.getAccountsByType( "com.google" );
        ArrayList<String> items = new ArrayList<String>();
        for( int i = 0 ; i < googleAccounts.length ; ++i )
            items.add( googleAccounts[i].name );
        accountAdapter = new ArrayAdapter<String>(this, R.layout.account_row, items);
        setListAdapter( accountAdapter );
    }

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
    
    protected void onListItemClick(
            ListView l,
            View v,
            int position,
            long id ) {
        String accountName = accountAdapter.getItem( position );
        Intent extras = new Intent();
        extras.putExtra( "account",accountName );
        setResult( RESULT_OK, extras );
        finish();
    }

    private ArrayAdapter<String> accountAdapter;
}
