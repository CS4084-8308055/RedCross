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
import com.google.firebase.firestore.Query;
import com.google.firebase.firestore.QuerySnapshot;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class CaseAdapter extends RecyclerView.Adapter<CaseAdapter.CaseViewHolder> {

    private List<DocumentSnapshot> mCaseSnapshots = new ArrayList<>();

    private String mDocId;
    private String mCallsign;
    private Boolean mEditMode;

    public CaseAdapter(String docId, String callsign, Boolean editMode){
        mDocId = docId;
        mCallsign = callsign;
        mEditMode = editMode;

        CollectionReference casesCollectionRef = FirebaseFirestore.getInstance().
                collection(Constants.COLLECTION_ROOT).document(docId).collection(Constants.COLLECTION_CASE);
        casesCollectionRef
                .orderBy(Constants.KEY_TIME_STARTED, Query.Direction.DESCENDING)
                .addSnapshotListener(new EventListener<QuerySnapshot>() {
            @Override
            public void onEvent(QuerySnapshot documentSnapshots, FirebaseFirestoreException e) {
                if (e != null) {
                    Log.w(Constants.TAG, "Listening failed");
                    return;
                }
                mCaseSnapshots = documentSnapshots.getDocuments();
                notifyDataSetChanged();
            }
        });


    }
    @NonNull
    @Override
    public CaseViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int i) {
        View itemView = LayoutInflater.from(parent.getContext()).inflate(R.layout.cases_itemview, parent, false);
        return new CaseViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(@NonNull CaseViewHolder caseViewHolder, int i) {
        SimpleDateFormat sdf = new SimpleDateFormat("HH:mm");
        DocumentSnapshot ds = mCaseSnapshots.get(i);
        String treatingStation = (String)ds.get(Constants.KEY_TREATING_STATION);
        String complaint = (String)ds.get(Constants.KEY_COMPLAINT);
        String timeStarted = sdf.format((Date)ds.get(Constants.KEY_TIME_STARTED));
        caseViewHolder.mTreatingStationTextView.setText(treatingStation);
        caseViewHolder.mComplaintTextView.setText(complaint);
        caseViewHolder.mTimeTextView.setText(timeStarted);

    }

    @Override
    public int getItemCount() {
        return mCaseSnapshots.size();
    }

    class CaseViewHolder extends RecyclerView.ViewHolder{

        private TextView mTreatingStationTextView;
        private TextView mComplaintTextView;
        private TextView mTimeTextView;

        public CaseViewHolder(@NonNull View itemView) {
            super(itemView);
            mTreatingStationTextView = itemView.findViewById(R.id.itemview_treatingstation);
            mComplaintTextView = itemView.findViewById(R.id.itemview_complaint);
            mTimeTextView = itemView.findViewById(R.id.itemview_time);
            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    DocumentSnapshot ds = mCaseSnapshots.get(getAdapterPosition());
                    Context c = view.getContext();
                    Intent intent = new Intent(c, CaseViewActivity.class);
                    Bundle extras = new Bundle();
                    extras.putString(Constants.EXTRA_DOC_ID, mDocId);
                    extras.putString(Constants.EXTRA_CASE_DOC_ID, ds.getId());
                    extras.putString(Constants.EXTRA_COMPLAINT, (String)mComplaintTextView.getText());
                    extras.putString(Constants.EXTRA_CALLSIGN, mCallsign);
                    extras.putBoolean(Constants.EXTRA_EDITMODE, mEditMode);
                    intent.putExtras(extras);
                    c.startActivity(intent);
                }
            });
        }
    }
}
