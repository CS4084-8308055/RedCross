package ie.ul.davidbeck.redcross;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.Date;
import java.util.HashMap;
import java.util.Map;

public class FinalActivity extends AppCompatActivity {

    private TextView mOutcome;
    private String mDutyDocId;
    private String mCaseDocId;
    private String mCallsign;
    private String mNextStage = "";
    private RadioGroup mRadioGroup;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_final);

        Intent receivedIntent = getIntent();
        Bundle extras = receivedIntent.getExtras();
        mDutyDocId = extras.getString(Constants.EXTRA_DOC_ID);
        mCaseDocId = extras.getString(Constants.EXTRA_CASE_DOC_ID);
        mCallsign = extras.getString(Constants.EXTRA_CALLSIGN);

        mOutcome = findViewById(R.id.itemview_outcome);
        mRadioGroup = findViewById(R.id.radioGroup);
        mRadioGroup.clearCheck();

        mRadioGroup.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @SuppressLint("ResourceType")
            @Override
            public void onCheckedChanged(RadioGroup group, int checkedId) {
                RadioButton rb = (RadioButton) group.findViewById(checkedId);
                if (rb != null && checkedId > -1) {
                    mNextStage = (String)rb.getText();
                }
            }
        });
    }
    public void handleOK(final View view){
        if (mOutcome.getText().toString() == "" || mNextStage == ""){
            Toast.makeText(FinalActivity.this, "You must complete this page", Toast.LENGTH_LONG).show();
        }else {

            Map<String, Object> update = new HashMap<>();

            update.put(Constants.KEY_TIME_FINISHED, new Date());
            update.put(Constants.KEY_OUTCOME, mOutcome.getText().toString());
            update.put(Constants.KEY_NEXT_STAGE, mNextStage);
            FirebaseFirestore db = FirebaseFirestore.getInstance();
            DocumentReference caseDocId = db.collection(Constants.COLLECTION_ROOT)
                    .document(mDutyDocId)
                    .collection(Constants.COLLECTION_CASE)
                    .document(mCaseDocId);
            caseDocId.update(update);
            Context c = view.getContext();
            Intent intent = new Intent(c, DutyActivity.class);
            Bundle extras = new Bundle();
            extras.putString(Constants.EXTRA_DOC_ID, mDutyDocId);
            extras.putString(Constants.EXTRA_CASE_DOC_ID, mCaseDocId);
            extras.putString(Constants.EXTRA_CALLSIGN, mCallsign);
            intent.putExtras(extras);
            c.startActivity(intent);
        }
    }
}
