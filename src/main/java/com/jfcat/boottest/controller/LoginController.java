package com.jfcat.boottest.controller;

import com.alibaba.fastjson.JSONObject;
import org.apache.http.HttpEntity;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.util.EntityUtils;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.thymeleaf.util.StringUtils;

import java.util.HashMap;
import java.util.Map;
import java.util.Set;

@RestController
@CrossOrigin
public class LoginController {

    //授权成功后的回调,我们需要在这个方法中拿到code去请求token
    @RequestMapping("/callback")
    public String callback(String code, String state) throws Exception{
        //获取到code和state
        System.out.println("code:"+code);
        System.out.println("state:"+state);
        Map<String, String> responseMap = null;
        if(!StringUtils.isEmpty(code)&&!StringUtils.isEmpty(state)){
            //拿到我们的code,去请求token
            //发送一个请求到
            String token_url = GitHubConstants.TOKEN_URL.replace("CLIENT_ID", GitHubConstants.CLIENT_ID)
                    .replace("CLIENT_SECRET", GitHubConstants.CLIENT_SECRET)
                    .replace("CALLBACK", GitHubConstants.CALLBACK)
                    .replace("CODE", code);
//           System.out.println("用户信息数据"+token_url);//这个里面有我们想要的用户信息数据
            String responseStr = HttpClientUtils.doGet(token_url);
            String token = HttpClientUtils.parseResponseEntity(responseStr).get("access_token");

            //根据token发送请求获取登录人的信息
            String userinfo_url = GitHubConstants.USER_INFO_URL.replace("TOKEN", token);
            responseStr = HttpClientUtils.doGet(userinfo_url);//json

            responseMap = HttpClientUtils.parseResponseEntityJSON(responseStr);
            System.out.println("登录用户信息:"+responseMap);//responseMap里面保存着用户登录信息
            System.out.println("获取登录用户的用户名:"+responseMap.get("login"));
        }
        return JSONObject.toJSONString(responseMap);// TODO 修改成自己需要返回的页面...
    }
}
//抽取出来的参数【代码拷贝下来只需要修改成自己的CLIENT_ID，Client CLIENT_SECRET，CALLBACK即可】
class GitHubConstants {
    public static final String CLIENT_ID = "6958a20a6482097f2387"; // TODO 修改成自己的
    public static final String CLIENT_SECRET = "de55d46838e41bcf888f3965df0ac3c0e3e8c4c6";  // TODO 修改成自己的
    public static final String CALLBACK = "http://localhost:8080/callback";  // TODO 修改成自己的  [注意：callback要和注册的回调路径保持一致  否则登录授权之后会报NullPointerException]

    //获取code的url
    public static final String CODE_URL = "https://github.com/login/oauth/authorize?client_id=CLIENT_ID&state=STATE&redirect_uri=CALLBACK";
    //获取token的url
    public static final String TOKEN_URL = "https://github.com/login/oauth/access_token?client_id=CLIENT_ID&client_secret=CLIENT_SECRET&code=CODE&redirect_uri=CALLBACK";
    //获取用户信息的url
    public static final String USER_INFO_URL = "https://api.github.com/user?access_token=TOKEN";
}
//工具类
class HttpClientUtils {
    /**
     * 使用HttpClient发送一个Get方式的请求
     *
     * @param url 请求的路径 请求参数拼接到url后面
     * @return 响应的数据
     * @throws Exception
     */
    public static String doGet(String url) throws Exception {
        CloseableHttpClient httpclient = HttpClients.createDefault();
        HttpGet httpGet = new HttpGet(url);
        CloseableHttpResponse response = httpclient.execute(httpGet); //发送一个http请求
        //如果响应成功,解析响应结果
        if (response.getStatusLine().getStatusCode() == 200) {
            HttpEntity responseEntity = response.getEntity(); //获取响应的内容
            return EntityUtils.toString(responseEntity);
        }
        return null;
    }

    // 参数的封装
    public static Map<String, String> parseResponseEntity(String responseEntityStr) {
        Map<String, String> map = new HashMap<>();
        String[] strs = responseEntityStr.split("\\&");
        for (String str : strs) {
            String[] mapStrs = str.split("=");
            String value = null;
            String key = mapStrs[0];
            if (mapStrs.length > 1) {
                value = mapStrs[1];
            }
            map.put(key, value);
        }
        return map;
    }

    //json字符串转map
    public static Map<String, String> parseResponseEntityJSON(String responseEntityStr) {
        Map<String, String> map = new HashMap<>();
        JSONObject jsonObject = JSONObject.parseObject(responseEntityStr); //解析json格式的字符串
        Set<Map.Entry<String, Object>> entries = jsonObject.entrySet();
        for (Map.Entry<String, Object> entry : entries) {
            String key = entry.getKey();
            String value = String.valueOf(entry.getValue());
            map.put(key, value);
        }
        return map;
    }

}