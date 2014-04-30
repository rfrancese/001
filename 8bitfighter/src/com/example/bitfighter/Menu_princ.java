/*Menù principale*/
package com.example.bitfighter;


import android.app.Activity;
import android.app.ActivityManager;
import android.app.Fragment;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.Toast;

public class Menu_princ extends Activity {
		
	String lingua = "";
		
	@Override
	protected void onCreate(Bundle savedInstanceState) {	
			
		Bundle b = getIntent().getExtras(); //prendo gli extras passati all'attività
		lingua = b.getString("lang"); // prendo la lingua dall'extra
		
		super.onCreate(savedInstanceState);
		
//		/*codice per full screen*/
//		requestWindowFeature(Window.FEATURE_NO_TITLE);
//        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
//        /*fine*/
        
		setContentView(R.layout.activity_menu);	
		
		final Button playbutton = (Button)findViewById(R.id.play);
		final Button classificabutton = (Button)findViewById(R.id.classifica);
		final Button creditsbutton = (Button)findViewById(R.id.credits);
		final Button escibutton = (Button)findViewById(R.id.esci);
		 	
		
	/*faccio un controllo sul valore della lingua e mi regolo di conseguenza sotto*/
		if(lingua.equals("ITA")){
			playbutton.setText(getText(R.string.playITA));		
			classificabutton.setText(getText(R.string.classificaITA));	
			creditsbutton.setText(getText(R.string.creditsITA));	
			escibutton.setText(getText(R.string.esciITA));	
			/*Context context = getApplicationContext();
			CharSequence text = "ITA";
			int duration = Toast.LENGTH_SHORT;

			Toast toast = Toast.makeText(context, text, duration);
			toast.show();*/
		}
		else if (lingua.equals("FRA")){
			playbutton.setText(getText(R.string.playFRA));		
			classificabutton.setText(getText(R.string.classificaFRA));	
			creditsbutton.setText(getText(R.string.creditsFRA));	
			escibutton.setText(getText(R.string.esciFRA));	
		}
		else if(lingua.equals("ENG")){
			playbutton.setText(getText(R.string.playENG));		
			classificabutton.setText(getText(R.string.classificaENG));	
			creditsbutton.setText(getText(R.string.creditsENG));	
			escibutton.setText(getText(R.string.esciENG));	
		}
	}
	
	/*vai al gioco*/
	public void gioca(View view){
		//da fare
		//Intent intent = new Intent(this, Gioca.class);
	    //startActivity(intent);
	}
	
	/*vai alla classifica*/
	public void leggiclassifica(View view){
		Intent intent = new Intent(this, Classifica.class);
		Bundle language = new Bundle();
		language.putString("lang", lingua);
		intent.putExtras(language);
	    startActivity(intent);
	}
	
	/*vai alla credits*/
	public void leggicredits(View view){
		Intent intent = new Intent(this, Crediti.class);
		Bundle language = new Bundle();
		language.putString("lang", lingua);
		intent.putExtras(language);
	    startActivity(intent);
	}
	
	/*esci dall'applicazione*/
	public void esci (View view) {
		finish();
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
