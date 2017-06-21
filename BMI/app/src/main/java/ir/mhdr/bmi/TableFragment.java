package ir.mhdr.bmi;

import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.Fragment;
import android.support.v7.widget.DividerItemDecoration;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.google.firebase.analytics.FirebaseAnalytics;

import org.joda.time.DateTime;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import ir.mhdr.bmi.bl.HistoryBL;
import ir.mhdr.bmi.bl.UserBL;
import ir.mhdr.bmi.lib.FirebaseUtils;
import ir.mhdr.bmi.lib.ProfileChangedListener;
import ir.mhdr.bmi.lib.Statics;
import ir.mhdr.bmi.lib.WeightTableAdapter;
import ir.mhdr.bmi.model.History;
import ir.mhdr.bmi.model.User;


public class TableFragment extends Fragment implements ProfileChangedListener {

    private RecyclerView recyclerViewTable;
    private WeightTableAdapter adapter;
    private RecyclerView.LayoutManager layoutManager;
    FloatingActionButton floatingActionButtonNewWeight;

    private FirebaseAnalytics mFirebaseAnalytics;

    HistoryBL historyBL;
    UserBL userBL;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.fragment_table, container, false);

        if (FirebaseUtils.checkPlayServices(getContext())) {

            // Obtain the FirebaseAnalytics instance.
            mFirebaseAnalytics = FirebaseAnalytics.getInstance(getContext());
            mFirebaseAnalytics.setCurrentScreen(this.getActivity(),"TableFragment",this.getClass().getSimpleName());
            mFirebaseAnalytics.setUserProperty("InstallSource", Statics.InstallSource);
        }

        userBL = new UserBL(view.getContext());
        historyBL = new HistoryBL(view.getContext());

        recyclerViewTable = (RecyclerView) view.findViewById(R.id.recyclerViewTable);
        floatingActionButtonNewWeight = (FloatingActionButton) view.findViewById(R.id.floatingActionButtonNewWeight);

        layoutManager = new LinearLayoutManager(view.getContext());
        recyclerViewTable.setLayoutManager(layoutManager);
        recyclerViewTable.addItemDecoration(new DividerItemDecoration(view.getContext(), DividerItemDecoration.VERTICAL));
        adapter = new WeightTableAdapter(new ArrayList<History>());
        recyclerViewTable.setAdapter(adapter);

        recyclerViewTable.addOnScrollListener(recyclerViewTable_OnScrollListener);

        floatingActionButtonNewWeight.setOnClickListener(floatingActionButtonNewWeight_OnClickListener);

        return view;
    }

    View.OnClickListener floatingActionButtonNewWeight_OnClickListener=new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            openWeightDialog();
        }
    };

    private void openWeightDialog() {

        final UserBL userBL = new UserBL(getContext());
        final HistoryBL historyBL = new HistoryBL(getContext());
        final User user = userBL.getActiveUser();

        String valueStr = user.getLatestWeight();

        WeightFragment weightFragment = new WeightFragment();

        if (valueStr.length() > 0) {
            weightFragment.setWeightValue(Double.parseDouble(valueStr));
        }

        weightFragment.setOnSaveListener(new WeightFragment.OnSaveListener() {
            @Override
            public void onSave(double value) {
                user.setLatestWeight(String.valueOf(value));
                int rows_affected = userBL.update(user);

                DateTime current = new DateTime();

                History history = new History();
                history.setUserId(user.getId());
                history.setValue(String.valueOf(value));
                history.setDatetime(current.toString());

                long historyId = historyBL.insert(history);

                if (historyId > 0) {
                    Toast.makeText(getContext(), R.string.new_weight_saved, Toast.LENGTH_LONG).show();
                }

                onResume();
            }
        });

        weightFragment.show(getFragmentManager(), "weight");
    }

    RecyclerView.OnScrollListener recyclerViewTable_OnScrollListener = new RecyclerView.OnScrollListener() {
        @Override
        public void onScrollStateChanged(RecyclerView recyclerView, int newState) {

            if (newState == RecyclerView.SCROLL_STATE_IDLE) {
                floatingActionButtonNewWeight.show();
            }

            super.onScrollStateChanged(recyclerView, newState);
        }

        @Override
        public void onScrolled(RecyclerView recyclerView, int dx, int dy) {

            if (dy > 0 || dy < 0 && floatingActionButtonNewWeight.isShown()) {
                floatingActionButtonNewWeight.hide();
            }

            super.onScrolled(recyclerView, dx, dy);
        }
    };

    @Override
    public void setUserVisibleHint(boolean isVisibleToUser) {
        super.setUserVisibleHint(isVisibleToUser);

        if (isVisibleToUser && isResumed()) {
            onResume();
        }
    }

    @Override
    public void onResume() {
        super.onResume();
        if (!getUserVisibleHint()) {
            return;
        }

        List<History> historyList = historyBL.getHistory(userBL.getActiveUser());
        Collections.reverse(historyList);
        adapter.setHistoryList(historyList);
    }

    @Override
    public void onProfileChanged() {
        onResume();
    }
}
