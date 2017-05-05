package ir.mhdr.bmi.bl;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import java.util.ArrayList;
import java.util.List;

import ir.mhdr.bmi.DatabaseHandler;
import ir.mhdr.bmi.model.User;

public class UserBL {

    private DatabaseHandler dbHandler;

    public UserBL(DatabaseHandler dbHandler) {
        this.dbHandler = dbHandler;
    }

    public UserBL(Context context) {
        this.dbHandler = new DatabaseHandler(context);
    }

    public long insert(User user) {
        SQLiteDatabase db = this.dbHandler.getWritableDatabase();
        ContentValues values = new ContentValues();

        values.put(DatabaseHandler.Schema_Users.COL2_NAME, user.getName());
        values.put(DatabaseHandler.Schema_Users.COL3_BIRTHDATE, user.getBirthdate());
        values.put(DatabaseHandler.Schema_Users.COL4_GENDER, user.getGender());
        values.put(DatabaseHandler.Schema_Users.COL5_LATEST_HEIGHT, user.getLatestHeight());
        values.put(DatabaseHandler.Schema_Users.COL6_LATEST_WEIGHT, user.getLatestWeight());
        values.put(DatabaseHandler.Schema_Users.COL7_ISACTIVE,user.getIsActive());

        long inserted_id = db.insert(DatabaseHandler.Schema_Users.TABLE_NAME, null, values);
        db.close();

        return inserted_id;
    }

    public Cursor getUserCursor(long id) {
        SQLiteDatabase db = this.dbHandler.getReadableDatabase();

        String[] column = {
                DatabaseHandler.Schema_Users.COL1_ID,
                DatabaseHandler.Schema_Users.COL2_NAME,
                DatabaseHandler.Schema_Users.COL3_BIRTHDATE,
                DatabaseHandler.Schema_Users.COL4_GENDER,
                DatabaseHandler.Schema_Users.COL5_LATEST_HEIGHT,
                DatabaseHandler.Schema_Users.COL6_LATEST_WEIGHT,
                DatabaseHandler.Schema_Users.COL7_ISACTIVE
        };

        String selection = DatabaseHandler.Schema_Users.COL1_ID + " = ?";

        String[] selectionArgs = {String.valueOf(id)};

        Cursor cursor = db.query(DatabaseHandler.Schema_Users.TABLE_NAME, column, selection, selectionArgs, null, null, null);

        return cursor;
    }

    public User getUser(long id) {
        Cursor cursor = this.getUserCursor(id);

        if (cursor != null) {
            cursor.moveToFirst();
        }

        User user = new User();
        user.setId(cursor.getInt(cursor.getColumnIndex(DatabaseHandler.Schema_Users.COL1_ID)));
        user.setName(cursor.getString(cursor.getColumnIndex(DatabaseHandler.Schema_Users.COL2_NAME)));
        user.setBirthdate(cursor.getString(cursor.getColumnIndex(DatabaseHandler.Schema_Users.COL3_BIRTHDATE)));
        user.setGender(cursor.getInt(cursor.getColumnIndex(DatabaseHandler.Schema_Users.COL4_GENDER)));
        user.setLatestHeight(cursor.getString(cursor.getColumnIndex(DatabaseHandler.Schema_Users.COL5_LATEST_HEIGHT)));
        user.setLatestWeight(cursor.getString(cursor.getColumnIndex(DatabaseHandler.Schema_Users.COL6_LATEST_WEIGHT)));
        user.setIsActive(cursor.getInt(cursor.getColumnIndex(DatabaseHandler.Schema_Users.COL7_ISACTIVE)));

        cursor.close();

        return user;
    }

