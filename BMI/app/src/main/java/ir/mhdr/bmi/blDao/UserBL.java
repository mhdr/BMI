package ir.mhdr.bmi.blDao;


import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

import ir.mhdr.bmi.dao.PrivateSetting;
import ir.mhdr.bmi.dao.User;
import ir.mhdr.bmi.dao.UserDao;
import ir.mhdr.bmi.lib.Statics;

public class UserBL {

    public long insert(User user) {
        UserDao userDao = Statics.daoSession.getUserDao();

        user.setUuid(UUID.randomUUID().toString());
        user.setTimestamp(UUID.randomUUID().toString());
        user.setIsRemoved(false);

        long id = userDao.insert(user);

        return id;
    }

    public void insertRange(List<User> userList)
    {
        UserDao userDao = Statics.daoSession.getUserDao();

        for (User user:userList)
        {
            user.setUuid(UUID.randomUUID().toString());
            user.setTimestamp(UUID.randomUUID().toString());
            user.setIsRemoved(false);
        }

        userDao.insertInTx(userList);
    }

    public User getUser(long id) {

        User user = null;

        UserDao userDao = Statics.daoSession.getUserDao();

        user = userDao.queryBuilder().
                where(UserDao.Properties.Id.eq(id)).
                where(UserDao.Properties.IsRemoved.eq(false)).
                unique();

        return user;
    }

    public User getUserByUuid(String uuid) {

        User user = null;

        UserDao userDao = Statics.daoSession.getUserDao();

        user = userDao.queryBuilder().
                where(UserDao.Properties.Uuid.eq(uuid)).
                where(UserDao.Properties.IsRemoved.eq(false)).
                unique();

        return user;
    }

    public User getActiveUser() {
        User user = null;

        PrivateSettingBL privateSettingBL = new PrivateSettingBL();
        user = privateSettingBL.getActiveUser();

        return user;
    }

    public List<User> getUsers() {
        List<User> userList = new ArrayList<>();

        UserDao userDao = Statics.daoSession.getUserDao();

        userList = userDao.queryBuilder().
                where(UserDao.Properties.IsRemoved.eq(false)).
                list();

        return userList;
    }

    public int countUsers() {
        UserDao userDao = Statics.daoSession.getUserDao();

        int count = (int) userDao.count();

        return count;
    }

    public void update(User user) {
        UserDao userDao = Statics.daoSession.getUserDao();

        user.setTimestamp(UUID.randomUUID().toString());

        userDao.update(user);
    }

    public void delete(User user) {
        UserDao userDao = Statics.daoSession.getUserDao();
        user.setTimestamp(UUID.randomUUID().toString());
        user.setIsRemoved(true);

        userDao.update(user);
    }

    public void SwitchActiveUser(User user) {
        PrivateSettingBL privateSettingBL = new PrivateSettingBL();
        privateSettingBL.setActiveUser(user);
    }
}
