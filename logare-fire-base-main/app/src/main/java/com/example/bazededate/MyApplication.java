package com.example.bazededate;

import android.app.Application;

import androidx.room.Room;
import androidx.room.migration.Migration;
import androidx.sqlite.db.SupportSQLiteDatabase;

public class MyApplication extends Application {
    private static MyApplication mInstance;
    private static AppDatabase appDatabase;
    static final Migration MIGRATION_1_4 = new Migration(4, 5) { // From version 1 to version 2
        @Override
        public void migrate(SupportSQLiteDatabase database) {
            // Remove the table
             // Use the right table name
            // OR: We could update it, by using an ALTER query
            // database.execSQL("create table user if not ex");
            // OR: If needed, we can create the table again with the required settings
            // database.execSQL("CREATE TABLE IF NOT EXISTS my_table (id INTEGER, PRIMARY KEY(id), ...)")
        }
    };


    @Override
    public void onCreate() {
        super.onCreate();

        mInstance = this;
        appDatabase = Room.databaseBuilder(getApplicationContext(), AppDatabase.class, "database-name").addMigrations(MIGRATION_1_4).build();
    }

    public static MyApplication getInstance() {
        return mInstance;
    }

    public static AppDatabase getAppDatabase() {
        return appDatabase;
    }
}
