package ir.mhdr.bmi.bl;


import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import org.joda.time.DateTime;

import java.util.ArrayList;
import java.util.List;

import ir.mhdr.bmi.lib.DatabaseHandler;
import ir.mhdr.bmi.model.History;
import ir.mhdr.bmi.model.User;

public class HistoryBL {

    private DatabaseHandler dbHandler;

    public HistoryBL(DatabaseHandler dbHandler) {
        this.dbHandler = dbHandler;
    }

    public HistoryBL(Context context) {
        this.dbHandler = new DatabaseHandler(context);
    }

    public long insert(History history) {
        SQLiteDatabase db = this.dbHandler.getWritableDatabase();
        ContentValues values = new ContentValues();

        values.put(DatabaseHandler.Schema_History.COL2_USER_ID, history.getUserId());
        values.put(DatabaseHandler.Schema_History.COL3_DATETIME, history.getDatetime());
        values.put(DatabaseHandler.Schema_History.COL4_VALUE, history.getValue());

        long inserted_id = db.insert(DatabaseHandler.Schema_History.TABLE_NAME, null, values);
        db.close();

        return inserted_id;
    }

    public Cursor getHistoryCursor(User user) {
        SQLiteDatabase db = this.dbHandler.getReadableDatabase();

        String[] columns = {
                DatabaseHandler.Schema_History.COL1_ID,
                DatabaseHandler.Schema_History.COL2_USER_ID,
                DatabaseHandler.Schema_History.COL3_DATETIME,
                DatabaseHandler.Schema_History.COL4_VALUE
        };

        String selection = DatabaseHandler.Schema_History.COL2_USER_ID + " = ?";
        String[] selectionArgs = {String.valueOf(user.getId())};

        Cursor cursor = db.query(DatabaseHandler.Schema_History.TABLE_NAME, columns, selection, selectionArgs, null, null, null);

        return cursor;
    }

    public List<History> getHistory(User user) {
        List<History> result = new ArrayList<>();


        Cursor cursor = this.getHistoryCursor(user);

        if (cursor.moveToFirst()) {
            do {

                History history = new History();
                history.setId(cursor.getInt(cursor.getColumnIndex(DatabaseHandler.Schema_History.COL1_ID)));
                history.setUserId(cursor.getInt(cursor.getColumnIndex(DatabaseHandler.Schema_History.COL2_USER_ID)));
                history.setDatetime(cursor.getString(cursor.getColumnIndex(DatabaseHandler.Schema_History.COL3_DATETIME)));
                history.setValue(cursor.getString(cursor.getColumnIndex(DatabaseHandler.Schema_History.COL4_VALUE)));

                result.add(history);

            } while (cursor.moveToNext());
        }

        cursor.close();
        return result;
    }

    public int delete(History history) {
        SQLiteDatabase db = this.dbHandler.getWritableDatabase();
        int rows_affected = db.delete(DatabaseHandler.Schema_History.TABLE_NAME,
                DatabaseHandler.Schema_History.COL1_ID + " = ?",
                new String[]{String.valueOf(history.getId())});
        db.close();

        return rows_affected;
    }

    public int deleteAllByUser(User user) {
        SQLiteDatabase db = this.dbHandler.getWritableDatabase();
        int rows_affected = db.delete(DatabaseHandler.Schema_History.TABLE_NAME,
                DatabaseHandler.Schema_History.COL2_USER_ID + " = ?",
                new String[]{String.valueOf(user.getId())});
        db.close();

        return rows_affected;
    }

    public int update(History history) {
        SQLiteDatabase db = this.dbHandler.getWritableDatabase();

        ContentValues values = new ContentValues();
        values.put(DatabaseHandler.Schema_History.COL2_USER_ID, history.getUserId());
        values.put(DatabaseHandler.Schema_History.COL3_DATETIME, history.getDatetime());
        values.put(DatabaseHandler.Schema_History.COL4_VALUE, history.getValue());

        int rows_affected = db.update(DatabaseHandler.Schema_History.TABLE_NAME, values,
                DatabaseHandler.Schema_History.COL1_ID + " = ?",
                new String[]{String.valueOf(history.getId())});
        db.close();

        return rows_affected;
    }

    public History getLastHistory(User user) {
        Cursor cursor = this.getHistoryCursor(user);
        cursor.moveToLast();

        History history = new History();
        history.setId(cursor.getInt(cursor.getColumnIndex(DatabaseHandler.Schema_History.COL1_ID)));
        history.setUserId(cursor.getInt(cursor.getColumnIndex(DatabaseHandler.Schema_History.COL2_USER_ID)));
        history.setDatetime(cursor.getString(cursor.getColumnIndex(DatabaseHandler.Schema_History.COL3_DATETIME)));
        history.setValue(cursor.getString(cursor.getColumnIndex(DatabaseHandler.Schema_History.COL4_VALUE)));

        cursor.close();

        return history;
    }
}
