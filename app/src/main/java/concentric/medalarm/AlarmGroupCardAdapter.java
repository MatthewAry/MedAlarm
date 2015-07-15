package concentric.medalarm;

import android.content.DialogInterface;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TableRow;
import android.widget.TextView;

import java.util.List;

import concentric.medalarm.models.AlarmGroup;
import concentric.medalarm.models.DBHelper;

/**
 * Created by MatthewAry on 7/9/2015.
 */
public class AlarmGroupCardAdapter extends RecyclerView.Adapter<AlarmGroupCardAdapter.ViewHolder> {
    private List<AlarmGroup> alarmGroups;

    // Provide a suitable constructor (depends on the kind of dataset)
    public AlarmGroupCardAdapter(List<AlarmGroup> dataSet) {
        alarmGroups = dataSet;
    }

    // Return the size of your dataset (invoked by the layout manager)
    @Override
    public int getItemCount() {
        return alarmGroups.size();
    }

    // Replace the contents of a view (invoked by the layout manager)
    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        AlarmGroup item = alarmGroups.get(position);
        // - get element from your dataset at this position
        // - replace the contents of the view with that element
        ViewHolder.groupName.setText(item.getGroupName());

        String alarmType[] = DBHelper.getContext().getResources().getStringArray(R.array
                .alarm_types);

        ViewHolder.groupType.setText(alarmType[item.getAlarmType()]);

    }

    // Create new views (invoked by the layout manager)
    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        // create a new view
        View v = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.alarm_group_card, parent, false);
        // Optionally set the view's size, margins, paddings and layout parameters below

        AlarmGroupCardAdapter.ViewHolder vh = new ViewHolder(v, new AlarmGroupCardAdapter
                .ViewHolder.alarmGroupViewHolderClicks() {
            public
        });
        return vh;
    }

    // Provide a reference to the views for each data item
    // Complex data items may need more than one view per item, and
    // you provide access to all the views for a data item in a view holder

    public static class ViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
        protected static TextView groupName;
        protected static TextView groupType;
        protected static TableRow itemDetails;
        protected alarmGroupViewHolderClicks mListener;

        public ViewHolder(View v) {
            super(v);
            groupName = (TextView) v.findViewById(R.id.groupName);
            groupType = (TextView) v.findViewById(R.id.groupType);

        }

        @Override
        public void onClick(View v) {

        }
    }
}
