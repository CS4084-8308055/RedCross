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
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.TextView;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.EventListener;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.FirebaseFirestoreException;
import com.google.firebase.firestore.Query;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class TreatmentActivity extends AppCompatActivity {

    private String mDutyDocId;
    private String mCaseDocId;
    private String mComplaintDocId;
    private String mCallsign;
    private String mComplaint;
    private long mStepId;
    private long mStepIfYes;
    private long mStepIfNo;

    TextView mTreatment;
    TextView mComment;
    TextView mQuestion;

    private List<DocumentSnapshot> mStepSnapshots = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_treatment);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        Intent receivedIntent = getIntent();
        Bundle extras = receivedIntent.getExtras();
        mDutyDocId = extras.getString(Constants.EXTRA_DOC_ID);
        mCaseDocId = extras.getString(Constants.EXTRA_CASE_DOC_ID);
        mComplaintDocId = extras.getString(Constants.EXTRA_COMPLAINT_DOC_ID);
        mComplaint = extras.getString(Constants.EXTRA_COMPLAINT);
        mCallsign = extras.getString(Constants.EXTRA_CALLSIGN);
        mStepId = extras.getLong(Constants.EXTRA_STEP_ID);

        mTreatment = findViewById(R.id.itemview_treatment);
        mComment = findViewById(R.id.itemview_comment);
        mQuestion = findViewById(R.id.itemview_question);

        FirebaseFirestore db = FirebaseFirestore.getInstance();

        Map<String, Object> updateCase = new HashMap<>();
        updateCase.put(Constants.KEY_STEP_ID, mStepId);
        db.collection(Constants.COLLECTION_ROOT)
                .document(mDutyDocId)
                .collection(Constants.COLLECTION_CASE)
                .document(mCaseDocId)
                .update(updateCase);



        Query thisStep = db.collection(Constants.COLLECTION_COMPLAINT).document(mComplaintDocId)
                .collection(Constants.COLLECTION_DIAGNOSIS).whereEqualTo(Constants.KEY_STEP_ID, mStepId);
        thisStep.get().addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
            @Override
            public void onComplete(@NonNull Task<QuerySnapshot> task) {
                if (task.isSuccessful()){
                    mStepSnapshots = task.getResult().getDocuments();

                    mTreatment.setText((String)mStepSnapshots.get(0).get(Constants.KEY_TREATMENT));
                    mQuestion.setText((String)mStepSnapshots.get(0).get(Constants.KEY_QUESTION));
                    mStepIfNo = (Long)mStepSnapshots.get(0).get(Constants.KEY_NO_STEP_ID);
                    mStepIfYes = (Long)mStepSnapshots.get(0).get(Constants.KEY_YES_STEP_ID);
                }
            }
        });

    }

    public void handleYes(View view){
        callNextStep(view, mStepIfYes, "Yes");
    }

    public void handleNo(View view){
        callNextStep(view, mStepIfNo, "No");
    }

    private void callNextStep(View view, long nextStepId, String answer){
        //write to Duties\dutydoc\Cases\casedoc\treatment - TreatmentStation/Time/Complaint/Treatment/Comment/Question/Answer
        Map<String, Object> treatment = new HashMap<>();

        treatment.put(Constants.KEY_TREATING_STATION, mCallsign);
        treatment.put(Constants.KEY_TIME_STARTED, new Date());
        treatment.put(Constants.KEY_COMPLAINT, mComplaint);
        treatment.put(Constants.KEY_TREATMENT, (String)mTreatment.getText());
        treatment.put(Constants.KEY_COMMENT, mComment.getText().toString());
        treatment.put(Constants.KEY_QUESTION, (String) mQuestion.getText());
        treatment.put(Constants.KEY_ANSWER, answer);
        FirebaseFirestore db = FirebaseFirestore.getInstance();
        final DocumentReference treatmentDocId =  db.collection(Constants.COLLECTION_ROOT)
                .document(mDutyDocId)
                .collection(Constants.COLLECTION_CASE)
                .document(mCaseDocId)
                .collection(Constants.COLLECTION_TREATMENT)
                .document();
        treatmentDocId.set(treatment);

        if (nextStepId == 0){
            finaliseCase(view);
        } else {
            Context c = view.getContext();
            Intent intent = new Intent(c, TreatmentActivity.class);
            Bundle extras = new Bundle();
            extras.putString(Constants.EXTRA_DOC_ID, mDutyDocId);
            extras.putString(Constants.EXTRA_CASE_DOC_ID, mCaseDocId);
            extras.putString(Constants.EXTRA_COMPLAINT_DOC_ID, mComplaintDocId);
            extras.putString(Constants.EXTRA_COMPLAINT, mComplaint);
            extras.putString(Constants.EXTRA_CALLSIGN, mCallsign);
            extras.putLong(Constants.EXTRA_STEP_ID, nextStepId);
            intent.putExtras(extras);
            c.startActivity(intent);
        }
    }

    private void finaliseCase(View view){
        Context c = view.getContext();
        Intent intent = new Intent(c, FinalActivity.class);
        Bundle extras = new Bundle();
        extras.putString(Constants.EXTRA_DOC_ID, mDutyDocId);
        extras.putString(Constants.EXTRA_CASE_DOC_ID, mCaseDocId);
        extras.putString(Constants.EXTRA_CALLSIGN, mCallsign);
        intent.putExtras(extras);
        c.startActivity(intent);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.treatment_menu, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()){
            case R.id.action_logout:
                FirebaseAuth.getInstance().signOut();
                finish();
                return true;
            case R.id.action_cases:
//                View view = findViewById(item.getItemId());
//                Context c = view.getContext();
                Intent intent = new Intent(this, DutyActivity.class);
                Bundle extras = new Bundle();
                extras.putString(Constants.EXTRA_DOC_ID, mDutyDocId);
                extras.putString(Constants.EXTRA_CALLSIGN, mCallsign);
                intent.putExtras(extras);
                this.startActivity(intent);
                finish();
                return true;
        }

        return super.onOptionsItemSelected(item);
    }
    @Override
    public void onBackPressed() {
        Context c = this;
        Intent intent = new Intent(c, DutyActivity.class);
        Bundle extras = new Bundle();
        extras.putString(Constants.EXTRA_DOC_ID, mDutyDocId);
        extras.putString(Constants.EXTRA_CALLSIGN, mCallsign);
        intent.putExtras(extras);
        c.startActivity(intent);
    }
}

