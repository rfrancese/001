/*Attività per la classifica*/
package it.unisa.bitfighter;

import java.util.Random;

import com.example.bitfighter.R;

import android.app.Activity;
import android.app.ActionBar;
import android.app.Fragment;
import android.graphics.Color;
import android.os.Bundle;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewGroup.LayoutParams;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.TableLayout;
import android.widget.TableRow;
import android.widget.TextView;
import android.os.Build;

public class Classifica extends Activity {
	

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		
		Bundle b = getIntent().getExtras(); //prendo gli extras passati all'attività
		String lingua = b.getString("lang"); // prendo la lingua dall'extra
            
		
		setContentView(R.layout.activity_classifica);
		getWindow().setFlags(WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON, WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON);
		
		TextView titoloclassifica = (TextView)findViewById(R.id.classifica);
        
        /*faccio un controllo sul valore della lingua e mi regolo di conseguenza sotto*/
		if(lingua.equals("ITA")){
			titoloclassifica.setText(getText(R.string.classificaITA));			
		}
		else if (lingua.equals("FRA")){
			titoloclassifica.setText(getText(R.string.classificaFRA));
		}
		else if(lingua.equals("ENG")){
			titoloclassifica.setText(getText(R.string.classificaENG));
		}
		
		leggiclassifica();
	}

	
	/*generazione di prova di una classifica*/
	private void leggiclassifica() {
		TableLayout ll = (TableLayout) findViewById(R.id.tabellaclassifica);

	    for (int i = 0; i < 300; i++) {
	        TableRow row= new TableRow(this);
	        TableRow.LayoutParams lp = new TableRow.LayoutParams(TableRow.LayoutParams.MATCH_PARENT);
	        lp.setMargins(0, 5, 0, 5);
	        row.setLayoutParams(lp);
	        TextView utente = new TextView(this);
	        TextView punteggio = new TextView(this);
	        utente.setText("cicciobello");
	        utente.setTextColor(Color.WHITE);
	        punteggio.setText("551100");
	        punteggio.setTextColor(Color.WHITE);
	        row.addView(utente);
	        row.addView(punteggio);
	        ll.addView(row,i);
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
