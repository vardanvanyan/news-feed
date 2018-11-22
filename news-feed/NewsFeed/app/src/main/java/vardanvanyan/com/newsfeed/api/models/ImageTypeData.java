
package vardanvanyan.com.newsfeed.api.models;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class ImageTypeData {

    @SerializedName("caption")
    @Expose
    private String caption;
    @SerializedName("displayCredit")
    @Expose
    private Boolean displayCredit;
    @SerializedName("credit")
    @Expose
    private String credit;
    @SerializedName("source")
    @Expose
    private String source;
    @SerializedName("photographer")
    @Expose
    private String photographer;
    @SerializedName("alt")
    @Expose
    private String alt;
    @SerializedName("mediaId")
    @Expose
    private String mediaId;
    @SerializedName("mediaApiUri")
    @Expose
    private String mediaApiUri;
    @SerializedName("suppliersReference")
    @Expose
    private String suppliersReference;
    @SerializedName("imageType")
    @Expose
    private String imageType;

    public String getCaption() {
        return caption;
    }

    public void setCaption(String caption) {
        this.caption = caption;
    }

    public Boolean getDisplayCredit() {
        return displayCredit;
    }

    public void setDisplayCredit(Boolean displayCredit) {
        this.displayCredit = displayCredit;
    }

    public String getCredit() {
        return credit;
    }

    public void setCredit(String credit) {
        this.credit = credit;
    }

    public String getSource() {
        return source;
    }

    public void setSource(String source) {
        this.source = source;
    }

    public String getPhotographer() {
        return photographer;
    }

    public void setPhotographer(String photographer) {
        this.photographer = photographer;
    }

    public String getAlt() {
        return alt;
    }

    public void setAlt(String alt) {
        this.alt = alt;
    }

    public String getMediaId() {
        return mediaId;
    }

    public void setMediaId(String mediaId) {
        this.mediaId = mediaId;
    }

    public String getMediaApiUri() {
        return mediaApiUri;
    }

    public void setMediaApiUri(String mediaApiUri) {
        this.mediaApiUri = mediaApiUri;
    }

    public String getSuppliersReference() {
        return suppliersReference;
    }

    public void setSuppliersReference(String suppliersReference) {
        this.suppliersReference = suppliersReference;
    }

    public String getImageType() {
        return imageType;
    }

    public void setImageType(String imageType) {
        this.imageType = imageType;
    }

}
