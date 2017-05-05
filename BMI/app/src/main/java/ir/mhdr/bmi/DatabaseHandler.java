package ir.mhdr.bmi;


import android.content.Context;
import android.database.DatabaseErrorHandler;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

public class DatabaseHandler extends SQLiteOpenHelper {

    public static class Schema_DB {
        private static int DATABASE_VERSION = 1;
        private static String DATABASE_NAME = "bmi";
    }

    public DatabaseHandler(Context context) {
        super(context, Schema_DB.DATABASE_NAME, null, Schema_DB.DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL(Schema_Users.QUERY_CREATE_TABLE);
        db.execSQL(Schema_History.QUERY_CREATE_TABLE);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        if (newVersion>oldVersion)
        {
            db.execSQL(Schema_Users.QUERY_DROP_TABLE);
            db.execSQL(Schema_History.QUERY_DROP_TABLE);

            db.execSQL(Schema_Users.QUERY_CREATE_TABLE);
            db.execSQL(Schema_History.QUERY_CREATE_TABLE);
        }
    }

    public static class Schema_Users {
        public static final String TABLE_NAME = "users";
        public static final String COL1_ID = "_id";
        public static final String COL2_NAME = "name";
        public static final String COL3_BIRTHDATE = "birthdate";
        public static final String COL4_GENDER = "gender";
        public static final String COL5_LATEST_HEIGHT = "latest_height";
        public static final String COL6_LATEST_WEIGHT = "latest_weight";
        public static final String COL7_ISACTIVE="isActive";
        public static final String QUERY_CREATE_TABLE=String.format("CREATE TABLE \"%s\" (\n" +
                "\"%s\"  INTEGER PRIMARY KEY AUTOINCREMENT NOT NULL,\n" +
                "\"%s\"  TEXT NOT NULL,\n" +
                "\"%s\"  TEXT NOT NULL,\n" +
                "\"%s\"  INTEGER NOT NULL,\n" +
                "\"%s\"  TEXT NOT NULL,\n" +
                "\"%s\"  TEXT NOT NULL,\n" +
                "\"%s\"  INTEGER NOT NULL\n" +
                ");",TABLE_NAME,COL1_ID,COL2_NAME,COL3_BIRTHDATE,COL4_GENDER,COL5_LATEST_HEIGHT,COL6_LATEST_WEIGHT,COL7_ISACTIVE);
        public static final String QUERY_DROP_TABLE =String.format("DROP TABLE IF EXISTS \"%s\";",TABLE_NAME);
    }

    public static class Schema_History {
        public static final String TABLE_NAME = "history";
        public static final String COL1_ID = "_id";
        public static final String COL2_USER_ID = "user_id";
        public static final String COL3_DATETIME = "datetime";
        public static final String COL4_VALUE = "value";
        public static final String COL5_TYPE = "type";
        public static final String QUERY_CREATE_TABLE=String.format("CREATE TABLE \"%s\" (\n" +
                "\"%s\"  INTEGER PRIMARY KEY AUTOINCREMENT NOT NULL,\n" +
                "\"%s\"  INTEGER NOT NULL,\n" +
                "\"%s\"  TEXT NOT NULL,\n" +
                "\"%s\"  TEXT NOT NULL,\n" +
                "\"%s\"  INTEGER NOT NULL\n" +
                ");",TABLE_NAME,COL1_ID,COL2_USER_ID,COL3_DATETIME,COL4_VALUE,COL5_TYPE);
        public static final String QUERY_DROP_TABLE =String.format("DROP TABLE IF EXISTS \"%s\";",TABLE_NAME);
    }
}
