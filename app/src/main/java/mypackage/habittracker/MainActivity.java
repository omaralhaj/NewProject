package mypackage.habittracker;

import android.content.ContentValues;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.TextView;
import android.widget.Toast;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        putValues("Omar" , 1997 ,"Programming");
        readValues("Omar");

    }

    public void putValues(String name, int birth , String habit){

        HabitHelper mDbHelper = new HabitHelper(this);
        SQLiteDatabase db = mDbHelper.getWritableDatabase();
        ContentValues values = new ContentValues();

        values.put(HabitContract.HabitEntry.COLUMN_NAME, name);
        values.put(HabitContract.HabitEntry.COLUMN_BIRTH, birth);
        values.put(HabitContract.HabitEntry.COLUMN_HABIT, habit);

        long newRowId = db.insert(HabitContract.HabitEntry.TABLE_NAME, null, values);

        if (newRowId == -1) {
            Toast.makeText(this, "Error with saving Habit", Toast.LENGTH_SHORT).show();
        } else {
            Toast.makeText(this, "Habit saved with row id: " + newRowId, Toast.LENGTH_SHORT).show();
        }
    }


    public Cursor readValues( String name){
        HabitHelper mDbHelper = new HabitHelper(this);

        SQLiteDatabase db = mDbHelper.getReadableDatabase();
        String[] projection = {
                HabitContract.HabitEntry._ID,
                HabitContract.HabitEntry.COLUMN_NAME,
                HabitContract.HabitEntry.COLUMN_BIRTH,
                HabitContract.HabitEntry.COLUMN_HABIT
        };

        String selection = HabitContract.HabitEntry.COLUMN_NAME + " = ?";
        String[] selectionArgs = {name};

        String sortOrder =
                HabitContract.HabitEntry._ID + " DESC";
        Cursor cursor = db.query(
                HabitContract.HabitEntry.TABLE_NAME,
                projection,
                selection,
                selectionArgs,
                null,
                null,
                sortOrder
        );

        if (cursor.moveToFirst()) {
            do {
                Toast.makeText(this,
                        cursor.getString(cursor.getColumnIndex(HabitContract.HabitEntry._ID)),
                        Toast.LENGTH_SHORT)
                        .show();
            } while (cursor.moveToNext());


        }
        return cursor;

    }

}
