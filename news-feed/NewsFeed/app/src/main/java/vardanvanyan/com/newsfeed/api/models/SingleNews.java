package vardanvanyan.com.newsfeed.api.models;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class SingleNews {

    @SerializedName("response")
    @Expose
    private ResponseSingleNews response;

    public ResponseSingleNews getResponse() {
        return response;
    }

    public void setResponse(ResponseSingleNews response) {
        this.response = response;
    }
}
