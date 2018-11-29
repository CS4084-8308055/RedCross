package ie.ul.davidbeck.redcross;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.widget.TextView;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.Date;
import java.util.HashMap;
import java.util.Map;

public class CaseDetailsActivity extends AppCompatActivity {

    private TextView mStation;
    private TextView mName;
    private TextView mAge;
    private TextView mComplaint;
    private String mDocId;

    Map<String, Object> mNewCase = new HashMap<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_case_details);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        Intent receivedIntent = getIntent();
        Bundle extras = receivedIntent.getExtras();
        mDocId = extras.getString(Constants.EXTRA_DOC_ID);
        final String callsign = extras.getString(Constants.EXTRA_CALLSIGN);


        mStation = findViewById(R.id.detail_view_treating_station);
        mName = findViewById(R.id.detail_view_name);
        mAge = findViewById(R.id.detail_view_age);
        mComplaint = findViewById(R.id.detail_view_complaint);
        mStation.setText(callsign);

    }
    public void handleCritical(View v){
        mNewCase.put(Constants.KEY_SYMPTOMS, "");
        mNewCase.put(Constants.KEY_ALLERGIES, "");
        mNewCase.put(Constants.KEY_MEDICATIONS, "");
        mNewCase.put(Constants.KEY_HISTORY, "");
        mNewCase.put(Constants.KEY_ORAL, "");
        mNewCase.put(Constants.KEY_EVENTS, "");
        writeData();
        AlertDialog.Builder builder;
        builder = new AlertDialog.Builder(this);
        builder.setMessage("Call an ambulance and begin treatment immediately.")
                .setPositiveButton(android.R.string.yes, new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int which) {
                        // next activity
                    }
                })
                .show();
    }
    public void handleNonCritical(View v){
        showHistoryDialog();
        writeData();
        // next activity
    }

    private void writeData(){
        mNewCase.put(Constants.KEY_TREATING_STATION, mStation.getText().toString());
        mNewCase.put(Constants.KEY_NAME, mName.getText().toString());
        mNewCase.put(Constants.KEY_AGE, mAge.getText().toString());
        mNewCase.put(Constants.KEY_COMPLAINT, mComplaint.getText().toString());
        mNewCase.put(Constants.KEY_TIME_STARTED, new Date());
        mNewCase.put(Constants.KEY_OUTCOME, "");
        mNewCase.put(Constants.KEY_NEXT_STAGE, "");
        FirebaseFirestore db = FirebaseFirestore.getInstance();
        db.collection(Constants.COLLECTION_ROOT)
                .document(mDocId)
                .collection(Constants.COLLECTION_CASE)
                .add(mNewCase);

    }

    private void showHistoryDialog() {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        View view = getLayoutInflater().inflate(R.layout.patient_details_dialog, null, false);
        builder.setView(view);
        builder.setTitle("Patient Details");
        final TextView symptomsEditText = view.findViewById(R.id.dialog_symptoms_edittext);
        final TextView allergiesEditText = view.findViewById(R.id.dialog_allergies_edittext);
        final TextView medicationsEditText = view.findViewById(R.id.dialog_medications_edittext);
        final TextView historyEditText = view.findViewById(R.id.dialog_history_edittext);
        final TextView oralEditText = view.findViewById(R.id.dialog_oral_edittext);
        final TextView eventsEditText = view.findViewById(R.id.dialog_events_edittext);
        builder.setPositiveButton(android.R.string.ok, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                mNewCase.put(Constants.KEY_SYMPTOMS, symptomsEditText.getText().toString());
                mNewCase.put(Constants.KEY_ALLERGIES, allergiesEditText.getText().toString());
                mNewCase.put(Constants.KEY_MEDICATIONS, medicationsEditText.getText().toString());
                mNewCase.put(Constants.KEY_HISTORY, historyEditText.getText().toString());
                mNewCase.put(Constants.KEY_ORAL, oralEditText.getText().toString());
                mNewCase.put(Constants.KEY_EVENTS, eventsEditText.getText().toString());

            }
        });
        builder.create().show();
    }

}
