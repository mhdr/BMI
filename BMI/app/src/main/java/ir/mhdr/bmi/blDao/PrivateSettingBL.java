package ir.mhdr.bmi.blDao;


import java.util.UUID;

import ir.mhdr.bmi.dao.PrivateSetting;
import ir.mhdr.bmi.dao.PrivateSettingDao;
import ir.mhdr.bmi.dao.User;
import ir.mhdr.bmi.dao.UserDao;
import ir.mhdr.bmi.lib.Statics;

public class PrivateSettingBL {


    public void setActiveUser(User user) {
        PrivateSettingDao privateSettingDao = Statics.daoSession.getPrivateSettingDao();

        PrivateSetting privateSetting = privateSettingDao.queryBuilder().
                where(PrivateSettingDao.Properties.Key.eq("ActiveUser")).unique();

        if (privateSetting == null) {
            privateSetting = new PrivateSetting();
            privateSetting.setKey("ActiveUser");
            privateSetting.setValue(user.getUuid());
            privateSettingDao.insert(privateSetting);
        } else {
            privateSetting.setValue(user.getUuid());
            privateSettingDao.update(privateSetting);
        }


    }

    public User getActiveUser() {

        User user = null;

        PrivateSettingDao privateSettingDao = Statics.daoSession.getPrivateSettingDao();

        PrivateSetting privateSetting = privateSettingDao.queryBuilder().
                where(PrivateSettingDao.Properties.Key.eq("ActiveUser")).unique();

        if (privateSetting != null) {
            String userUuid = privateSetting.getValue();

            UserDao userDao = Statics.daoSession.getUserDao();

            user = userDao.queryBuilder().where(UserDao.Properties.Uuid.eq(userUuid)).unique();
        }

        return user;
    }

    public void createSession() {
        PrivateSettingDao privateSettingDao = Statics.daoSession.getPrivateSettingDao();

        PrivateSetting privateSetting = privateSettingDao.queryBuilder().
                where(PrivateSettingDao.Properties.Key.eq("Session")).unique();

        if (privateSetting == null) {
            privateSetting = new PrivateSetting();
            privateSetting.setKey("Session");
            privateSetting.setValue(UUID.randomUUID().toString());
        }

        privateSettingDao.insert(privateSetting);
    }

    public String getSession() {
        PrivateSettingDao privateSettingDao = Statics.daoSession.getPrivateSettingDao();

        String session = "";

        PrivateSetting privateSetting = privateSettingDao.queryBuilder().
                where(PrivateSettingDao.Properties.Key.eq("Session")).unique();

        if (privateSetting != null) {
            session = privateSetting.getValue();
        }

        return session;
    }

}
