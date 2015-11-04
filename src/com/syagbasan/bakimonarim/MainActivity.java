package com.syagbasan.bakimonarim;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.text.SimpleDateFormat;
import java.util.Calendar;

import org.apache.commons.net.ftp.FTP;
import org.apache.commons.net.ftp.FTPClient;
import org.apache.commons.net.ftp.FTPReply;

import android.graphics.Bitmap;
import android.graphics.Color;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.media.AudioManager;
import android.media.ExifInterface;
import android.media.SoundPool;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.AlertDialog;
import android.content.ContentValues;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

public class MainActivity extends Activity implements LocationListener, android.content.DialogInterface.OnClickListener{

	public FTPClient mFTPClient = null;
	private String SQLhost="",SQLport="",SQLusername="",SQLpassword="",SQLdatabasename="",SQLtablename="",FTPhost="",FTPport="",FTPusername="",FTPpassword="";
	private File imageFile=null;
  	ImageView pic;
  	Bitmap bitmap;
   	static String takenFileName=null;
   	double latitude, longitude;
   	static LocationManager locationManager;
	static int positive = 0,negative = 0,shutter = 0;
   	ExifInterface exifInterface;
   	TextView siteno,sitename;
	static TextView deneme,haritadanLat,haritadanLon;
	TextView mainLon,mainLat;
   	static SoundPool sp;
   	Button server,capture,tcell,sol,btnHarita;
   	private String array_spinner[];
   	EditText etSiteno,etSitename,etAciklama;
	Spinner s;
	String firma = "TCELL",date_time=null;
	Connection conn = null;
	ResultSet resultSet;
	Statement statement;
	double sqlsendlat,sqlsendlong;
	boolean FTPconnection,SQLconnection;
	Uri imageUri = getOutputUri();
	
   @SuppressLint("SimpleDateFormat")
   @Override
   protected void onCreate(Bundle savedInstanceState) {
      super.onCreate(savedInstanceState);
      setContentView(R.layout.activity_main);
      
      UIControllers();
      
      SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy/MM/dd HH:mm:ss");
      Calendar cal = Calendar.getInstance();
      date_time = dateFormat.format(cal.getTime());

      SQLhost 	  = HomeScreen.SqlHost;
      SQLport     = HomeScreen.SqlPort;
      SQLusername = HomeScreen.SqlUsername;
      SQLpassword = HomeScreen.SqlPassword;
      SQLdatabasename = HomeScreen.SqlDatabaseName;
      SQLtablename = HomeScreen.SqlTableName;
      FTPhost	  = HomeScreen.FtpHost;
      FTPport	  = HomeScreen.FtpPort;
      FTPusername = HomeScreen.FtpUsername;
      FTPpassword = HomeScreen.FtpPassword;
      
      //gps config...
      locationManager = (LocationManager) getSystemService(Context.LOCATION_SERVICE);
      locationManager.requestLocationUpdates( LocationManager.NETWORK_PROVIDER,0,0,this);
      
      sp = new SoundPool(5 , AudioManager.STREAM_MUSIC , 0);
      positive = sp.load(this, R.raw.positive, 1);
      negative = sp.load(this, R.raw.negative, 1);
      shutter = sp.load(this, R.raw.shutter, 1);	
   }
   
   @SuppressWarnings("rawtypes")
   private void UIControllers(){
	   
	   deneme = (TextView) findViewById(R.id.tdeneme);
	   	
	   haritadanLat = (TextView) findViewById(R.id.tvharitadanlat);
	   haritadanLon = (TextView) findViewById(R.id.tvharitadanlon);
	      
	   btnHarita = (Button) findViewById(R.id.bHarita);
	   btnHarita.setClickable(false);      
	      
	   etSitename = (EditText) findViewById(R.id.etSiteName);
	   etSiteno = (EditText) findViewById(R.id.etSiteNo);
	   etAciklama = (EditText) findViewById(R.id.etAciklama);
	   sitename = (TextView) findViewById(R.id.tvSiteName);
	   siteno = (TextView) findViewById(R.id.tvSiteNo);
	   mainLat = (TextView) findViewById(R.id.tvLat);
	   mainLon = (TextView) findViewById(R.id.tvLong);
	      
	   array_spinner=new String[4];
	   array_spinner[0]="Site Bakım Kontrol";
	   array_spinner[1]="DÖF Bildirim";
	   array_spinner[2]="Site Visit";
	   array_spinner[3]="Diğer";
	   s = (Spinner) findViewById(R.id.spinner1);
	   @SuppressWarnings("unchecked")
	   ArrayAdapter adapter = new ArrayAdapter(this, android.R.layout.preference_category, array_spinner);
	   s.setAdapter(adapter);
	      
	   pic = (ImageView)findViewById(R.id.imageView2);
	      
	  server = (Button) findViewById(R.id.bUploadToServer);
	  server.setTextColor(Color.rgb(00, 99, 00));
	  server.setClickable(false);
	  capture = (Button) findViewById(R.id.button_capture);
	  capture.setTextColor(Color.rgb(10, 768, 909));
	  tcell = (Button) findViewById(R.id.bTCELL);
	  tcell.setTextColor(Color.rgb(00, 99, 00));
	  sol = (Button) findViewById(R.id.bSOL);
	  sol.setTextColor(Color.BLACK);
   }
   
