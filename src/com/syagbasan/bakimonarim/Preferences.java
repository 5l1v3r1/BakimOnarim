package com.syagbasan.bakimonarim;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.DialogInterface.OnClickListener;
import android.content.SharedPreferences;
import android.content.SharedPreferences.OnSharedPreferenceChangeListener;
import android.os.Bundle;
import android.preference.EditTextPreference;
import android.preference.Preference;
import android.preference.PreferenceActivity;
import android.view.KeyEvent;

public class Preferences extends PreferenceActivity implements
OnSharedPreferenceChangeListener, OnClickListener {

	@SuppressWarnings("deprecation")
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		addPreferencesFromResource(R.xml.pref);
		
		SharedPreferences sp = getPreferenceScreen().getSharedPreferences();
        
		SharedPreferences sp2 = getPreferenceScreen().getSharedPreferences();
        
		SharedPreferences sp3 = getPreferenceScreen().getSharedPreferences();
        
		SharedPreferences sp4 = getPreferenceScreen().getSharedPreferences();
        
		SharedPreferences sp5 = getPreferenceScreen().getSharedPreferences();
		
		SharedPreferences sp6 = getPreferenceScreen().getSharedPreferences();
        
		SharedPreferences sp7 = getPreferenceScreen().getSharedPreferences();
		
		SharedPreferences sp8 = getPreferenceScreen().getSharedPreferences();
        
		SharedPreferences sp9 = getPreferenceScreen().getSharedPreferences();
		
		SharedPreferences sp10 = getPreferenceScreen().getSharedPreferences();
        
		SharedPreferences sp11 = getPreferenceScreen().getSharedPreferences();
        
		EditTextPreference editTextPref = (EditTextPreference) findPreference("editnamesurname");
        editTextPref
                .setSummary(sp.getString("editnamesurname", ""));
        
        
        EditTextPreference editTextPref2 = (EditTextPreference) findPreference("editFTPhost");
        editTextPref2
                .setSummary(sp6.getString("editFTPhost", ""));
        
        EditTextPreference editTextPref3 = (EditTextPreference) findPreference("editFTPport");
        editTextPref3
                .setSummary(sp7.getString("editFTPport", ""));
        
        EditTextPreference editTextPref6 = (EditTextPreference) findPreference("editFTPuser");
        editTextPref6
                .setSummary(sp2.getString("editFTPuser", ""));
        
        EditTextPreference editTextPref7 = (EditTextPreference) findPreference("editFTPpass");
        editTextPref7
                .setSummary(sp3.getString("editFTPpass", ""));
        
        
        EditTextPreference editTextPref4 = (EditTextPreference) findPreference("editSQLhost");
        editTextPref4
                .setSummary(sp4.getString("editSQLhost", ""));
        
        EditTextPreference editTextPref5 = (EditTextPreference) findPreference("editSQLport");
        editTextPref5
                .setSummary(sp5.getString("editSQLport", ""));
        
        EditTextPreference editTextPref8 = (EditTextPreference) findPreference("editSQLuser");
        editTextPref8
                .setSummary(sp8.getString("editSQLuser", ""));
        
        EditTextPreference editTextPref9 = (EditTextPreference) findPreference("editSQLpass");
        editTextPref9
                .setSummary(sp9.getString("editSQLpass", ""));
        
        EditTextPreference editTextPref10 = (EditTextPreference) findPreference("editSQLdatabasename");
        editTextPref10
                .setSummary(sp10.getString("editSQLdatabasename", ""));
        
        EditTextPreference editTextPref11 = (EditTextPreference) findPreference("editSQLtablename");
        editTextPref11
                .setSummary(sp11.getString("editSQLtablename", ""));
        
	}

    @SuppressWarnings("deprecation")
	protected void onResume() {
        super.onResume();
        getPreferenceScreen().getSharedPreferences()
                .registerOnSharedPreferenceChangeListener(this);
    }

    @SuppressWarnings("deprecation")
	protected void onPause() {
        super.onPause();
        getPreferenceScreen().getSharedPreferences()
                .unregisterOnSharedPreferenceChangeListener(this);
    }

	@Override
	public void onSharedPreferenceChanged(SharedPreferences sharedPreferences,
			String key) {
		@SuppressWarnings("deprecation")
		Preference pref = findPreference(key);
	    if (pref instanceof EditTextPreference) {
	         EditTextPreference etp = (EditTextPreference) pref;
	         pref.setSummary(etp.getText());
	    }
	}
	
	@Override
	public boolean onKeyDown(int keyCode, KeyEvent event) {
	    if (keyCode == KeyEvent.KEYCODE_BACK) {
	    	AlertDialog ad = new AlertDialog.Builder(this)
			.setMessage("Bilgilerinizi doğru girdiğinize emin misiniz?")
			.setIcon(R.drawable.ic_launcher)
			.setTitle("Uyarı:")
			.setNegativeButton("Hayır", this)
			.setPositiveButton("Evet", this)
			.setCancelable(false)
			.create();
			ad.show();
	        return true;
	    }
	    return super.onKeyDown(keyCode, event);
	}

	@Override
	public void onClick(DialogInterface dialog, int which) {
		
		switch(which){
			case DialogInterface.BUTTON_POSITIVE: // yes
				onBackPressed();
			break;
			case DialogInterface.BUTTON_NEGATIVE: // no
				
			break;
			default:
				// nothing
			break;
		}
	}
}