package ie.ul.davidbeck.redcross;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;

public class ComplaintActivity extends AppCompatActivity {

    private String mDutyDocId;
    private String mCaseDocId;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_complaint);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        Intent receivedIntent = getIntent();
        Bundle extras = receivedIntent.getExtras();
        mDutyDocId = extras.getString(Constants.EXTRA_DOC_ID);
        mCaseDocId = extras.getString(Constants.EXTRA_CASE_DOC_ID);
        final String callsign = extras.getString(Constants.EXTRA_CALLSIGN);

        RecyclerView complaintRecyclerView = findViewById(R.id.complaint_recycler_view);
        complaintRecyclerView.setLayoutManager(new LinearLayoutManager(this));
        complaintRecyclerView.setHasFixedSize(true);

        ComplaintAdapter complaintAdapter = new ComplaintAdapter(mDutyDocId, mCaseDocId, callsign);
        complaintRecyclerView.setAdapter(complaintAdapter);

    }

}