   private void UpdateExif(){
		
	   try {
			exifInterface = new ExifInterface(this.imageFile.getAbsolutePath());
	   } catch (IOException e1) {
			e1.printStackTrace();
	   }
	   	
	   int num1Lat = (int)Math.floor(sqlsendlat);
	   int num2Lat = (int)Math.floor((sqlsendlat - num1Lat) * 60);
	   double num3Lat = (sqlsendlat - ((double)num1Lat+((double)num2Lat/60))) * 3600000;

	   int num1Lon = (int)Math.floor(sqlsendlong);
	   int num2Lon = (int)Math.floor((sqlsendlong - num1Lon) * 60);
	   double num3Lon = (sqlsendlong - ((double)num1Lon+((double)num2Lon/60))) * 3600000;

	   exifInterface.setAttribute(ExifInterface.TAG_GPS_LATITUDE, num1Lat+"/1,"+num2Lat+"/1,"+num3Lat+"/1000");
	   exifInterface.setAttribute(ExifInterface.TAG_GPS_LONGITUDE, num1Lon+"/1,"+num2Lon+"/1,"+num3Lon+"/1000");

	   if (latitude > 0) {
	        exifInterface.setAttribute(ExifInterface.TAG_GPS_LATITUDE_REF, "N"); 
	   } else {
	        exifInterface.setAttribute(ExifInterface.TAG_GPS_LATITUDE_REF, "S");
	   }

	   if (longitude > 0) {
	        exifInterface.setAttribute(ExifInterface.TAG_GPS_LONGITUDE_REF, "E");    
	   }else {
	    	exifInterface.setAttribute(ExifInterface.TAG_GPS_LONGITUDE_REF, "W");
	   }
	 
	   try {
		   exifInterface.saveAttributes();
	   } catch (IOException e) {
		   e.printStackTrace();
		   Toast.makeText(MainActivity.this, e.toString(),Toast.LENGTH_LONG).show();
	   }
}
   
   @SuppressLint("SimpleDateFormat")
   public void Send(View view){
	   if(GoogleMaps.only_one_marker == false){
			sqlsendlat = GoogleMaps.latsql;
			sqlsendlong = GoogleMaps.lonsql;
		}else{
			sqlsendlat = latitude;
			sqlsendlong = longitude;
		}
	   
	   SimpleDateFormat dateFormat = new SimpleDateFormat(
	            "dd/MM/yyyy HH:mm:ss");
	   Calendar cal = Calendar.getInstance();
	   date_time = dateFormat.format(cal.getTime());
	   
	   UpdateExif();
	   
	   ftpConnect();
	   
	   pic.setImageResource(R.drawable.capture);
	   
	   btnHarita.setClickable(false);
	   haritadanLat.setText("");
	   haritadanLon.setText("");
	   
	   String message="",Titlemessage="";
	   if(!FTPconnection){
		   message ="FTP Kullanıcı Ayarlarınız Hatalı yada Server Kapalı\nLütfen giriş sayfasındaki Kullanıcı Ayarları bilgilerinizi düzeltiniz...";
		   Titlemessage = "Hata: Fotoğraf ve Açıklamalar Gönderilemedi";
		   sp.play(negative,1,1,0,0,1);
	   }else if(FTPconnection && !SQLconnection){
		   message = "SQL Kullanıcı Ayarlarınız Hatalı yada Server Kapalı\nLütfen giriş sayfasındaki Kullanıcı Ayarları bilgilerinizi düzeltiniz...";
		   Titlemessage = "Hata: Fotoğraf ve Açıklamalar Gönderilemedi";
		   sp.play(negative,1,1,0,0,1);
	   }else if(FTPconnection && SQLconnection){
		   message = "Fotoğraf ve Açıklamalar Gönderildi";
		   Titlemessage = "Başarılı";
		   sp.play(positive, 1, 1, 0, 0, 1);
	   }
	   
	   AlertDialog ad = new AlertDialog.Builder(this)
	   	.setIcon(R.drawable.ic_launcher)
		.setTitle(Titlemessage)
		.setPositiveButton("Tamam", this)
		.setCancelable(false)
		.setMessage(message)
		.create();
		ad.show();   
   } 
   
