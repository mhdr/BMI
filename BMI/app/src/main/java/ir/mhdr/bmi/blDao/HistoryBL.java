package ir.mhdr.bmi.blDao;


import org.joda.time.DateTime;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

import ir.mhdr.bmi.dao.History;
import ir.mhdr.bmi.dao.HistoryDao;
import ir.mhdr.bmi.dao.User;
import ir.mhdr.bmi.dao.UserDao;
import ir.mhdr.bmi.lib.Statics;

public class HistoryBL {

    public long insert(History history) {
        HistoryDao historyDao = Statics.daoSession.getHistoryDao();

        history.setUuid(UUID.randomUUID().toString());
        history.setIsRemoved(false);

        // for sync
        history.setSyncState(false);
        history.setTimestamp(UUID.randomUUID().toString());
        history.setDatetimeModified(new DateTime().toString());
        //

        long id = historyDao.insert(history);

        return id;
    }

    public void insertRange(List<History> historyList) {
        HistoryDao historyDao = Statics.daoSession.getHistoryDao();

        for (History history : historyList) {
            history.setUuid(UUID.randomUUID().toString());
            history.setIsRemoved(false);

            // for sync
            history.setSyncState(false);
            history.setTimestamp(UUID.randomUUID().toString());
            history.setDatetimeModified(new DateTime().toString());
            //
        }

        historyDao.insertInTx(historyList);
    }

    public List<History> getHistory(User user) {
        List<History> historyList = new ArrayList<>();

        HistoryDao historyDao = Statics.daoSession.getHistoryDao();

        historyList = historyDao.queryBuilder()
                .where(HistoryDao.Properties.IsRemoved.eq(false))
                .where(HistoryDao.Properties.UserUuid.eq(user.getUuid()))
                .list();

        return historyList;
    }

    public void delete(History history) {
        HistoryDao historyDao = Statics.daoSession.getHistoryDao();

        history.setIsRemoved(true);

        // for sync
        history.setSyncState(false);
        history.setTimestamp(UUID.randomUUID().toString());
        history.setDatetimeModified(new DateTime().toString());
        //

        historyDao.update(history);
    }

    public void deleteAllByUser(User user) {
        HistoryDao historyDao = Statics.daoSession.getHistoryDao();

        List<History> historyList = new ArrayList<>();

        historyList = historyDao.queryBuilder()
                .where(HistoryDao.Properties.UserUuid.eq(user.getUuid()))
                .where(HistoryDao.Properties.IsRemoved.eq(false))
                .list();

        for (History history : historyList) {
            history.setIsRemoved(false);
            history.setSyncState(false);
        }

        historyDao.updateInTx(historyList);
    }

    public void update(History history) {
        HistoryDao historyDao = Statics.daoSession.getHistoryDao();

        // for sync
        history.setSyncState(false);
        history.setTimestamp(UUID.randomUUID().toString());
        history.setDatetimeModified(new DateTime().toString());
        //

        historyDao.update(history);
    }

    public History getLastHistory(User user) {
        HistoryDao historyDao = Statics.daoSession.getHistoryDao();
        History history = null;

        history = historyDao.queryBuilder()
                .orderAsc(HistoryDao.Properties.Datetime)
                .limit(1)
                .unique();

        return history;
    }
}
