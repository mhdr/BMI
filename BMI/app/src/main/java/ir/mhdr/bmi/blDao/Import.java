package ir.mhdr.bmi.blDao;


import android.content.Context;

import java.util.ArrayList;
import java.util.List;

import ir.mhdr.bmi.bl.*;
import ir.mhdr.bmi.bl.HistoryBL;
import ir.mhdr.bmi.bl.UserBL;
import ir.mhdr.bmi.model.History;
import ir.mhdr.bmi.model.User;

public class Import {

    private Context context;

    public Import(Context context) {
        this.context = context;
    }

    public void fromV1ToV2() {
        PrivateSettingBL privateSettingBL = new PrivateSettingBL();

        if (privateSettingBL.getImportDbV1() == 1) {
            return;
        }

        UserBL userBLOld = new UserBL(context);
        HistoryBL historyBLOld = new HistoryBL(context);

        ir.mhdr.bmi.blDao.UserBL userBL = new ir.mhdr.bmi.blDao.UserBL();
        ir.mhdr.bmi.blDao.HistoryBL historyBL = new ir.mhdr.bmi.blDao.HistoryBL();

        List<User> usersOld = userBLOld.getUsers();
        ir.mhdr.bmi.dao.User activeUser = null;


        // if there is no previouse version of app
        if (usersOld.size() == 0) {
            privateSettingBL.setImportDbV1(1);
            return;
        }

        for (User user : usersOld) {
            ir.mhdr.bmi.dao.User newUser = new ir.mhdr.bmi.dao.User();
            newUser.setName(user.getName());
            newUser.setBirthdate(user.getBirthdate());
            newUser.setGender(user.getGender());
            newUser.setLatestWeight(user.getLatestWeight());
            newUser.setLatestHeight(user.getLatestHeight());


            userBL.insert(newUser);
            String userUuid = newUser.getUuid();

            //set first user as active user
            if (activeUser == null) {
                activeUser = newUser;
            }

            List<History> historyListOld = historyBLOld.getHistory(user);
            List<ir.mhdr.bmi.dao.History> historyList = new ArrayList<>();

            for (History history : historyListOld) {
                ir.mhdr.bmi.dao.History newHistory = new ir.mhdr.bmi.dao.History();
                newHistory.setUserUuid(userUuid);
                newHistory.setValue(history.getValue());
                newHistory.setDatetime(history.getDatetime());

                historyList.add(newHistory);
            }

            historyBL.insertRange(historyList);
        }


        privateSettingBL.setActiveUser(activeUser);
        privateSettingBL.setImportDbV1(1);
    }
}
