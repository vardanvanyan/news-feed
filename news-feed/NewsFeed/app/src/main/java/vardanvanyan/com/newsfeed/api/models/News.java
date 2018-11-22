
package vardanvanyan.com.newsfeed.api.models;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class News {

    @SerializedName("response")
    @Expose
    private ResponseAllNews response;

    public ResponseAllNews getResponse() {
        return response;
    }

    public void setResponse(ResponseAllNews response) {
        this.response = response;
    }

}
