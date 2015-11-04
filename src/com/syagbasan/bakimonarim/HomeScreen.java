package com.syagbasan.bakimonarim;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Bundle;
import android.os.Handler;
import android.preference.PreferenceManager;
import android.view.KeyEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;

public class HomeScreen extends Activity implements OnClickListener, android.content.DialogInterface.OnClickListener{

	boolean connection=false;
	Button giris,pref;
    long startTime = 0;
    int minutes=0,seconds=0; 
    public static String 
    	SqlHost="" ,SqlPort="",SqlUsername="",SqlPassword="",
    	SqlDatabaseName="",SqlTableName="",FtpHost="",FtpPort="",
   		FtpUsername="",FtpPassword="",NameSurname="";
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.home);
		
		pref = (Button) findViewById(R.id.bPreferences);
		pref.setOnClickListener(this);
		giris = (Button) findViewById(R.id.bGiris);
		giris.setOnClickListener(this);
		
		AlertDialog ad = new AlertDialog.Builder(this)
			.setMessage("Uygulamaya Giriş yapmadan önce Kullanıcı Ayarları'nızı doğru girdiğinizden emin olunuz.. ")
			.setIcon(R.drawable.ic_launcher)
			.setTitle("Uyarı:")
			.setNeutralButton("Tamam", this)
			.setCancelable(false)
			.create();
		ad.show();
	    	   
		if( getIntent().getBooleanExtra("Exit me", false)){
		    finish();
		    return; // add this to prevent from doing unnecessary stuffs
		}
	}
	
	public boolean checkOnlineState() {
		
	    ConnectivityManager CManager = (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);
	    NetworkInfo NInfo = CManager.getActiveNetworkInfo();
	    if (NInfo != null && NInfo.isConnectedOrConnecting()) {
	    	connection = true;
	        return true;
	    }else{
	    	connection = false;
	    }
	    return false;
	}

	Handler timerHandler = new Handler();
	Runnable timerRunnable = new Runnable() {
	    
	    @Override
	    public void run() {
	        long millis = System.currentTimeMillis() - startTime;
	        seconds = (int) (millis / 1000);
	        minutes = seconds / 60;
	        seconds = seconds % 60;

	        timerHandler.postDelayed(this, 500);
	        if(seconds==2){
	        	timerHandler.removeCallbacks(timerRunnable);
	        	giris.setText("Giris"); 
	        	giris.setTextColor(Color.parseColor("#006099"));
	        }
	    }
	};
	
	@Override
	public void onClick(View v){

		switch(v.getId()){
			case R.id.bGiris:
				checkOnlineState();
				connection();
			break;
			case R.id.bPreferences:
				startActivity(new Intent(HomeScreen.this, Preferences.class));
			break;
		}
	}
	
	@Override
	protected void onResume() {
	    // TODO onResume
	    super.onResume();
	    SharedPreferences myPreference=PreferenceManager.getDefaultSharedPreferences(this);   
	    NameSurname = myPreference.getString("editnamesurname", "");
	    FtpHost = myPreference.getString("editFTPhost", "");
	    FtpPort = myPreference.getString("editFTPport", "");
	    FtpUsername = myPreference.getString("editFTPuser", "");
	    FtpPassword = myPreference.getString("editFTPpass", "");
	    SqlHost = myPreference.getString("editSQLhost", "");
	    SqlPort = myPreference.getString("editSQLport", "");
	    SqlUsername = myPreference.getString("editSQLuser", "");
	    SqlPassword =  myPreference.getString("editSQLpass", ""); 
	    SqlDatabaseName = myPreference.getString("editSQLdatabasename", "");
	    SqlTableName =  myPreference.getString("editSQLtablename", ""); 
	}
	
	public void connection() {
		if(connection){
			Intent intent = new Intent(getBaseContext(), MainActivity.class);
			startActivity(intent);		
		}else{
			giris.setTextColor(Color.parseColor("red"));
			giris.setText("Lütfen Internet Bağlantınızı Kontrol Ediniz");
		}
		startTime = System.currentTimeMillis();
        timerHandler.postDelayed(timerRunnable, 0);
	}

	@Override
	public boolean onKeyDown(int keyCode, KeyEvent event) {
	    if (keyCode == KeyEvent.KEYCODE_BACK) {
	    	 AlertDialog ad2 = new AlertDialog.Builder(this)
	 	   	.setIcon(R.drawable.ic_launcher)
	 		.setTitle("UYGULAMADAN ÇIKMAK İSTEDİĞİNİZE EMİN MİSİNİZ?")
	 		.setPositiveButton("Evet", this)
	 		.setNegativeButton("Hayır", this)
	 		.setCancelable(false)
	 		.create();
	 		ad2.show();
	    	 
	        return true;
	    }
	    return super.onKeyDown(keyCode, event);
	}

	@Override
	public void onClick(DialogInterface arg0, int arg1) {
		switch(arg1){
		case DialogInterface.BUTTON_POSITIVE:
			finish();
	        moveTaskToBack(true);
			break;
		case DialogInterface.BUTTON_NEGATIVE:
			break;
		case DialogInterface.BUTTON_NEUTRAL:
			break;
		}
	}		
}