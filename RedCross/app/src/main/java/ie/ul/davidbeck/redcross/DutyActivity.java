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
import android.util.Log;
import android.view.View;

import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.EventListener;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.FirebaseFirestoreException;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

import static java.lang.Boolean.FALSE;
import static java.lang.Boolean.TRUE;

public class DutyActivity extends AppCompatActivity {

    private String mCallsign;
    private Boolean mEditMode = TRUE;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_duty);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        Intent receivedIntent = getIntent();
        Bundle extras = receivedIntent.getExtras();
        final String docId = extras.getString(Constants.EXTRA_DOC_ID);
        final String callsign = extras.getString(Constants.EXTRA_CALLSIGN);
        mCallsign = callsign;

        DocumentReference docRef = FirebaseFirestore.getInstance().collection(Constants.COLLECTION_ROOT).document(docId);
        docRef.addSnapshotListener(new EventListener<DocumentSnapshot>() {
            @Override
            public void onEvent(DocumentSnapshot documentSnapshot, FirebaseFirestoreException e) {
                SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy");

                if (e != null) {
                    Log.w(Constants.TAG, "Listen failed!");
                    return;
                }
                if (documentSnapshot.exists()){
                    Date today = null;
                    Date dutyDate = (Date)documentSnapshot.get(Constants.KEY_DUTYDATE);
                    try {
                        today = sdf.parse(sdf.format(new Date()));
                    } catch (ParseException e1) {
                        e1.printStackTrace();
                    }
                    if (!today.equals(dutyDate)) { mEditMode = FALSE; };
                }
            }
        });

        RecyclerView dutyRecyclerView = findViewById(R.id.duty_recycler_view);
        dutyRecyclerView.setLayoutManager(new LinearLayoutManager(this));
        dutyRecyclerView.setHasFixedSize(true);

        CaseAdapter caseAdapter = new CaseAdapter(docId, callsign, mEditMode);
        dutyRecyclerView.setAdapter(caseAdapter);


        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (mEditMode) {
                    Context c = view.getContext();
                    Intent intent = new Intent(c, CaseDetailsActivity.class);
                    Bundle extras = new Bundle();
                    extras.putString(Constants.EXTRA_DOC_ID, docId);
                    extras.putString(Constants.EXTRA_CALLSIGN, callsign);
                    intent.putExtras(extras);
                    c.startActivity(intent);
                }
            }
        });
    }

    @Override
    public void onBackPressed() {
        Intent intent = new Intent(this, MainActivity.class);
        intent.putExtra(Constants.EXTRA_CALLSIGN, mCallsign);
        startActivity(intent);
    }
}
