package provider;

import com.alibaba.fastjson.JSON;
import java.io.IOException;
import java.io.UnsupportedEncodingException;

import dto.TranslatePostDTO;
import dto.TranslateResultDTO;
import okhttp3.*;

// 返回翻译结果的类
public class TranslateProvider {
    public TranslateResultDTO getTranslateResuly(TranslatePostDTO translatePostDTO) {
        OkHttpClient client = new OkHttpClient();
        String q = "";
        String from = "";
        String to = "";
        String appid = "";
        String salt = "";
        String sign = "";
        try {
           q = java.net.URLEncoder.encode(translatePostDTO.getQ(), "utf-8");
           from = java.net.URLEncoder.encode(translatePostDTO.getFrom(), "utf-8");
           to = java.net.URLEncoder.encode(translatePostDTO.getTo(), "utf-8");
           appid = java.net.URLEncoder.encode(translatePostDTO.getAppid(),"utf-8");
           salt = java.net.URLEncoder.encode(translatePostDTO.getSalt(),"utf-8");
           sign = java.net.URLEncoder.encode(translatePostDTO.getSign(),"utf-8");
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }
        String getURL = "https://fanyi-api.baidu.com/api/trans/vip/translate?q="+q+"&from="+
                from+"&to="+to+"&appid="+appid+
                "&salt="+salt+"&sign="+sign;
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
