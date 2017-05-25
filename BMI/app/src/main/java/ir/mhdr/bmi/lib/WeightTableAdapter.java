package ir.mhdr.bmi.lib;

import android.content.Context;
import android.graphics.drawable.Drawable;
import android.support.v7.widget.PopupMenu;
import android.support.v7.widget.RecyclerView;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import java.sql.Array;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import ir.mhdr.bmi.R;
import ir.mhdr.bmi.bl.UserBL;
import ir.mhdr.bmi.model.History;
import ir.mhdr.bmi.model.User;

public class WeightTableAdapter extends RecyclerView.Adapter<WeightTableAdapter.WeightTableViewHolder> {

    public List<History> getHistoryList() {
        return historyList;
    }

    private List<History> historyList;
    private Context context;
    private User user;

    public void setHistoryList(List<History> historyList) {
        this.historyList = historyList;
        this.notifyDataSetChanged();
    }

    public WeightTableAdapter(List<History> historyList) {
        this.historyList = historyList;
    }

    @Override
    public WeightTableViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        context = parent.getContext();
        LayoutInflater inflater = LayoutInflater.from(context);

        View view = inflater.inflate(R.layout.weight_table_row, parent, false);

        UserBL userBL = new UserBL(context);
        this.user = userBL.getActiveUser();

        WeightTableViewHolder viewHolder = new WeightTableViewHolder(view);

        return viewHolder;
    }

    @Override
    public void onBindViewHolder(WeightTableViewHolder holder, int position) {
        final History history = this.historyList.get(position);
        final WeightTableViewHolder viewHolder = holder;

        Drawable drawable = context.getResources().getDrawable(R.drawable.color_bmi_range_1);

        BMI bmi = new BMI(user.getLatestHeight(), history.getValue());

        switch (bmi.getRange()) {
            case 1:
                drawable = context.getResources().getDrawable(R.drawable.color_bmi_range_1);
                break;
            case 2:
                drawable = context.getResources().getDrawable(R.drawable.color_bmi_range_2);
                break;
            case 3:
                drawable = context.getResources().getDrawable(R.drawable.color_bmi_range_3);
                break;
            case 4:
                drawable = context.getResources().getDrawable(R.drawable.color_bmi_range_4);
                break;
            case 5:
                drawable = context.getResources().getDrawable(R.drawable.color_bmi_range_5);
                break;
            case 6:
                drawable = context.getResources().getDrawable(R.drawable.color_bmi_range_6);
                break;
            case 7:
                drawable = context.getResources().getDrawable(R.drawable.color_bmi_range_7);
                break;
            case 8:
                drawable = context.getResources().getDrawable(R.drawable.color_bmi_range_8);
                break;
        }

        String datetimeStr=String.format("%d/%d/%d %d:%d",history.getDatetime3().getYear(),
                history.getDatetime3().getMonth().getValue(),history.getDatetime3().getDayOfMonth(),
                history.getDatetime2().getHourOfDay(),history.getDatetime2().getMinuteOfHour());

        holder.viewBMIColorForTable.setBackground(drawable);
        holder.textViewWeightForTable.setText(history.getValue());
        holder.textViewDateForTable.setText(datetimeStr);

        final Button button = holder.buttonOptions;

        holder.buttonOptions.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                PopupMenu popup = new PopupMenu(context, button);
                popup.inflate(R.menu.weight_table_menu);
                //popup.setGravity(Gravity.END);

                popup.setOnMenuItemClickListener(new PopupMenu.OnMenuItemClickListener() {
                    @Override
                    public boolean onMenuItemClick(MenuItem item) {
                        switch (item.getItemId()) {
                            case R.id.menuItemDelete:

                                //

                                historyList.remove(viewHolder.getAdapterPosition());
                                notifyItemRemoved(viewHolder.getAdapterPosition());
                                return true;

                            case R.id.menuItemEdit:

                                //

                                return true;
                        }
                        return false;
                    }
                });

                popup.show();
            }
        });
    }

    @Override
    public int getItemCount() {
        return historyList.size();
    }

    public class WeightTableViewHolder extends RecyclerView.ViewHolder {

        public Button buttonOptions;
        public View viewBMIColorForTable;
        public TextView textViewWeightForTable;
        public TextView textViewDateForTable;

        public WeightTableViewHolder(View itemView) {
            super(itemView);


            buttonOptions = (Button) itemView.findViewById(R.id.buttonOptions);
            viewBMIColorForTable = itemView.findViewById(R.id.viewBMIColorForTable);
            textViewWeightForTable = (TextView) itemView.findViewById(R.id.textViewWeightForTable);
            textViewDateForTable = (TextView) itemView.findViewById(R.id.textViewDateForTable);
        }
    }
}
