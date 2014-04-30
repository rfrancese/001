/*Attività per i credits*/
package com.example.bitfighter;

import android.app.Activity;
import android.app.ActionBar;
import android.app.Fragment;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.os.Build;

public class Crediti extends Activity {

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		Bundle b = getIntent().getExtras(); //prendo gli extras passati all'attività
		String lingua = b.getString("lang"); // prendo la lingua dall'extra
				
		
		setContentView(R.layout.activity_crediti);
		
		TextView titolocredits = (TextView)findViewById(R.id.credits);
		TextView introcredits = (TextView)findViewById(R.id.intro);
        
        /*faccio un controllo sul valore della lingua e mi regolo di conseguenza sotto*/
		if(lingua.equals("ITA")){
			titolocredits.setText(getText(R.string.creditsITA));	
			introcredits.setText(getText(R.string.textcreditsITA));			
		}
		else if (lingua.equals("FRA")){
			titolocredits.setText(getText(R.string.creditsFRA));
			introcredits.setText(getText(R.string.textcreditsFRA));			
		}
		else if(lingua.equals("ENG")){
			titolocredits.setText(getText(R.string.creditsENG));
			introcredits.setText(getText(R.string.textcreditsENG));			
		}

	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {

		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.menu, menu);
		return true;
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		// Handle action bar item clicks here. The action bar will
		// automatically handle clicks on the Home/Up button, so long
		// as you specify a parent activity in AndroidManifest.xml.
		int id = item.getItemId();
		if (id == R.id.action_esci) {
			onBackPressed();
		}
		return super.onOptionsItemSelected(item);
	}



}