   public boolean ftpConnect(){
	   
		try {
			mFTPClient = new FTPClient();
			mFTPClient.connect(FTPhost, Integer.parseInt(FTPport));
			// now check the reply code, if positive mean connection success
			if (FTPReply.isPositiveCompletion(mFTPClient.getReplyCode())) {
			boolean status = mFTPClient.login(FTPusername, FTPpassword);
			mFTPClient.setFileType(FTP.BINARY_FILE_TYPE);
			mFTPClient.enterLocalPassiveMode();
			
			String data = Environment.getExternalStorageDirectory() +File.separator+"DCIM"+File.separator+"Bakim-Onarim"+File.separator+ takenFileName+".jpg";
			
			FileInputStream in = new FileInputStream(new File(data));
		    boolean result = mFTPClient.storeFile(File.separator+takenFileName+".jpg", in);
		    in.close();
		    
		    if (result) {
			    server.setClickable(false);
			    result = false;
			    FTPconnection = true;
			    sqlConnectAndUpdate();
		    }else{
		    	FTPconnection = false;
		    }
		    
		    if(SQLconnection == false){
		    	mFTPClient.deleteFile(takenFileName+".jpg");
		    }
			
		    mFTPClient.logout(); 
		    mFTPClient.disconnect();
			return status;
			}
		} catch(Exception e) {
			e.printStackTrace();
		}
		return false;
	}
   
   private void sqlConnectAndUpdate() {

	   try {
			try{
				conn = CONN( SQLdatabasename, SQLhost+":"+SQLport);
				statement = conn.createStatement();
									
				String konu = array_spinner[s.getSelectedItemPosition()];
				statement.executeUpdate("INSERT INTO "+SQLtablename+"(LokasyonKodu,LokasyonAdi,Konu,Aciklama,Firma,FotoNo,N,E,KayitYapan,KayitTarihi)"
				+ "VALUES('"+etSiteno.getText().toString()+"','"+etSitename.getText().toString()+"','"+ konu +"','"+ etAciklama.getText().toString() +"','"+
				firma+"','"+takenFileName+"','"+ String.valueOf(sqlsendlat) +"','" + String.valueOf(sqlsendlong) + "','" + HomeScreen.NameSurname + "','" + date_time + "')");
									 
				statement.close();
				conn.close();	
			}catch (SQLException e) {
				e.printStackTrace();
				SQLconnection = false;
			}
		
			try{
				conn = CONN( SQLdatabasename, SQLhost+":"+SQLport);
				statement = conn.createStatement();
				resultSet = statement.executeQuery("SELECT FotoNo FROM "+SQLtablename+" where FotoNo='"+ takenFileName +"'");

				while (resultSet.next()) {
					deneme.setText(resultSet.getString("FotoNo"));
					SQLconnection = true;
				}

				resultSet.close();
				statement.close();
				conn.close();
			}catch (Exception e){
				SQLconnection = false;
				Toast.makeText(getApplicationContext(), "Database gönderilen veriler kontrol edilemedi", Toast.LENGTH_LONG).show();
			}		
		} catch (Exception e) {
			e.printStackTrace();
			SQLconnection = false;
			Toast.makeText(getApplicationContext(), "Update işlemi gerçekleşmedi" , Toast.LENGTH_LONG).show();
		}
	}
   

   private Connection CONN( String _DB, String _server ){

		Connection conn = null;
		String ConnURL = null;
		try {
			Class.forName("net.sourceforge.jtds.jdbc.Driver");
			ConnURL = "jdbc:jtds:sqlserver://" + _server + ";" + "databaseName=" + _DB + ";user=" + SQLusername + ";password=" + SQLpassword + ";";
			conn = DriverManager.getConnection(ConnURL);	
		} catch (SQLException se) {
			Log.e("ERRO",se.getMessage());
		} catch (ClassNotFoundException e) {
			Log.e("ERRO",e.getMessage());
		} catch (Exception e) {
		    Log.e("ERRO",e.getMessage());
		}
		return conn;
   }
   
