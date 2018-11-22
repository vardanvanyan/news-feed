package vardanvanyan.com.newsfeed.database.dao;

import android.arch.persistence.room.Dao;
import android.arch.persistence.room.Delete;
import android.arch.persistence.room.Insert;
import android.arch.persistence.room.Query;
import android.arch.persistence.room.Update;

import vardanvanyan.com.newsfeed.database.entities.News;

/**
 * News DAO model for queering from/to database.
 */
@Dao
public interface NewsDao {

    @Insert
    void insert(News... person);

    @Update
    void update(News... person);

    @Delete
    void delete(News... person);

    @Query("Select * FROM news")
    News[] loadAll();

    @Query("SELECT * FROM news WHERE id = :newsId ")
    News loadNewsById(final int newsId);

    @Query("SELECT id FROM news WHERE news_title = :newsTitle ")
    int getNewsIdByTitle(final String newsTitle);
}