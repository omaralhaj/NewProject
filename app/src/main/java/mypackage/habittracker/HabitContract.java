package mypackage.habittracker;


import android.provider.BaseColumns;

public class HabitContract  {


    private HabitContract(){}

    public static class HabitEntry implements BaseColumns {
        public static final String TABLE_NAME = "habits";
        public static final String COLUMN_NAME = "name";
        public static final String COLUMN_BIRTH = "birth";
        public static final String COLUMN_HABIT = "habit";
    }


}
