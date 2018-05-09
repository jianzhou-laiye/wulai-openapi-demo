package com.ai.wul;

import java.io.IOException;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.HashMap;
import java.util.UUID;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import org.apache.commons.codec.digest.DigestUtils;
import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.HttpClientBuilder;
import org.apache.http.util.EntityUtils;


public class Main {


    static String pubkey = "输入pubkey的值";
    static String secret = "从Console.wul.ai获取到secret";
    static String nonce = UUID.randomUUID().toString().replace("-", "");
    private static MessageDigest md;

    static {
        try {
            md = MessageDigest.getInstance("SHA");
        } catch (NoSuchAlgorithmException e) {
            e.printStackTrace();
        }
    }


    /**
     * 获取带Header的请求
     * @param url
     * @param pubkey
     * @param nonce
     * @param timeStamp
     * @return
     */
    private static HttpPost getHttpPost(String url, String pubkey, String nonce, Long timeStamp) {
        HttpPost request = new HttpPost(url);
        request.addHeader("content-type", "application/json");
        request.addHeader("Api-Auth-pubkey", pubkey);
        request.addHeader("Api-Auth-timestamp", String.valueOf(timeStamp));
        request.addHeader("Api-Auth-nonce", nonce);
        request.addHeader("Api-Auth-sign", DigestUtils.sha1Hex(nonce + timeStamp + secret));
        return request;
    }

    /**
     * 如果不使用DigestUtils的方法，可以用此方法计算哈希值
     * @param nonce
     * @param timeStamp
     * @param secret
     * @return
     */
    private static String getSign(String nonce, Long timeStamp, String secret) {
        String source = nonce + timeStamp + secret;
        StringBuilder buffer = new StringBuilder();
        try {
            MessageDigest sha = (MessageDigest) md.clone();
            sha.update(source.getBytes());
            for (byte b : sha.digest()) {
                buffer.append(String.format("%02X", b));
            }
            return buffer.toString().toLowerCase();
        } catch (CloneNotSupportedException e) {
            e.printStackTrace();
        }
        return null;
    }

    public static void main(String[] args) {
        Gson gson = new GsonBuilder().create();
        Long timeStamp = System.currentTimeMillis() / 1000;

        String url = "https://openapi.wul.ai/v1/user/create";
        HttpPost post = getHttpPost(url, pubkey, nonce, timeStamp);
        HttpClient httpClient = HttpClientBuilder.create().build();
        HashMap<String, String> params = new HashMap<>();
        params.put("imgurl", "http://your_avatar");
        params.put("nickname", "展示的用户名");
        params.put("username", "id_xxxx");
        try {
            post.setEntity(new ByteArrayEntity(body.getBytes("UTF-8")));
            HttpResponse response = httpClient.execute(post);
            HttpEntity entity = response.getEntity();
            String responseString = EntityUtils.toString(entity, "UTF-8");
            System.out.println(responseString);
        } catch (IOException e) {
            e.printStackTrace();
        }

    }
}
