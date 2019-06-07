package provider;

import com.alibaba.fastjson.JSON;

import java.io.IOException;

import dto.TranslatePostDTO;
import okhttp3.*;

public class TranslateProvider {
    public String getTranslateResuly(TranslatePostDTO translatePostDTO) {
        MediaType mediaType = MediaType.get("application/json: charset=utf-8");
        OkHttpClient client = new OkHttpClient();
        RequestBody body = RequestBody.create(mediaType, JSON.toJSONString(translatePostDTO));

        Request request = new Request.Builder()
                .url("https://fanyi-api.baidu.com/api/trans/vip/translate")
                .build();

        try (Response response = client.newCall(request).execute()) {
            String  response.body().string();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
