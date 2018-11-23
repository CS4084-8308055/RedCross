package ie.ul.davidbeck.redcross;

import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

public class DutyAdapter extends RecyclerView.Adapter<DutyAdapter.DutyViewHolder> {
    @NonNull
    @Override
    public DutyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int i) {
        View itemView = LayoutInflater.from(parent.getContext()).inflate(R.layout.duties_itemview, parent, false);
        return new DutyViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(@NonNull DutyViewHolder dutyViewHolder, int i) {

    }

    @Override
    public int getItemCount() {
        return 0;
    }

    class DutyViewHolder extends RecyclerView.ViewHolder {
        private TextView mLocationTextView;
        private TextView mDutyDateTextView;

        public DutyViewHolder(@NonNull View itemView) {
            super(itemView);
            mLocationTextView = itemView.findViewById(R.id.itemview_location);
            mDutyDateTextView = itemView.findViewById(R.id.itemview_dutydate);
        }
    }
}