   public void TakeFoto(View view){
	   imageUri = getOutputUri();
	   ContentValues values = new ContentValues();
	   values.put(MediaStore.Images.Media.TITLE, "New Picture");
	   values.put(MediaStore.Images.Media.DESCRIPTION, "From your Camera");
	   Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
	   intent.putExtra(MediaStore.EXTRA_OUTPUT, imageUri);
	   startActivityForResult(intent, 0);
	  
	   server.setClickable(true);
	   btnHarita.setClickable(true);
   }
   
   public void Harita(View view){
	   haritadanLat.setText("");
	   haritadanLon.setText("");
	   Intent i = new Intent(getApplicationContext() , GoogleMaps.class);
	   startActivity(i );
   }
   
   public void TCELL(View view){
	   tcell.setTextColor(Color.rgb(00, 99, 00));
	   sol.setTextColor(Color.BLACK);
	   sitename.setText("Site Name: ");
	   siteno.setText("Site No: ");
	   firma = "TCELL";
   }
   
   public void SOL(View view){
	   tcell.setTextColor(Color.BLACK);
	   sol.setTextColor(Color.rgb(00, 99, 00));
	   sitename.setText("Proje Adı: ");
	   siteno.setText("Proje Kodu: ");
	   firma = "SOL";
   }

   @Override
   protected void onActivityResult(int requestCode, int resultCode, Intent data) {

	   super.onActivityResult(requestCode, resultCode, data);
	   if (requestCode == 0) {
	    	try {
	    		bitmap = MediaStore.Images.Media.getBitmap(getContentResolver(), imageUri);
	    		pic.setImageBitmap(bitmap);
	    	} catch (FileNotFoundException e) {
	    		e.printStackTrace();
	    	} catch (IOException e) {
	    		e.printStackTrace();
	    	}
	   }
   }
   
   private Uri getOutputUri() {
	   // If sd card is available
	   if(android.os.Environment.getExternalStorageState().equals(android.os.Environment.MEDIA_MOUNTED)){
		   
		   String path = Environment.getExternalStorageDirectory() .getAbsolutePath();
		   takenFileName = "Img" + System.currentTimeMillis();
		   path += "/DCIM";
		   path += "/Bakim-Onarim";
		   File file = new File(path);
		   if (!file.isDirectory()) {
		      file.mkdirs();
		   }
		   path += "/" + takenFileName + ".jpg";
		   imageFile = new File(path);
		   if (!imageFile.exists()) {
		       try {
		           imageFile.createNewFile();
		       } catch (IOException e) {
		           e.printStackTrace();
		           return null;
		       }
		   }
		   return Uri.fromFile(imageFile);
	   }else{ // If sd card is not available
		   return null;
	   }
	}
   
	@Override
	public void onLocationChanged(Location location) {
		
		latitude = location.getLatitude();
		longitude = location.getLongitude();
		
		mainLat.setText("Latitude: "+String.valueOf(latitude));
		mainLon.setText("Longitude: " + String.valueOf(longitude)); 
	}
	@Override
	public void onProviderDisabled(String provider) {
		///Toast.makeText(getBaseContext(), "Gps turned off ", Toast.LENGTH_LONG).show();
	}
	@Override
	public void onProviderEnabled(String provider) {
		//Toast.makeText(getBaseContext(), "Gps turned on ", Toast.LENGTH_LONG).show();
	}
	@Override
	public void onStatusChanged(String arg0, int arg1, Bundle arg2) {
	}
	
	@Override
	public boolean onKeyDown(int keyCode, KeyEvent event) {
	    if (keyCode == KeyEvent.KEYCODE_BACK) {
	    	Intent intent = new Intent(this, HomeScreen.class);
	        startActivity(intent);
	    	System.exit(0);
	        return true;
	    }
	    return super.onKeyDown(keyCode, event);
	}

	@Override
	public void onClick(DialogInterface arg0, int arg1) {

		switch(arg1){
		case DialogInterface.BUTTON_POSITIVE:
			break;
		case DialogInterface.BUTTON_NEGATIVE:
			break;
		}
	}	
}