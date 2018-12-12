package ie.ul.davidbeck.redcross;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.View;

public class ViewTreatmentActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_view_treatment);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        Intent receivedIntent = getIntent();
        Bundle extras = receivedIntent.getExtras();
        final String dutyDocId = extras.getString(Constants.EXTRA_DOC_ID);
        final String caseDocId = extras.getString(Constants.EXTRA_CASE_DOC_ID);

        Context context = this;

        RecyclerView treatmentRecyclerView = findViewById(R.id.treatment_recycler_view);
        treatmentRecyclerView.setLayoutManager(new LinearLayoutManager(this));
        treatmentRecyclerView.setHasFixedSize(true);

        TreatmentAdapter treatmentAdapter = new TreatmentAdapter(dutyDocId, caseDocId, context);
        treatmentRecyclerView.setAdapter(treatmentAdapter);

    }

}
