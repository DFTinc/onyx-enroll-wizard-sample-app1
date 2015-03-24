package com.example.onyx_enroll_wizard_sample_app;

import java.io.File;

import android.app.AlertDialog;
import android.app.ListActivity;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.Toast;

import com.dft.onyx.FingerprintTemplate;
import com.dft.onyx.core;
import com.dft.onyx.enroll.util.Consts;
import com.dft.onyx.enroll.util.EnrolledFingerprintDetails;
import com.dft.onyx.verify.VerifyIntentHelper;
import com.dft.onyx.wizardroid.enrollwizard.SelfEnrollIntentHelper;

import org.opencv.android.OpenCVLoader;

public class OnyxEnrollWizardSampleMainActivity extends ListActivity {
	private static final int ENROLL_REQUEST_CODE = 20342;
	private static final int VERIFY_REQUEST_CODE = 1337;
	private String[] mMainMenuArray;
	private ArrayAdapter<String> mMainMenuArrayAdapter;

    private static final String TAG = "OnyxEnrollWizardSample";

    static {
        if(!OpenCVLoader.initDebug()) {
            Log.d(TAG, "Unable to load OpenCV!");
        } else {
            Log.i(TAG, "OpenCV loaded successfully");
            core.initOnyx();
        }
    }

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_onyx_enroll_wizard_sample_main);
	}
	
	@Override
	public void onResume() {
		super.onResume();
		mMainMenuArray = getResources().getStringArray(R.array.array_main_menu);
		mMainMenuArrayAdapter = new ArrayAdapter<String>(this,
		        android.R.layout.simple_list_item_1, mMainMenuArray);
		setListAdapter(mMainMenuArrayAdapter);
		    
		/** This is a sample of how to retrieve the FingerprintTemplate[]
		/* created by the Enrollment Wizard 
		 */
	   if (fingerprintExists()) {
		   FingerprintTemplate[] enrolledFingerprintTemplateArray = EnrolledFingerprintDetails.getInstance()
					.getEnrolledEnrollmentMetric(this).getFingerprintTemplateArray();
			// You can then retrieve individual FingerprintTemplate objects from the FingerprintTemplateVector
			for (int i = 0; i < enrolledFingerprintTemplateArray.length; i++) {
				FingerprintTemplate fingerprintTemplate = enrolledFingerprintTemplateArray[i];
				//TODO You can do things with the individual fingerprintTemplate, such as sending to a server
			}
	   }
	}
	
	@Override
	public void onListItemClick(ListView lv, View v, int position, long id) {
		String item = lv.getItemAtPosition(position).toString();
		if (item.equals(getResources().getString(R.string.array_main_menu_enroll))) {
			// First, generate an intent for the OnyxGuideActivity
            Intent onyxSelfEnrollIntent = new SelfEnrollIntentHelper().getSelfEnrollIntent(this,
                    getString(R.string.onyx_license));
			// Then start it for result
			startActivityForResult(onyxSelfEnrollIntent, ENROLL_REQUEST_CODE);
		}
		if (item.equals(getResources().getString(R.string.array_main_menu_validate))) {
			if (fingerprintExists()) {
				Intent verifyIntent = VerifyIntentHelper.getVerifyActivityIntent(
						this, getString(R.string.onyx_license));
				startActivityForResult(verifyIntent, VERIFY_REQUEST_CODE);	
			} else {
				Toast.makeText(this, getResources().getString(R.string.toast_no_enrolled_fingerprint),
						Toast.LENGTH_LONG).show();
			}
		}
		if (item.equals(getResources().getString(R.string.array_main_menu_clear_enrollment))) {
			if (!fingerprintExists()) {
				Toast.makeText(this, getResources().getString(R.string.toast_no_enrolled_fingerprint),
						Toast.LENGTH_LONG).show();
			} else {
				AlertDialog.Builder alertClearEnroll = new AlertDialog.Builder(this);
				alertClearEnroll.setTitle(R.string.alert_clear_enroll_title);
				alertClearEnroll.setMessage(R.string.alert_clear_enroll_message).setCancelable(false);
				alertClearEnroll.setPositiveButton(android.R.string.yes, new DialogInterface.OnClickListener() {
					public void onClick(DialogInterface dialog, int id){
						deleteEnrolledTemplateIfExists();
					}
				});
				
				alertClearEnroll.setNegativeButton(android.R.string.no, new DialogInterface.OnClickListener() {
					public void onClick(DialogInterface dialog, int id) {
						dialog.cancel();
					}
				});
				
				AlertDialog alertDialog = alertClearEnroll.create();
				alertDialog.show();
			}
		}
    }
	
	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		super.onActivityResult(requestCode, resultCode, data);
		if (requestCode == ENROLL_REQUEST_CODE) {
			if (resultCode == RESULT_OK) {
				// Successfully enrolled
				Toast.makeText(this, getResources().getString(R.string.toast_enroll_success_message), Toast.LENGTH_LONG).show();
			} else {
				// Did not successfully enroll
				Toast.makeText(this, getResources().getString(R.string.toast_enroll_fail_message), Toast.LENGTH_LONG).show();
			}
		}
		
		if (requestCode == VERIFY_REQUEST_CODE) {
			if (resultCode == RESULT_OK) {
				// Successfully verified
				Toast.makeText(this, getResources().getString(R.string.toast_verify_success_message), Toast.LENGTH_LONG).show();
			} else {
				// Did not successfully verify
				Toast.makeText(this, getResources().getString(R.string.toast_verify_fail_message), Toast.LENGTH_LONG).show();
			}
		}
	}
	
	static File mEnrolledFile = null;
	
	private boolean fingerprintExists() {
        mEnrolledFile = getFileStreamPath(Consts.ENROLLED_ENROLLMENT_METRIC_FILENAME);
        if (mEnrolledFile.exists()) {
        	return true;
        } else {
        	return false;
        }
	}
	
    private void deleteEnrolledTemplateIfExists() {
    	if (fingerprintExists()) {
            mEnrolledFile.delete();
            Toast.makeText(this, getResources().getString(R.string.toast_deleting_enrolled_fingerprint),
            		Toast.LENGTH_LONG).show();
        }
    }
}
