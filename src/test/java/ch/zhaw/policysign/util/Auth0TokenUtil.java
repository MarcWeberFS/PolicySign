package ch.zhaw.policysign.util;

import java.io.IOException;

import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import okhttp3.MediaType;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;

public class Auth0TokenUtil {

    public static String getAuth0Token(String username, String password) throws Exception {
        OkHttpClient client = new OkHttpClient();

        MediaType mediaType = MediaType.parse("application/json");
        RequestBody body = RequestBody.create(mediaType, "{\n  \"grant_type\": \"password\",\n  \"username\": \"" + username + "\",\n  \"password\": \"" + password + "\",\n  \"audience\": \"https://dev-s7l02gby3olradaj.us.auth0.com/api/v2/\",\n  \"scope\": \"openid profile email\",\n  \"client_id\": \"" + "zVJq0X5dmu4EPMAuDnJbSDgIIyPbg4Wg" + "\"\n}");
        Request request = new Request.Builder()
            .url("https://dev-s7l02gby3olradaj.us.auth0.com/oauth/token")
            .post(body)
            .addHeader("content-type", "application/json")
            .build();

        Response response = client.newCall(request).execute();
        if (!response.isSuccessful()) throw new IOException("Unexpected code " + response);

        // Parse the response body to extract the token
        String responseBody = response.body().string();
        JsonObject jsonObject = JsonParser.parseString(responseBody).getAsJsonObject();
        return jsonObject.get("access_token").getAsString();
    }
}
