package com.yyq.mycommunity.Provider;


import com.alibaba.fastjson.JSON;
import com.yyq.mycommunity.dto.AccessTokenDTO;
import com.yyq.mycommunity.dto.GithubUser;
import okhttp3.*;
import org.springframework.stereotype.Component;

import java.io.IOException;

@Component
public class GiteeProvider {



    public String getAccessToken(AccessTokenDTO accessTokenDTO){

        MediaType mediaType = MediaType.get("application/json; charset=utf-8");

        OkHttpClient client = new OkHttpClient();

        RequestBody body = RequestBody.create(mediaType, JSON.toJSONString(accessTokenDTO));
        Request request = new Request.Builder()
                .url("https://gitee.com/oauth/token?grant_type=authorization_code&code="+accessTokenDTO.getCode()+"&client_id="+accessTokenDTO.getClient_id()+"&redirect_uri="+accessTokenDTO.getRedirect_uri()+"&client_secret="+accessTokenDTO.getClient_secret())
                .post(body)
                .build();
        try (Response response = client.newCall(request).execute()) {
//                获取 access_token=gho_16C7e42F292c6912E7710c838347Ae178B4a&scope=repo%2Cgist&token_type=bearer
            String string = response.body().string();

//                截取字符串token
            String token = string.split(",")[0].split(":")[1].replace('"',' ').trim();
//            System.out.println(token);
            return token;
        } catch (IOException e) {
            e.printStackTrace();
        }

        return null;
    }

    public GithubUser giteeUser(String accessToken){
        OkHttpClient client = new OkHttpClient();
//        String run(String url) throws IOException {
        Request request = new Request.Builder()
                .url("https://gitee.com/api/v5/user?access_token="+accessToken)
                .build();

        try (Response response = client.newCall(request).execute()) {
            String string = response.body().string();
            GithubUser githubUser = JSON.parseObject(string, GithubUser.class);

            return githubUser;
        } catch (IOException e) {
            e.printStackTrace();
        }
        return null;
    }
//    }


}
