package ir.mhdr.bmi;

import android.content.Context;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.DividerItemDecoration;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import ir.mhdr.bmi.bl.HistoryBL;
import ir.mhdr.bmi.bl.UserBL;
import ir.mhdr.bmi.lib.ProfileChangedListener;
import ir.mhdr.bmi.lib.WeightTableAdapter;
import ir.mhdr.bmi.model.History;


public class TableFragment extends Fragment implements ProfileChangedListener{

    private RecyclerView recyclerViewTable;
    private WeightTableAdapter adapter;
    private RecyclerView.LayoutManager layoutManager;

    HistoryBL historyBL;
    UserBL userBL;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.fragment_table, container, false);

        userBL = new UserBL(view.getContext());
        historyBL = new HistoryBL(view.getContext());

        recyclerViewTable = (RecyclerView) view.findViewById(R.id.recyclerViewTable);
        layoutManager = new LinearLayoutManager(view.getContext());
        recyclerViewTable.setLayoutManager(layoutManager);
        recyclerViewTable.addItemDecoration(new DividerItemDecoration(view.getContext(), DividerItemDecoration.VERTICAL));
        adapter = new WeightTableAdapter(new ArrayList<History>());
        recyclerViewTable.setAdapter(adapter);

        return view;
    }

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
        if (!getUserVisibleHint())
        {
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
