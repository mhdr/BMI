package ir.mhdr.bmi.bl;

import ir.mhdr.bmi.DatabaseHandler;

public class UserBL {

    private DatabaseHandler dbHandler;

    public UserBL(DatabaseHandler dbHandler)
    {
        this.dbHandler=dbHandler;
    }
}
