/*Attività per i credits*/
package it.unisa.bitfighter;

import com.example.bitfighter.R;

import android.app.Activity;
import android.app.ActionBar;
import android.app.Fragment;
import android.content.Intent;
import android.graphics.Typeface;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.view.View.OnClickListener;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;
import android.os.Build;

public class Crediti extends Activity {
	
	 String inviamail = "";
	 String indirizzo;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		Bundle b = getIntent().getExtras(); //prendo gli extras passati all'attività
		String lingua = b.getString("lang"); // prendo la lingua dall'extra

		
		setContentView(R.layout.activity_crediti);
		getWindow().setFlags(WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON, WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON);
			
		Button bottonecontattaci = (Button)findViewById(R.id.contattaci);

		TextView titolocredits = (TextView)findViewById(R.id.credits);
		Typeface tf = Typeface.createFromAsset(getAssets(),"fonts/8bitoperator.ttf");
		titolocredits.setTypeface(tf);
		
		TextView introcredits = (TextView)findViewById(R.id.intro);
		introcredits.setTextSize(20);
        
        /*faccio un controllo sul valore della lingua e mi regolo di conseguenza sotto*/
		if(lingua.equals("ITA")){
			titolocredits.setText(getText(R.string.creditsITA));	
			introcredits.setText(getText(R.string.textcreditsITA));
			inviamail = (String) getText(R.string.inviamailITA);
			bottonecontattaci.setText(getText(R.string.contattaciITA));
		}
		else if (lingua.equals("FRA")){
			titolocredits.setText(getText(R.string.creditsFRA));
			introcredits.setText(getText(R.string.textcreditsFRA));
			inviamail = (String) getText(R.string.inviamailFRA);
			bottonecontattaci.setText(getText(R.string.contattaciFRA));
		}
		else if(lingua.equals("ENG")){
			titolocredits.setText(getText(R.string.creditsENG));
			introcredits.setText(getText(R.string.textcreditsENG));	
			inviamail = (String) getText(R.string.inviamailENG);
			bottonecontattaci.setText(getText(R.string.contattaciENG));
		}
			
		TextView Alberto = (TextView)findViewById(R.id.alberto);
		Alberto.setTypeface(tf);
		
		TextView Giovanni = (TextView)findViewById(R.id.giovanni);
		Giovanni.setTypeface(tf);
		
		bottonecontattaci = (Button) findViewById(R.id.contattaci);
		
		bottonecontattaci.setOnClickListener(new OnClickListener() {
			 
			@Override
			public void onClick(View v) {
				indirizzo = (String)getText(R.string.indirizzimail); 
				sendmail();	 
			}
		});

		
//		bottoneAlberto = (Button) findViewById(R.id.albertomail);
//		bottoneAlberto.setOnClickListener(new OnClickListener() {
// 
//			@Override
//			public void onClick(View v) {
//				indirizzo = (String)getText(R.string.Alberto_mail); 
//				sendmail();	 
//			}
//		});
//			
//			bottoneGiovanni = (Button) findViewById(R.id.giovannimail);
//			bottoneGiovanni.setOnClickListener(new OnClickListener() {
//	 
//				@Override
//				public void onClick(View v) {
//					indirizzo = (String)getText(R.string.Giovanni_mail); 
//					sendmail();	 
//				}
//			});
			
			
		}
	
	public void sendmail(){
		Intent emailIntent = new Intent(android.content.Intent.ACTION_SEND);
		String[] recipients = new String[]{indirizzo, "",};
		emailIntent.putExtra(android.content.Intent.EXTRA_EMAIL, recipients);
		emailIntent.putExtra(android.content.Intent.EXTRA_SUBJECT, "AeroFighter8bit");
		emailIntent.setType("message/rfc822");
		startActivity(Intent.createChooser(emailIntent, inviamail));

	}

	
	
	

	
	//metodo per invio mail
	public void inviamail(String indirizzomail){
		Intent emailIntent = new Intent(Intent.ACTION_SEND);
		emailIntent.putExtra(Intent.EXTRA_EMAIL  , new String[]{indirizzomail});
		emailIntent.putExtra(Intent.EXTRA_SUBJECT, "AeroFighter8bit");
        startActivity(Intent.createChooser(emailIntent, "Choose an Email client"));
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
