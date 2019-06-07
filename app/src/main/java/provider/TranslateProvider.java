package provider;

import com.alibaba.fastjson.JSON;
import java.io.IOException;
import dto.TranslatePostDTO;
import dto.TranslateResultDTO;
import okhttp3.*;

// 返回翻译结果的类
public class TranslateProvider {
    public TranslateResultDTO getTranslateResuly(TranslatePostDTO translatePostDTO) {
        OkHttpClient client = new OkHttpClient();
        String getURL = "https://fanyi-api.baidu.com/api/trans/vip/translate?q="+translatePostDTO.getQ()+"&from="+
                translatePostDTO.getFrom()+"&to="+translatePostDTO.getTo()+"&appid="+translatePostDTO.getAppid()+
                "&salt="+translatePostDTO.getSalt()+"&sign="+translatePostDTO.getSign();
        System.out.println(getURL);
        Request request = new Request.Builder()
                .get()
                .url(getURL)
                .build();

        try (Response response = client.newCall(request).execute()) {
            String translateRES = response.body().string();
            TranslateResultDTO translateResultDTO = JSON.parseObject(translateRES, TranslateResultDTO.class);
            return translateResultDTO;
        } catch (IOException e) {
            e.printStackTrace();
        }
        return null;
    }
}
