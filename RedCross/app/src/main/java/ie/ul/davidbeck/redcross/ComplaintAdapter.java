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

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class ComplaintAdapter extends RecyclerView.Adapter<ComplaintAdapter.ComplaintViewHolder>{

    private List<DocumentSnapshot> mComplaintSnapshots = new ArrayList<>();
    private String mCallsign;
    private String mDutyDocId;
    private String mCaseDocId;

    public ComplaintAdapter(String dutyDocId, String caseDocId, String callsign){
        CollectionReference complaintsCollectionRef = FirebaseFirestore.getInstance().collection(Constants.COLLECTION_COMPLAINT);

        mCallsign = callsign;
        mDutyDocId = dutyDocId;
        mCaseDocId = caseDocId;

        complaintsCollectionRef.addSnapshotListener(new EventListener<QuerySnapshot>() {
            @Override
            public void onEvent(QuerySnapshot documentSnapshots, FirebaseFirestoreException e) {
                if (e != null) {
                    Log.w(Constants.TAG, "Listening failed");
                    return;
                }
                mComplaintSnapshots = documentSnapshots.getDocuments();
                notifyDataSetChanged();
            }
        });
    }

    @NonNull
    @Override
    public ComplaintViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int i) {
        View itemView = LayoutInflater.from(parent.getContext()).inflate(R.layout.complaints_itemview, parent, false);
        return new ComplaintViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(@NonNull ComplaintViewHolder complaintViewHolder, int i) {
        DocumentSnapshot ds = mComplaintSnapshots.get(i);
        String complaint = (String)ds.get(Constants.KEY_COMPLAINT);
        complaintViewHolder.mComplaint.setText(complaint);
    }

    @Override
    public int getItemCount() {
        return mComplaintSnapshots.size();
    }

    class ComplaintViewHolder extends RecyclerView.ViewHolder {

        private TextView mComplaint;

        public ComplaintViewHolder(@NonNull View itemView) {
            super(itemView);
            mComplaint = itemView.findViewById(R.id.itemview_complaint);
            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    DocumentSnapshot ds = mComplaintSnapshots.get(getAdapterPosition());
                    Map<String, Object> complaint = new HashMap<>();
                    complaint.put(Constants.KEY_COMPLAINT, (String)ds.get(Constants.KEY_COMPLAINT));
                    FirebaseFirestore db = FirebaseFirestore.getInstance();
                    db.collection(Constants.COLLECTION_ROOT).document(mDutyDocId)
                            .collection(Constants.COLLECTION_CASE).document(mCaseDocId)
                            .update(complaint);
                    Context c = view.getContext();
                    Intent intent = new Intent(c, TreatmentActivity.class);
                    Bundle extras = new Bundle();
                    extras.putString(Constants.EXTRA_DOC_ID, mDutyDocId);
                    extras.putString(Constants.EXTRA_CASE_DOC_ID, mCaseDocId);
                    extras.putString(Constants.EXTRA_COMPLAINT_DOC_ID, ds.getId());
                    extras.putString(Constants.EXTRA_CALLSIGN, mCallsign);
                    intent.putExtras(extras);
                    c.startActivity(intent);

                }
            });
        }
    }
}
