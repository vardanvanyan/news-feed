
package vardanvanyan.com.newsfeed.api.models;

import java.util.List;
import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class Element_ {

    @SerializedName("type")
    @Expose
    private String type;
    @SerializedName("assets")
    @Expose
    private List<Object> assets = null;
    @SerializedName("textTypeData")
    @Expose
    private TextTypeData textTypeData;

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public List<Object> getAssets() {
        return assets;
    }

    public void setAssets(List<Object> assets) {
        this.assets = assets;
    }

    public TextTypeData getTextTypeData() {
        return textTypeData;
    }

    public void setTextTypeData(TextTypeData textTypeData) {
        this.textTypeData = textTypeData;
    }

}
