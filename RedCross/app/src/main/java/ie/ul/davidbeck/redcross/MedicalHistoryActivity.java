package ie.ul.davidbeck.redcross;

import android.content.DialogInterface;
import android.content.Intent;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;

import com.google.firebase.firestore.FirebaseFirestore;

import java.util.Date;
import java.util.HashMap;
import java.util.Map;

public class MedicalHistoryActivity extends AppCompatActivity {

    private TextView mSymptoms;
    private TextView mAllergies;
    private TextView mMedication;
    private TextView mHistory;
    private TextView mOral;
    private TextView mEvents;

    private String mDocId;
    private String mCallsign;
    private String mName;
    private int mAge;
    private String mComplaint;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_medical_history);

        Intent receivedIntent = getIntent();
        Bundle extras = receivedIntent.getExtras();
        mDocId = extras.getString(Constants.EXTRA_DOC_ID);
        mCallsign = extras.getString(Constants.EXTRA_CALLSIGN);
        mName = extras.getString(Constants.EXTRA_NAME);
        mAge = Integer.parseInt(extras.getString(Constants.EXTRA_AGE));
        mCallsign = extras.getString(Constants.EXTRA_CALLSIGN);

        mSymptoms = findViewById(R.id.detail_view_symptoms);
        mAllergies = findViewById(R.id.detail_view_allergies);
        mMedication = findViewById(R.id.detail_view_medications);
        mHistory = findViewById(R.id.detail_view_history);
        mOral = findViewById(R.id.detail_view_oral);
        mEvents = findViewById(R.id.detail_view_events);

    }

    public void handleContinue(final View view){
        Map<String, Object> newCase = new HashMap<>();

        newCase.put(Constants.KEY_TREATING_STATION, mCallsign);
        newCase.put(Constants.KEY_NAME, mName);
        newCase.put(Constants.KEY_AGE, mAge);
        newCase.put(Constants.KEY_COMPLAINT, mComplaint);
        newCase.put(Constants.KEY_TIME_STARTED, new Date());
        newCase.put(Constants.KEY_OUTCOME, "");
        newCase.put(Constants.KEY_NEXT_STAGE, "");
        newCase.put(Constants.KEY_SYMPTOMS, mSymptoms.getText().toString());
        newCase.put(Constants.KEY_ALLERGIES, mAllergies.getText().toString());
        newCase.put(Constants.KEY_MEDICATIONS, mMedication.getText().toString());
        newCase.put(Constants.KEY_HISTORY, mHistory.getText().toString());
        newCase.put(Constants.KEY_ORAL, mOral.getText().toString());
        newCase.put(Constants.KEY_EVENTS, mEvents.getText().toString());
        FirebaseFirestore db = FirebaseFirestore.getInstance();
        db.collection(Constants.COLLECTION_ROOT)
                .document(mDocId)
                .collection(Constants.COLLECTION_CASE)
                .add(newCase);
    }

}
