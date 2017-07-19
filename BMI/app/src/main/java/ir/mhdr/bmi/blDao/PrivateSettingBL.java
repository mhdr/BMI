package ir.mhdr.bmi.blDao;


import org.joda.time.DateTime;

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
            privateSettingDao.insert(privateSetting);
        }
    }

    public void createActiveUser() {
        PrivateSettingDao privateSettingDao = Statics.daoSession.getPrivateSettingDao();

        PrivateSetting privateSetting = privateSettingDao.queryBuilder().
                where(PrivateSettingDao.Properties.Key.eq("ActiveUser")).unique();

        if (privateSetting == null) {
            privateSetting = new PrivateSetting();
            privateSetting.setKey("ActiveUser");
            privateSetting.setValue(null);
            privateSettingDao.insert(privateSetting);
        }
    }

    public void createSettings() {
        createSession();
        createActiveUser();
        createBmiRangeMode();
        createImportDbV1();
    }

    public void createBmiRangeMode() {
        PrivateSettingDao privateSettingDao = Statics.daoSession.getPrivateSettingDao();

        PrivateSetting privateSetting = privateSettingDao.queryBuilder().
                where(PrivateSettingDao.Properties.Key.eq("BmiRangeMode")).unique();

        if (privateSetting == null) {
            privateSetting = new PrivateSetting();
            privateSetting.setKey("BmiRangeMode");
            privateSetting.setValue("-1");
            privateSettingDao.insert(privateSetting);
        }
    }

    public int getBmiRangeMode() {
        PrivateSettingDao privateSettingDao = Statics.daoSession.getPrivateSettingDao();

        int result = -1;

        PrivateSetting privateSetting = privateSettingDao.queryBuilder().
                where(PrivateSettingDao.Properties.Key.eq("BmiRangeMode")).unique();

        if (privateSetting != null) {
            String value = privateSetting.getValue();
            result = Integer.parseInt(value);
        }

        return result;
    }

    public void setBmiRange(int value) {
        PrivateSettingDao privateSettingDao = Statics.daoSession.getPrivateSettingDao();

        PrivateSetting privateSetting = privateSettingDao.queryBuilder().
                where(PrivateSettingDao.Properties.Key.eq("BmiRangeMode")).unique();

        privateSetting.setValue(String.valueOf(value));
        privateSettingDao.update(privateSetting);
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

    public void createImportDbV1() {
        PrivateSettingDao privateSettingDao = Statics.daoSession.getPrivateSettingDao();

        PrivateSetting privateSetting = privateSettingDao.queryBuilder().
                where(PrivateSettingDao.Properties.Key.eq("ImportDbV1")).unique();

        if (privateSetting == null) {
            privateSetting = new PrivateSetting();
            privateSetting.setKey("ImportDbV1");
            privateSetting.setValue("0");
            privateSettingDao.insert(privateSetting);
        }
    }

    public int getImportDbV1() {
        PrivateSettingDao privateSettingDao = Statics.daoSession.getPrivateSettingDao();

        int result = 0;

        PrivateSetting privateSetting = privateSettingDao.queryBuilder().
                where(PrivateSettingDao.Properties.Key.eq("ImportDbV1")).unique();

        if (privateSetting != null) {
            String value = privateSetting.getValue();
            result = Integer.parseInt(value);
        }

        return result;
    }

    public void setImportDbV1(int value) {
        PrivateSettingDao privateSettingDao = Statics.daoSession.getPrivateSettingDao();

        PrivateSetting privateSetting = privateSettingDao.queryBuilder().
                where(PrivateSettingDao.Properties.Key.eq("ImportDbV1")).unique();

        privateSetting.setValue(String.valueOf(value));
        privateSettingDao.update(privateSetting);
    }
}
