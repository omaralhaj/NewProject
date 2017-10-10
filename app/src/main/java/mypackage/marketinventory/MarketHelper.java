package mypackage.marketinventory;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;


public class MarketHelper extends SQLiteOpenHelper {

    public static final String DATABASE_NAME = "my_products.db";
    public static final int DATABASE_VERSION = 1;

    public MarketHelper(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {

        String SQL_CREATE_ENTRIES = "CREATE TABLE " + MarketContract.ProductEntry.TABLE_NAME + "(" +
                MarketContract.ProductEntry._ID + " INTEGER PRIMARY KEY AUTOINCREMENT," +
                MarketContract.ProductEntry.COLUMN_NAME + " TEXT," +
                MarketContract.ProductEntry.COLUMN_PRICE + " INTEGER," +
                MarketContract.ProductEntry.COLUMN_QUANTITY + " INTEGER," +
                MarketContract.ProductEntry.COLUMN_PIC + " BLOB)";
        db.execSQL(SQL_CREATE_ENTRIES);

    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldV, int newV) {
        db.execSQL("DROP TABLE IF EXISTS " + MarketContract.ProductEntry.TABLE_NAME);
        onCreate(db);
    }


}
