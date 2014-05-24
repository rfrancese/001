/*Scelta della lingua*/
package it.unisa.bitfighter;

import java.io.BufferedInputStream;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;

import com.example.bitfighter.R;

import android.app.Activity;
import android.app.ActionBar;
import android.app.Fragment;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.os.Bundle;
import android.os.PowerManager;
import android.support.v4.widget.SimpleCursorAdapter;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.os.Build;


/*attività iniziale della selezione della lingua*/

public class MainActivity extends Activity {
	String lingua = "";
	Bundle b;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
//		if((b = getIntent().getExtras())!=null){ //prendo gli extras passati all'attività
//			lingua = b.getString("flagselezionelingua");
//			super.onCreate(savedInstanceState);
//			setContentView(R.layout.activity_main);
//		}
//		else{
		super.onCreate(savedInstanceState);

		
//		/*codice per full screen*/
//		requestWindowFeature(Window.FEATURE_NO_TITLE);
//        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, 
//                                WindowManager.LayoutParams.FLAG_FULLSCREEN);
//        /*fine*/
		
			//altrimenti legge dal file e capisce cosa l'utente ha selezionato
			String ritorna = readFromFile();
			Log.d("LETTO", "HO LETTO " + ritorna);
			
				if(ritorna.equals("ITA") || ritorna.equals("ENG") || ritorna.equals("FRA") ){
					creaintento(ritorna);
					writeToFile(ritorna);
					Log.d("LETTO", "SONO QUI" + ritorna);
				}
				else
					setContentView(R.layout.activity_main);
	//	}
			
		getWindow().setFlags(WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON, WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON);
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


	public String readFromFile() {

	    String ret = "";

	    try {
	        InputStream inputStream = openFileInput("linguaselezione.txt");

	        if ( inputStream != null ) {
	            InputStreamReader inputStreamReader = new InputStreamReader(inputStream);
	            BufferedReader bufferedReader = new BufferedReader(inputStreamReader);
	            String receiveString = "";
	            StringBuilder stringBuilder = new StringBuilder();

	            while ( (receiveString = bufferedReader.readLine()) != null ) {
	                stringBuilder.append(receiveString);
	            }

	            inputStream.close();
	            ret = stringBuilder.toString();
	        }
	    }
	    catch (FileNotFoundException e) {
	        Log.e("login activity", "File not found: " + e.toString());
	    } catch (IOException e) {
	        Log.e("login activity", "Can not read file: " + e.toString());
	    }

	    return ret;
	}
	

	/*avvia il menu principale*/
	public void start_ITA(View view) {
		creaintento("ITA"); //crea l'intento per andare al menù principale	
		writeToFile("ITA");
	}
	public void start_ENG(View view) {
		creaintento("ENG"); //crea l'intento per andare al menù principale
		writeToFile("ENG");
	}
	public void start_FRA(View view) {
		creaintento("FRA"); //crea l'intento per andare al menù principale
		writeToFile("FRA");
	}
	
	/*crea intento prendendo in input la lingua scelta*/
	public void creaintento(String LANG){
		Intent intent = new Intent(
				getApplicationContext(),
				Menu_princ.class
			);
		Bundle lingua = new Bundle();
		lingua.putString("lang", LANG);
		intent.putExtras(lingua);
		startActivity(intent);	
		finish(); //chiude questa attività;
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {

		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.main, menu);
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
	
	@Override
	public void onBackPressed() {
		onDestroy();
	    android.os.Process.killProcess(android.os.Process.myPid());
	    // This above line close correctly
	}

	
}
