package ie.ul.davidbeck.redcross;

import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.TextView;

public class CaseDetailsActivity extends AppCompatActivity {

    private TextView mStation;
    private TextView mName;
    private TextView mAge;
    private TextView mComplaint;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_case_details);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        Intent receivedIntent = getIntent();
        final String docId = receivedIntent.getStringExtra(Constants.EXTRA_DOC_ID);


        mStation = findViewById(R.id.detail_view_treating_station);
        mName = findViewById(R.id.detail_view_name);
        mAge = findViewById(R.id.detail_view_age);
        mComplaint = findViewById(R.id.detail_view_complaint);

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
