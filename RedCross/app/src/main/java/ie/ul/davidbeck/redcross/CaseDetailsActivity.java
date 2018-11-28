package ie.ul.davidbeck.redcross;

import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.TextView;

import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.HashMap;
import java.util.Map;

public class CaseDetailsActivity extends AppCompatActivity {

    private TextView mStation;
    private TextView mName;
    private TextView mAge;
    private TextView mComplaint;
    private String mDocId;


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
        writeData();
    }
    public void handleNonCritical(View v){
        writeData();
    }

    private void writeData(){
        Map<String, Object> newCase = new HashMap<>();
        newCase.put(Constants.KEY_TREATING_STATION, mStation.getText());
        newCase.put(Constants.KEY_NAME, mName.getText());
        newCase.put(Constants.KEY_AGE, mAge.getText());
        newCase.put(Constants.KEY_COMPLAINT, mComplaint.getText());
        newCase.put(Constants.KEY_TIME_STARTED, mAge);
        newCase.put(Constants.KEY_OUTCOME, "");
        newCase.put(Constants.KEY_NEXT_STAGE, "");
        FirebaseFirestore.getInstance().collection(Constants.COLLECTION_ROOT)
                .document(mDocId).collection(Constants.COLLECTION_CASE).add(newCase);
    }
}
