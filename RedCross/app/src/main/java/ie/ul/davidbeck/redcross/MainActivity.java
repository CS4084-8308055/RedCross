package ie.ul.davidbeck.redcross;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.CalendarView;
import android.widget.EditText;
import android.widget.TextView;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.FirebaseFirestore;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.HashMap;
import java.util.Map;

public class MainActivity extends AppCompatActivity {

    Date mSearchDate = null;
    String mCallsign;
    String mLocation = "";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
        try {
            mSearchDate = sdf.parse(sdf.format(new Date()));
        } catch (ParseException e) {
            e.printStackTrace();
        }
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        String uid = FirebaseAuth.getInstance().getCurrentUser().getEmail();
        mCallsign = uid.substring(0, uid.indexOf("@"));

        callAdapter(Constants.SEARCH_DATE);

        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                showAddDialog(mCallsign);
            }
        });
    }

    private void callAdapter(int searchType) {
        RecyclerView recyclerView = findViewById(R.id.recycler_view);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        recyclerView.setHasFixedSize(true);

        DutyAdapter dutyAdapter = new DutyAdapter(mCallsign, mSearchDate, mLocation, searchType);
        recyclerView.setAdapter(dutyAdapter);
    }

    private void showAddDialog(final String callsign) {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        View view = getLayoutInflater().inflate(R.layout.addduty_dialog, null, false);
        builder.setView(view);
        builder.setTitle("Create Duty");
        final TextView quoteEditText = view.findViewById(R.id.dialog_location_edittext);
        builder.setPositiveButton(android.R.string.ok, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
                Date dutyDate = null;
                try {
                    dutyDate = sdf.parse(sdf.format(new Date()));
                } catch (ParseException e) {
                    e.printStackTrace();
                }
                Map<String, Object> duty = new HashMap<>();
                duty.put(Constants.KEY_LOCATION, quoteEditText.getText().toString());
                duty.put(Constants.KEY_CREATEDBY, callsign);
                duty.put(Constants.KEY_DUTYDATE, dutyDate);
                FirebaseFirestore.getInstance().collection(Constants.COLLECTION_ROOT).add(duty);

            }
        });
        builder.setNegativeButton(android.R.string.cancel, null);

        builder.create().show();
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()){
            case R.id.action_logout:
                FirebaseAuth.getInstance().signOut();
                Context c = this;
                Intent intent = new Intent(c, LoginActivity.class);
                c.startActivity(intent);
                return true;
            case R.id.action_date_search:
                showDateSearchDialog();
                return true;
            case R.id.action_loc_search:
                showLocSearchDialog();
                return true;
        }

        return super.onOptionsItemSelected(item);
    }

    private void showDateSearchDialog() {
        android.app.AlertDialog.Builder builder = new android.app.AlertDialog.Builder(this);

        View view = getLayoutInflater().inflate(R.layout.dialog_date_search, null, false);
        builder.setView(view);
        final CalendarView deliveryDateView = view.findViewById(R.id.calendar_view);
        final GregorianCalendar calendar = new GregorianCalendar();

        deliveryDateView.setOnDateChangeListener(new CalendarView.OnDateChangeListener() {
            @Override
            public void onSelectedDayChange(@NonNull CalendarView view, int year, int month, int dayOfMonth) {
                calendar.set(year, month, dayOfMonth);
            }
        });

        builder.setPositiveButton(android.R.string.ok, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
                try {
                    mSearchDate = sdf.parse(sdf.format(calendar.getTime()));
                } catch (ParseException e) {
                    e.printStackTrace();
                }
                String strDate = mSearchDate.toString();
                callAdapter(Constants.SEARCH_DATE);
            }
        });
        builder.setNegativeButton(android.R.string.cancel, null);
        builder.create().show();

    }

    private void showLocSearchDialog() {
        android.app.AlertDialog.Builder builder = new android.app.AlertDialog.Builder(this);

        View view = getLayoutInflater().inflate(R.layout.dialog_location_search, null, false);
        builder.setView(view);
        final TextView locationView = view.findViewById(R.id.dialog_view_location);

        builder.setPositiveButton(android.R.string.ok, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                mLocation = locationView.getText().toString();
                callAdapter(Constants.SEARCH_LOCATION);
            }
        });
        builder.setNegativeButton(android.R.string.cancel, null);
        builder.create().show();

    }
    @Override
    public void onBackPressed() {
    }
}
