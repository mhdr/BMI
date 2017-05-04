package ir.mhdr.bmi.bl;


import android.content.ContentValues;
import android.content.Context;
import android.database.sqlite.SQLiteDatabase;

import ir.mhdr.bmi.DatabaseHandler;
import ir.mhdr.bmi.model.History;
import ir.mhdr.bmi.model.User;

public class HistoyBL {

    private DatabaseHandler dbHandler;

    public HistoyBL(DatabaseHandler dbHandler) {
        this.dbHandler = dbHandler;
    }

    public HistoyBL(Context context) {
        this.dbHandler = new DatabaseHandler(context);
    }

    public long insert(History history)
    {
        SQLiteDatabase db = this.dbHandler.getWritableDatabase();
        ContentValues values = new ContentValues();

        values.put(DatabaseHandler.Schema_History.COL2_USER_ID,history.getUserId());
        values.put(DatabaseHandler.Schema_History.COL3_DATETIME,history.getDatetime());
        values.put(DatabaseHandler.Schema_History.COL4_VALUE,history.getValue());
        values.put(DatabaseHandler.Schema_History.COL5_TYPE,history.getType());

        long inserted_id = db.insert(DatabaseHandler.Schema_History.TABLE_NAME, null, values);
        db.close();

        return inserted_id;
    }
}
