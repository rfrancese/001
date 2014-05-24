/*Menù principale*/
package it.unisa.bitfighter;

import it.unisa.bitfighter.MainActivity;

import java.io.IOException;
import java.io.OutputStreamWriter;

import com.example.bitfighter.R;

import android.app.Activity;
import android.app.ActivityManager;
import android.app.Fragment;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.util.Log;
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
	ProgressDialog loadingdialog;
		
	@Override
	protected void onCreate(Bundle savedInstanceState) {	
			
		Bundle b = getIntent().getExtras(); //prendo gli extras passati all'attività
		lingua = b.getString("lang"); // prendo la lingua dall'extra
		
		super.onCreate(savedInstanceState);
		
//		/*codice per full screen*/
//		requestWindowFeature(Window.FEATURE_NO_TITLE);
//        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
//        /*fine*/
		
		loadingdialog=new ProgressDialog(this);
		loadingdialog.dismiss(); //e spengo il dialogo
        
		setContentView(R.layout.activity_menu);	
		getWindow().setFlags(WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON, WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON);
		
		final Button playbutton = (Button)findViewById(R.id.play);
		final Button classificabutton = (Button)findViewById(R.id.classifica);
		final Button creditsbutton = (Button)findViewById(R.id.credits);
		final Button cambialinguabutton = (Button)findViewById(R.id.cambialingua);
		final Button escibutton = (Button)findViewById(R.id.esci);
		 	
		
	/*faccio un controllo sul valore della lingua e mi regolo di conseguenza sotto*/
		if(lingua.equals("ITA")){
			playbutton.setText(getText(R.string.playITA));		
			classificabutton.setText(getText(R.string.classificaITA));	
			creditsbutton.setText(getText(R.string.creditsITA));	
			cambialinguabutton.setText(R.string.cambialinguaITA);
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
			cambialinguabutton.setText(R.string.cambialinguaFRA);
			escibutton.setText(getText(R.string.esciFRA));	
		}
		else if(lingua.equals("ENG")){
			playbutton.setText(getText(R.string.playENG));		
			classificabutton.setText(getText(R.string.classificaENG));	
			creditsbutton.setText(getText(R.string.creditsENG));
			cambialinguabutton.setText(R.string.cambialinguaENG);
			escibutton.setText(getText(R.string.esciENG));	
		}
	}
	
	/*vai al gioco*/
	public void gioca(View view){
		loadingdialog = ProgressDialog.show(this, "", "Caricamento in corso, attendere ...", true); //faccio uscire il dialogo di caricamento in corso
		Intent intent = new Intent(this, Gioca.class);
		Bundle language = new Bundle();
		language.putString("lang", lingua);
		intent.putExtras(language);
	    startActivity(intent); //inizio l'attività nuova	    
	    finish();
	}
	
	/*vai alla classifica*/
	public void leggiclassifica(View view){
		Intent intent = new Intent(this, Classifica.class);
		Bundle language = new Bundle();
		language.putString("lang", lingua);
		intent.putExtras(language);
	    startActivity(intent);
	}
	
	/*permette di cambiare la lingua, porta l'utente al menù di seleziona lingua*/
	public void cambialingua(View view){
		Intent intent = new Intent(this, MainActivity.class);
//		Bundle language = new Bundle();
		writeToFile("nullo");
//		language.putString("flagselezionelingua", "VERO");
//		intent.putExtras(language);
	    startActivity(intent);
	    finish(); //chiude questa attività;
	}
	
	public void writeToFile(String data) {
	    try {
	        OutputStreamWriter outputStreamWriter = new OutputStreamWriter(openFileOutput("linguaselezione.txt", Context.MODE_PRIVATE));
	        outputStreamWriter.write(data);
	        outputStreamWriter.close();
	        Log.d("SCRITTO", "HO SCRITTO " + data);
	    }
	    catch (IOException e) {
	        Log.e("Exception", "File write failed: " + e.toString());
	    } 
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
