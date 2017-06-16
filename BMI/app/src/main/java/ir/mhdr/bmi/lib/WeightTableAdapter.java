package ir.mhdr.bmi.lib;

import android.content.Context;
import android.graphics.drawable.Drawable;
import android.support.v4.app.FragmentManager;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.AppCompatButton;
import android.support.v7.widget.AppCompatTextView;
import android.support.v7.widget.ListPopupWindow;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import org.joda.time.DateTime;

import java.util.List;
import java.util.Locale;

import ir.mhdr.bmi.R;
import ir.mhdr.bmi.WeightFragment;
import ir.mhdr.bmi.bl.HistoryBL;
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

        String datetimeStr = String.format(Locale.US,"%d/%d/%d %d:%d", history.getDatetime3().getYear(),
                history.getDatetime3().getMonth().getValue(), history.getDatetime3().getDayOfMonth(),
                history.getDatetime2().getHourOfDay(), history.getDatetime2().getMinuteOfHour());

        holder.viewBMIColorForTable.setBackground(drawable);
        holder.textViewWeightForTable.setText(history.getValue());
        holder.textViewDateForTable.setText(datetimeStr);

        final AppCompatButton button = holder.buttonOptions;

        holder.buttonOptions.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String[] menuItems = context.getResources().getStringArray(R.array.table_menu);
                ArrayAdapter<String> adapter = new ArrayAdapter<String>(context, R.layout.weight_table_menu,
                        R.id.textViewTableMenuItem, menuItems);

                final ListPopupWindow listPopupWindow = new ListPopupWindow(context);
                listPopupWindow.setAdapter(adapter);
                listPopupWindow.setAnchorView(button);
                listPopupWindow.setWidth(420);
                listPopupWindow.setHeight(ListPopupWindow.WRAP_CONTENT);
                listPopupWindow.setHorizontalOffset(-380);
                listPopupWindow.setVerticalOffset(-50);

                listPopupWindow.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                    @Override
                    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {

                        if (position == 0) {
                            // edit


                            final int itemPosition = viewHolder.getAdapterPosition();
                            final History currentHistory = historyList.get(itemPosition);

                            final UserBL userBL = new UserBL(context.getApplicationContext());
                            final HistoryBL historyBL = new HistoryBL(context);

                            final String valueStr = currentHistory.getValue();

                            WeightFragment weightFragment = new WeightFragment();

                            if (valueStr.length() > 0) {
                                weightFragment.setWeightValue(Double.parseDouble(valueStr));
                            }

                            weightFragment.setOnSaveListener(new WeightFragment.OnSaveListener() {
                                @Override
                                public void onSave(double value) {
                                    currentHistory.setValue(String.valueOf(value));
                                    int rows_affected = historyBL.update(currentHistory);

                                    if (itemPosition == 0) {
                                        user.setLatestWeight(String.valueOf(value));
                                        userBL.update(user);
                                    }

                                    notifyItemChanged(itemPosition,currentHistory);
                                    Toast.makeText(context, R.string.weight_edited,Toast.LENGTH_LONG).show();
                                }
                            });
                            FragmentManager manager = ((AppCompatActivity)context).getSupportFragmentManager();
                            weightFragment.show(manager, "weight");


                        } else if (position == 1) {
                            // delete

                            if (historyList.size()==1)
                            {
                                Toast.makeText(context, context.getResources().getString(R.string.atleast_one_weight_is_needed), Toast.LENGTH_LONG).show();
                                return;
                            }

                            int itemPosition = viewHolder.getAdapterPosition();

                            UserBL userBL = new UserBL(context.getApplicationContext());
                            HistoryBL historyBL = new HistoryBL(context.getApplicationContext());

                            History historyToDelete = historyList.get(itemPosition);
                            historyBL.delete(historyToDelete);

                            if (itemPosition == 0) {
                                History currentHistory = historyBL.getLastHistory(user);
                                user.setLatestWeight(currentHistory.getValue());
                                userBL.update(user);
                            }

                            historyList.remove(itemPosition);
                            notifyItemRemoved(itemPosition);

                            Toast.makeText(context, R.string.weight_deleted,Toast.LENGTH_LONG).show();
                        }

                        listPopupWindow.dismiss();
                    }
                });

                listPopupWindow.show();
            }
        });
    }

    @Override
    public int getItemCount() {
        return historyList.size();
    }

    public class WeightTableViewHolder extends RecyclerView.ViewHolder {

        public AppCompatButton buttonOptions;
        public View viewBMIColorForTable;
        public AppCompatTextView textViewWeightForTable;
        public AppCompatTextView textViewDateForTable;

        public WeightTableViewHolder(View itemView) {
            super(itemView);


            buttonOptions = (AppCompatButton) itemView.findViewById(R.id.buttonOptions);
            viewBMIColorForTable = itemView.findViewById(R.id.viewBMIColorForTable);
            textViewWeightForTable = (AppCompatTextView) itemView.findViewById(R.id.textViewWeightForTable);
            textViewDateForTable = (AppCompatTextView) itemView.findViewById(R.id.textViewDateForTable);
        }
    }

}
