package vardanvanyan.com.newsfeed.database;

import android.arch.persistence.room.Database;
import android.arch.persistence.room.Room;
import android.arch.persistence.room.RoomDatabase;
import android.content.Context;

import vardanvanyan.com.newsfeed.database.dao.NewsDao;
import vardanvanyan.com.newsfeed.database.entities.News;

/**
 * Designs news database and opens access to database.
 */
@Database(entities = {News.class}, version = 1, exportSchema = false)
public abstract class AppDataBase extends RoomDatabase {

    private static final String APP_DATABASE_NAME = "news-db";

    private static AppDataBase instance;

    public abstract NewsDao newsDao();

    /**
     * Gets news database (designed using Room architecture).
     *
     * @param context For accessing application context
     * @return News database
     */
    public static AppDataBase getAppDatabase(final Context context) {
        if (instance == null) {
            instance = Room.databaseBuilder(context.getApplicationContext(),
                    AppDataBase.class, APP_DATABASE_NAME)
                    .allowMainThreadQueries()
                    .build();
        }
        return instance;
    }
}