    public Cursor getActiveUserCursor()
    {
        SQLiteDatabase db = this.dbHandler.getReadableDatabase();

        String[] column = {
                DatabaseHandler.Schema_Users.COL1_ID,
                DatabaseHandler.Schema_Users.COL2_NAME,
                DatabaseHandler.Schema_Users.COL3_BIRTHDATE,
                DatabaseHandler.Schema_Users.COL4_GENDER,
                DatabaseHandler.Schema_Users.COL5_LATEST_HEIGHT,
                DatabaseHandler.Schema_Users.COL6_LATEST_WEIGHT,
                DatabaseHandler.Schema_Users.COL7_ISACTIVE
        };

        String selection = DatabaseHandler.Schema_Users.COL7_ISACTIVE + " = ?";

        String isActive="1";

        String[] selectionArgs = {isActive};

        Cursor cursor = db.query(DatabaseHandler.Schema_Users.TABLE_NAME, column, selection, selectionArgs, null, null, null);

        return cursor;
    }

    public User getActiveUser() {
        Cursor cursor = this.getActiveUserCursor();

        if (cursor != null) {
            cursor.moveToFirst();
        }

        User user = new User();
        user.setId(cursor.getInt(cursor.getColumnIndex(DatabaseHandler.Schema_Users.COL1_ID)));
        user.setName(cursor.getString(cursor.getColumnIndex(DatabaseHandler.Schema_Users.COL2_NAME)));
        user.setBirthdate(cursor.getString(cursor.getColumnIndex(DatabaseHandler.Schema_Users.COL3_BIRTHDATE)));
        user.setGender(cursor.getInt(cursor.getColumnIndex(DatabaseHandler.Schema_Users.COL4_GENDER)));
        user.setLatestHeight(cursor.getString(cursor.getColumnIndex(DatabaseHandler.Schema_Users.COL5_LATEST_HEIGHT)));
        user.setLatestWeight(cursor.getString(cursor.getColumnIndex(DatabaseHandler.Schema_Users.COL6_LATEST_WEIGHT)));
        user.setIsActive(cursor.getInt(cursor.getColumnIndex(DatabaseHandler.Schema_Users.COL7_ISACTIVE)));

        cursor.close();

        return user;
    }

    public Cursor getUsersCursor() {
        SQLiteDatabase db = this.dbHandler.getReadableDatabase();

        String[] column = {
                DatabaseHandler.Schema_Users.COL1_ID,
                DatabaseHandler.Schema_Users.COL2_NAME,
                DatabaseHandler.Schema_Users.COL3_BIRTHDATE,
                DatabaseHandler.Schema_Users.COL4_GENDER,
                DatabaseHandler.Schema_Users.COL5_LATEST_HEIGHT,
                DatabaseHandler.Schema_Users.COL6_LATEST_WEIGHT,
                DatabaseHandler.Schema_Users.COL7_ISACTIVE
        };

        Cursor cursor = db.query(DatabaseHandler.Schema_Users.TABLE_NAME, column, null, null, null, null, null);

        return cursor;
    }

    public List<User> getUsers() {
        List<User> result = new ArrayList<>();


        Cursor cursor = this.getUsersCursor();

        if (cursor.moveToFirst()) {
            do {
                User user = new User();
                user.setId(cursor.getInt(cursor.getColumnIndex(DatabaseHandler.Schema_Users.COL1_ID)));
                user.setName(cursor.getString(cursor.getColumnIndex(DatabaseHandler.Schema_Users.COL2_NAME)));
                user.setBirthdate(cursor.getString(cursor.getColumnIndex(DatabaseHandler.Schema_Users.COL3_BIRTHDATE)));
                user.setGender(cursor.getInt(cursor.getColumnIndex(DatabaseHandler.Schema_Users.COL4_GENDER)));
                user.setLatestHeight(cursor.getString(cursor.getColumnIndex(DatabaseHandler.Schema_Users.COL5_LATEST_HEIGHT)));
                user.setLatestWeight(cursor.getString(cursor.getColumnIndex(DatabaseHandler.Schema_Users.COL6_LATEST_WEIGHT)));
                user.setIsActive(cursor.getInt(cursor.getColumnIndex(DatabaseHandler.Schema_Users.COL7_ISACTIVE)));

                result.add(user);

            } while (cursor.moveToNext());
        }

        return result;
    }

    public int countUsers() {
        Cursor cursor=this.getUsersCursor();
        int result = cursor.getCount();

        cursor.close();

        return result;
    }
}
