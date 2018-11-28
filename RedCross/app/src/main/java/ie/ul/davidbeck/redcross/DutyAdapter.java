package ie.ul.davidbeck.redcross;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.EventListener;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.FirebaseFirestoreException;
import com.google.firebase.firestore.QuerySnapshot;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class DutyAdapter extends RecyclerView.Adapter<DutyAdapter.DutyViewHolder> {

    private List<DocumentSnapshot> mDutySnapshots = new ArrayList<>();
    private String mCallsign;

    public DutyAdapter(String callsign){
        SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy");
        Date dutyDate = null;
        mCallsign = callsign;
        try {
            dutyDate = sdf.parse(sdf.format(new Date()));
        } catch (ParseException e) {
            e.printStackTrace();
        }
        CollectionReference dutiesCollectionRef = FirebaseFirestore.getInstance().
                collection(Constants.COLLECTION_ROOT);

        dutiesCollectionRef.whereEqualTo("DutyDate", dutyDate).addSnapshotListener(new EventListener<QuerySnapshot>() {
            @Override
            public void onEvent(QuerySnapshot documentSnapshots, FirebaseFirestoreException e) {
                if (e != null) {
                    Log.w(Constants.TAG, "Listening failed");
                    return;
                }
                mDutySnapshots = documentSnapshots.getDocuments();
                notifyDataSetChanged();
            }
        });
    }
    @NonNull
    @Override
    public DutyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int i) {
        View itemView = LayoutInflater.from(parent.getContext()).inflate(R.layout.duties_itemview, parent, false);
        return new DutyViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(@NonNull DutyViewHolder dutyViewHolder, int i) {
        SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy");
        try {
            Date dutyDate = sdf.parse(sdf.format(new Date()));
        } catch (ParseException e) {
            e.printStackTrace();
        }
        DocumentSnapshot ds = mDutySnapshots.get(i);
        String location = (String)ds.get(Constants.KEY_LOCATION);
        String dutyDate = sdf.format((Date)ds.get(Constants.KEY_DUTYDATE));
        dutyViewHolder.mDutyDateTextView.setText(dutyDate);
        dutyViewHolder.mLocationTextView.setText(location);
    }

    @Override
    public int getItemCount() {
        return mDutySnapshots.size();
    }

    class DutyViewHolder extends RecyclerView.ViewHolder {
        private TextView mLocationTextView;
        private TextView mDutyDateTextView;

        public DutyViewHolder(@NonNull View itemView) {
            super(itemView);
            mLocationTextView = itemView.findViewById(R.id.itemview_location);
            mDutyDateTextView = itemView.findViewById(R.id.itemview_dutydate);
            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    DocumentSnapshot ds = mDutySnapshots.get(getAdapterPosition());
                    Context c = view.getContext();
                    Intent intent = new Intent(c, DutyActivity.class);
                    Bundle extras = new Bundle();
                    extras.putString(Constants.EXTRA_DOC_ID, ds.getId());
                    extras.putString(Constants.EXTRA_CALLSIGN, mCallsign);
                    intent.putExtras(extras);
                    c.startActivity(intent);
                }
            });
        }
    }
}
