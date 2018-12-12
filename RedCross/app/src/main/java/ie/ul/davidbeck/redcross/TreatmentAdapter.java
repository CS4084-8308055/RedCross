package ie.ul.davidbeck.redcross;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v7.app.AlertDialog;
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

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class TreatmentAdapter extends RecyclerView.Adapter<TreatmentAdapter.TreatmentViewHolder> {

    private String mDutyDocId;
    private String mCaseDocId;
    private List<DocumentSnapshot> mTreatmentSnapshots = new ArrayList<>();
    private Context mContext;

    public TreatmentAdapter(String dutyDocId, String caseDocId, Context context){
        mDutyDocId = dutyDocId;
        mCaseDocId = caseDocId;
        mContext = context;

        CollectionReference treatmentsCollectionRef = FirebaseFirestore.getInstance()
                .collection(Constants.COLLECTION_ROOT).document(dutyDocId)
                .collection(Constants.COLLECTION_CASE).document(caseDocId)
                .collection(Constants.COLLECTION_TREATMENT);
        treatmentsCollectionRef.orderBy(Constants.KEY_TIME_STARTED, Query.Direction.ASCENDING);
        treatmentsCollectionRef.addSnapshotListener(new EventListener<QuerySnapshot>() {
            @Override
            public void onEvent(QuerySnapshot documentSnapshots, FirebaseFirestoreException e) {
                if (e != null) {
                    Log.w(Constants.TAG, "Listening failed");
                    return;
                }
                mTreatmentSnapshots = documentSnapshots.getDocuments();
                notifyDataSetChanged();
            }
        });


    }

    @NonNull
    @Override
    public TreatmentAdapter.TreatmentViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int i) {
        View itemView = LayoutInflater.from(parent.getContext()).inflate(R.layout.treatment_itemview, parent, false);
        return new TreatmentViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(@NonNull TreatmentAdapter.TreatmentViewHolder treatmentViewHolder, int i) {
        SimpleDateFormat sdf = new SimpleDateFormat("HH:mm:ss");
        DocumentSnapshot ds = mTreatmentSnapshots.get(i);
        String treatment = (String)ds.get(Constants.KEY_TREATMENT);
        String treatmentTime = sdf.format((Date)ds.get(Constants.KEY_TIME_STARTED));
        treatmentViewHolder.mTreatmentTimeTextView.setText(treatmentTime);
        treatmentViewHolder.mTreatmentTextView.setText(treatment);

    }

    @Override
    public int getItemCount() {
        return mTreatmentSnapshots.size();
    }
    class TreatmentViewHolder extends RecyclerView.ViewHolder {
        private TextView mTreatmentTextView;
        private TextView mTreatmentTimeTextView;

        public TreatmentViewHolder(@NonNull View itemView) {
            super(itemView);
            mTreatmentTextView = itemView.findViewById(R.id.itemview_treatment);
            mTreatmentTimeTextView = itemView.findViewById(R.id.itemview_treatmenttime);
            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    DocumentSnapshot ds = mTreatmentSnapshots.get(getAdapterPosition());
                    showDetailDialog(ds);
                    //Context c = view.getContext();
                    //Intent intent = new Intent(c, TreatmentDetailsActivity.class);
                    //Bundle extras = new Bundle();
                    //extras.putString(Constants.EXTRA_DOC_ID, mDutyDocId);
                    //extras.putString(Constants.EXTRA_CASE_DOC_ID, mCaseDocId);
                    //extras.putString(Constants.EXTRA_TREATMENT_DOC_ID, ds.getId());
                    //intent.putExtras(extras);
                    //c.startActivity(intent);
                }
            });
        }
        private void showDetailDialog(DocumentSnapshot ds){
            SimpleDateFormat sdf = new SimpleDateFormat("HH:mm:ss");

            AlertDialog.Builder builder = new AlertDialog.Builder(mContext);
            View view = LayoutInflater.from(mContext).inflate(R.layout.treatmentdetaildialog, null, false);
            builder.setView(view);
            builder.setTitle("Treatment Details");
            final TextView treatingStationTextView = view.findViewById(R.id.treatment_detail_treating_station);
            final TextView complaintTextView = view.findViewById(R.id.treatment_detail_complaint);
            final TextView timeTextView = view.findViewById(R.id.treatment_detail_time);
            final TextView treatmentTextView = view.findViewById(R.id.treatment_detail_treatment);
            final TextView commentTextView = view.findViewById(R.id.treatment_detail_comment);
            final TextView questionTextView = view.findViewById(R.id.treatment_detail_question);

            treatingStationTextView.setText((String)ds.get(Constants.KEY_TREATING_STATION));
            complaintTextView.setText((String)ds.get(Constants.KEY_COMPLAINT));
            timeTextView.setText(sdf.format((Date)ds.get(Constants.KEY_TIME_STARTED)));
            treatmentTextView.setText((String)ds.get(Constants.KEY_TREATMENT));
            commentTextView.setText((String)ds.get(Constants.KEY_COMMENT));
            questionTextView.setText(((String)ds.get(Constants.KEY_QUESTION )) + " " + ((String)ds.get(Constants.KEY_ANSWER)));


            builder.setPositiveButton(android.R.string.ok, null);

            builder.create().show();

        }
    }
}
