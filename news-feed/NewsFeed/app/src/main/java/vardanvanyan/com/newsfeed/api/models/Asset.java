
package vardanvanyan.com.newsfeed.api.models;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class Asset {

    @SerializedName("type")
    @Expose
    private String type;
    @SerializedName("mimeType")
    @Expose
    private String mimeType;
    @SerializedName("file")
    @Expose
    private String file;
    @SerializedName("typeData")
    @Expose
    private TypeData typeData;

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getMimeType() {
        return mimeType;
    }

    public void setMimeType(String mimeType) {
        this.mimeType = mimeType;
    }

    public String getFile() {
        return file;
    }

    public void setFile(String file) {
        this.file = file;
    }

    public TypeData getTypeData() {
        return typeData;
    }

    public void setTypeData(TypeData typeData) {
        this.typeData = typeData;
    }

}
