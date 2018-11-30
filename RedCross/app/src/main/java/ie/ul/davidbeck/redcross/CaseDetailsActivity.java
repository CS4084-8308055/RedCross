package ie.ul.davidbeck.redcross;

import android.content.Context;
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
    public void handleCritical(final View view){
        Map<String, Object> newCase = new HashMap<>();

        newCase.put(Constants.KEY_TREATING_STATION, mStation.getText().toString());
        newCase.put(Constants.KEY_NAME, mName.getText().toString());
        newCase.put(Constants.KEY_AGE, mAge.getText().toString());
        newCase.put(Constants.KEY_COMPLAINT, mComplaint.getText().toString());
        newCase.put(Constants.KEY_TIME_STARTED, new Date());
        newCase.put(Constants.KEY_OUTCOME, "");
        newCase.put(Constants.KEY_NEXT_STAGE, "");
        newCase.put(Constants.KEY_SYMPTOMS, "");
        newCase.put(Constants.KEY_ALLERGIES, "");
        newCase.put(Constants.KEY_MEDICATIONS, "");
        newCase.put(Constants.KEY_HISTORY, "");
        newCase.put(Constants.KEY_ORAL, "");
        newCase.put(Constants.KEY_EVENTS, "");
        FirebaseFirestore db = FirebaseFirestore.getInstance();
        db.collection(Constants.COLLECTION_ROOT)
                .document(mDocId)
                .collection(Constants.COLLECTION_CASE)
                .add(newCase);
        AlertDialog.Builder builder;
        builder = new AlertDialog.Builder(this);
        builder.setMessage("Call an ambulance and begin treatment immediately.")
                .setPositiveButton(android.R.string.yes, new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int which) {
                        callTreatment(view);
                    }
                })
                .show();
    }
    public void handleNonCritical(View view){
        Context c = view.getContext();
        Intent intent = new Intent(c, MedicalHistoryActivity.class);
        Bundle extras = new Bundle();
        extras.putString(Constants.EXTRA_DOC_ID, mDocId);
        extras.putString(Constants.EXTRA_CALLSIGN, mStation.getText().toString());
        extras.putString(Constants.EXTRA_NAME, mName.getText().toString());
        extras.putString(Constants.EXTRA_AGE, mAge.getText().toString());
        extras.putString(Constants.EXTRA_COMPLAINT, mComplaint.getText().toString());
        intent.putExtras(extras);
        c.startActivity(intent);
    }

    private void callTreatment(View view) {
        Context c = view.getContext();
        Intent intent = new Intent(c, ComplaintActivity.class);
        Bundle extras = new Bundle();
        extras.putString(Constants.EXTRA_DOC_ID, mDocId);
        extras.putString(Constants.EXTRA_CALLSIGN, mStation.getText().toString());
        intent.putExtras(extras);
        c.startActivity(intent);
    }


}
