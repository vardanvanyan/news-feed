package vardanvanyan.com.newsfeed.database.entities;

import android.arch.persistence.room.ColumnInfo;
import android.arch.persistence.room.Entity;
import android.arch.persistence.room.PrimaryKey;

/**
 * News Entity (alternative fot table) for saving news detailed info into database
 */
@Entity(tableName = "news")
public class News {

    @PrimaryKey(autoGenerate = true)
    public int id;

    @ColumnInfo(name = "news_title")
    public String newsTitle;

    @ColumnInfo(name = "news_category")
    public String newsCategory;

    @ColumnInfo (name = "news_description")
    public String newsDescription;

    @ColumnInfo(name = "news_image", typeAffinity = ColumnInfo.BLOB)
    private byte[] newsImage;

    public String getNewsTitle() {
        return newsTitle;
    }

    public String getNewsCategory() {
        return newsCategory;
    }

    public String getNewsDescription() {
        return newsDescription;
    }

    public byte[] getNewsImage() {
        return newsImage;
    }

    public void setId(int id) {
        this.id = id;
    }

    public void setNewsTitle(String newsTitle) {
        this.newsTitle = newsTitle;
    }

    public void setNewsCategory(String newsCategory) {
        this.newsCategory = newsCategory;
    }

    public void setNewsDescription(String newsDescription) {
        this.newsDescription = newsDescription;
    }

    public void setNewsImage(byte[] newsImage) {
        this.newsImage = newsImage;
    }
}
