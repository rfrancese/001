/*Scelta della lingua*/
package com.example.bitfighter;

import android.app.Activity;
import android.app.ActionBar;
import android.app.Fragment;
import android.content.Intent;
import android.os.Bundle;
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
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		
//		/*codice per full screen*/
//		requestWindowFeature(Window.FEATURE_NO_TITLE);
//        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, 
//                                WindowManager.LayoutParams.FLAG_FULLSCREEN);
//        /*fine*/
        
		setContentView(R.layout.activity_main);

	
	}
	
	/*avvia il menu principale*/
	public void start_ITA(View view) {
		creaintento("ITA"); //crea l'intento per andare al menù principale		
	}
	public void start_ENG(View view) {
		creaintento("ENG"); //crea l'intento per andare al menù principale		
	}
	public void start_FRA(View view) {
		creaintento("FRA"); //crea l'intento per andare al menù principale				
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

	    android.os.Process.killProcess(android.os.Process.myPid());
	    // This above line close correctly
	}

	
}
