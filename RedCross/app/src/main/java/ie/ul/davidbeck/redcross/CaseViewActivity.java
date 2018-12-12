package ie.ul.davidbeck.redcross;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.widget.TextView;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.Query;
import com.google.firebase.firestore.QuerySnapshot;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

public class CaseViewActivity extends AppCompatActivity {

    private String mCallsign;
    private String mDutyDocId;
    private String mComplaint;
    private String mCaseDocId;
    private String mComplaintDocId;
    private Long mStepId;

    private TextView mNameView;
    private TextView mAgeView;
    private TextView mComplaintView;
    private TextView mTimeView;
    private TextView mOutcomeView;
    private TextView mNextStageView;
    private TextView mSymptomsView;
    private TextView mAllergiesView;
    private TextView mMedicationView;
    private TextView mHistoryView;
    private TextView mOralView;
    private TextView mEventsView;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_case_view);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);


        Intent receivedIntent = getIntent();
        Bundle extras = receivedIntent.getExtras();
        mDutyDocId = extras.getString(Constants.EXTRA_DOC_ID);
        mCaseDocId = extras.getString(Constants.EXTRA_CASE_DOC_ID);
        mComplaint = extras.getString(Constants.EXTRA_COMPLAINT);
        mCallsign = extras.getString(Constants.EXTRA_CALLSIGN);

        mNameView = findViewById(R.id.name_view);
        mAgeView = findViewById(R.id.age_view);
        mComplaintView = findViewById(R.id.complaint_view);
        mTimeView = findViewById(R.id.time_view);
        mOutcomeView = findViewById(R.id.outcome_view);
        mNextStageView = findViewById(R.id.next_stage_view);
        mSymptomsView = findViewById(R.id.symptoms_view);
        mAllergiesView = findViewById(R.id.allergies_view);
        mMedicationView = findViewById(R.id.medication_view);
        mHistoryView = findViewById(R.id.history_view);
        mOralView = findViewById(R.id.oral_view);
        mEventsView = findViewById(R.id.events_view);

        FirebaseFirestore db = FirebaseFirestore.getInstance();

        DocumentReference thisCase = db.collection(Constants.COLLECTION_ROOT).document(mDutyDocId)
                .collection(Constants.COLLECTION_CASE).document(mCaseDocId);
        thisCase.get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
            @Override
            public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                if (task.isSuccessful()){
                    SimpleDateFormat sdf = new SimpleDateFormat("HH:mm");
                    DocumentSnapshot caseSnapshot = task.getResult();

                    mNameView.setText((String)caseSnapshot.get(Constants.KEY_NAME));
                    mAgeView.setText((String)caseSnapshot.get(Constants.KEY_AGE));
                    mComplaintView.setText((String)caseSnapshot.get(Constants.KEY_COMPLAINT));
                    String timeStarted = sdf.format((Date)caseSnapshot.get(Constants.KEY_TIME_STARTED));
                    mTimeView.setText(timeStarted);
                    mOutcomeView.setText((String)caseSnapshot.get(Constants.KEY_OUTCOME));
                    mNextStageView.setText((String)caseSnapshot.get(Constants.KEY_NEXT_STAGE));
                    mSymptomsView.setText((String)caseSnapshot.get(Constants.KEY_SYMPTOMS));
                    mAllergiesView.setText((String)caseSnapshot.get(Constants.KEY_ALLERGIES));
                    mMedicationView.setText((String)caseSnapshot.get(Constants.KEY_MEDICATIONS));
                    mHistoryView.setText((String)caseSnapshot.get(Constants.KEY_HISTORY));
                    mOralView.setText((String)caseSnapshot.get(Constants.KEY_ORAL));
                    mEventsView.setText((String)caseSnapshot.get(Constants.KEY_EVENTS));
                    mStepId = (Long)caseSnapshot.get(Constants.KEY_STEP_ID);
                }
            }
        });


    }
    public void handleContinue(final View view){

        FirebaseFirestore db = FirebaseFirestore.getInstance();
        db.collection("Complaints")
                .whereEqualTo("Complaint", mComplaint)
                .get()
                .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<QuerySnapshot> task) {
                        if (task.isSuccessful()) {
                            mComplaintDocId = task.getResult().getDocuments().get(0).getId();
                            Context c = view.getContext();
                            Intent intent = new Intent(c, TreatmentActivity.class);
                            Bundle extras = new Bundle();
                            extras.putString(Constants.EXTRA_DOC_ID, mDutyDocId);
                            extras.putString(Constants.EXTRA_CASE_DOC_ID, mCaseDocId);
                            extras.putString(Constants.EXTRA_COMPLAINT_DOC_ID, mComplaintDocId);
                            extras.putString(Constants.EXTRA_CALLSIGN, mCallsign);
                            extras.putLong(Constants.EXTRA_STEP_ID, mStepId);
                            intent.putExtras(extras);
                            c.startActivity(intent);
                        }
                    }
                });


    }

    public void handleTreatments(final View view){
        Context c = view.getContext();
        Intent intent = new Intent(c, ViewTreatmentActivity.class);
        Bundle extras = new Bundle();
        extras.putString(Constants.EXTRA_DOC_ID, mDutyDocId);
        extras.putString(Constants.EXTRA_CASE_DOC_ID, mCaseDocId);
        intent.putExtras(extras);
        c.startActivity(intent);

    }

}
