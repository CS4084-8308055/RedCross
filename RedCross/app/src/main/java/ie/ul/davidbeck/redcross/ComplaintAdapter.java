package ie.ul.davidbeck.redcross;

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
import java.util.List;

public class ComplaintAdapter extends RecyclerView.Adapter<ComplaintAdapter.ComplaintViewHolder>{

    private List<DocumentSnapshot> mComplaintSnapshots = new ArrayList<>();

    public ComplaintAdapter(){
        CollectionReference complaintsCollectionRef = FirebaseFirestore.getInstance().collection(Constants.COLLECTION_COMPLAINT);

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
        View itemView = LayoutInflater.from(parent.getContext()).inflate(R.layout.complaints_itemview, parent);
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
        }
    }
}
