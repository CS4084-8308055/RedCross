package ie.ul.davidbeck.redcross;

import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

public class CaseAdapter extends RecyclerView.Adapter<CaseAdapter.CaseViewHolder> {
    @NonNull
    @Override
    public CaseViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int i) {
        View itemView = LayoutInflater.from(parent.getContext()).inflate(R.layout.cases_itemview, parent, false);
        return new CaseViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(@NonNull CaseViewHolder caseViewHolder, int i) {

    }

    @Override
    public int getItemCount() {
        return 0;
    }

    class CaseViewHolder extends RecyclerView.ViewHolder{

        private TextView mTreatingStationTextView;
        private TextView mComplaintTextView;

        public CaseViewHolder(@NonNull View itemView) {
            super(itemView);
            mTreatingStationTextView = itemView.findViewById(R.id.itemview_treatingstation);
            mComplaintTextView = itemView.findViewById(R.id.itemview_complaint);
        }
    }
}
