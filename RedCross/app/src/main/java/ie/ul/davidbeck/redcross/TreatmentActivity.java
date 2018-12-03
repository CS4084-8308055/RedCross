package ie.ul.davidbeck.redcross;

import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.TextView;

public class TreatmentActivity extends AppCompatActivity {

    private String mDutyDocId;
    private String mCaseDocId;
    private String mComplaintDocId;
    private String mCallsign;

    TextView mTreatment;
    TextView mComment;
    TextView mQuestion;
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
        mCallsign = extras.getString(Constants.EXTRA_CALLSIGN);

        mTreatment = findViewById(R.id.itemview_treatment);
        mComment = findViewById(R.id.itemview_comment);
        mQuestion = findViewById(R.id.itemview_question);

        mTreatment.setText(mDutyDocId + "/n" + mCaseDocId + "/n" + mComplaintDocId);

        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Snackbar.make(view, "Replace with your own action", Snackbar.LENGTH_LONG)
                        .setAction("Action", null).show();
            }
        });
    }

}
