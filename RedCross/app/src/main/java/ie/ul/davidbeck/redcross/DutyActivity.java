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

public class DutyActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_duty);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        Intent receivedIntent = getIntent();
        final String docId = receivedIntent.getStringExtra(Constants.EXTRA_DOC_ID);

        RecyclerView dutyRecyclerView = findViewById(R.id.duty_recycler_view);
        dutyRecyclerView.setLayoutManager(new LinearLayoutManager(this));
        dutyRecyclerView.setHasFixedSize(true);

        CaseAdapter caseAdapter = new CaseAdapter(docId);
        dutyRecyclerView.setAdapter(caseAdapter);


        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Context c = view.getContext();
                Intent intent = new Intent(c, CaseDetailsActivity.class);
                intent.putExtra(Constants.EXTRA_DOC_ID, docId);
                c.startActivity(intent);
            }
        });
    }

}
