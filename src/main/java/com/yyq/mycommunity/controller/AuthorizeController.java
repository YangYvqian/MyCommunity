package com.yyq.mycommunity.controller;


import com.yyq.mycommunity.Provider.GitHubProvider;
import com.yyq.mycommunity.Provider.GiteeProvider;
import com.yyq.mycommunity.dto.AccessTokenDTO;
import com.yyq.mycommunity.dto.GithubUser;
import com.yyq.mycommunity.mapper.UserMapper;
import com.yyq.mycommunity.model.User;
import com.yyq.mycommunity.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.UUID;

@Controller
public class AuthorizeController {

    private GitHubProvider gitHubProvider;
    @Autowired
    private GiteeProvider giteeProvider;
    private UserMapper userMapper;
    private UserService userService;

//  从配置文件中加载值-----方便统一管理
    @Value("${github.client.id}")
    private String clientId;
    //  从配置文件中加载值-----方便统一管理
    @Value("${github.client.secret}")
    private String ClientSecret;
    //  从配置文件中加载值-----方便统一管理
    @Value("${github.Redirect.uri}")
    private String RedirectUri;

    //  从配置文件中加载值-----方便统一管理
    @Value("${gitee.client.id}")
    private String giteeClientId;
    //  从配置文件中加载值-----方便统一管理
    @Value("${gitee.client.secret}")
    private String giteeClientSecret;
    //  从配置文件中加载值-----方便统一管理
    @Value("${gitee.Redirect.uri}")
    private String giteeRedirectUri;

    @Autowired
    public void setUserMapper(UserMapper userMapper) {
        this.userMapper = userMapper;
    }

    @Autowired
    public void setGitHubProvider(GitHubProvider gitHubProvider) {
        this.gitHubProvider = gitHubProvider;
    }
    @Autowired
    public void setUserService(UserService userService) {
        this.userService = userService;
    }



    @GetMapping("/callback")
    public String callback(@RequestParam(name = "code") String code,
                           @RequestParam(name = "state") String state,
//                           @RequestParam(name = "flag") Integer flag,
                           HttpServletRequest request,
                           HttpServletResponse response){

        AccessTokenDTO accessTokenDTO = new AccessTokenDTO();

            //        从配置文件中获取到的值进行绑定
            accessTokenDTO.setClient_id(giteeClientId);
            accessTokenDTO.setClient_secret(giteeClientSecret);
            accessTokenDTO.setRedirect_uri(giteeRedirectUri);
            accessTokenDTO.setCode(code);
            accessTokenDTO.setState(state);
//        获取accessToken的值
            String accessToken = giteeProvider.getAccessToken(accessTokenDTO);

//        通过accessToken的值获取用户信息，也就是拿到了token令牌来获取信息
            GithubUser githubUser = giteeProvider.giteeUser(accessToken);


            if (githubUser != null) {
//            获取第三方账户信息并插入数据库实现持久化
                User user = new User();
                String token = UUID.randomUUID().toString();
                user.setToken(token);
                user.setName(githubUser.getName());
                user.setAccountId(String.valueOf(githubUser.getId()));
                user.setAvatarUrl(githubUser.getAvatarUrl());

                userService.createOrUpdateUser(user);
                response.addCookie(new Cookie("token", token));
                return "redirect:/";
            }else {
                return "/";
            }


/*      **********************************************/
/*      *************Github登录方式********************/
/*      *************太慢了啊啊啊啊啊啊啊****************/
/*
        //        从配置文件中获取到的值进行绑定
            accessTokenDTO.setClient_id(clientId);
            accessTokenDTO.setClient_secret(ClientSecret);
            accessTokenDTO.setRedirect_uri(RedirectUri);
            accessTokenDTO.setCode(code);
            accessTokenDTO.setState(state);
//        获取accessToken的值
            String accessToken = gitHubProvider.getAccessToken(accessTokenDTO);
//        通过accessToken的值获取用户信息，也就是拿到了token令牌来获取信息
            GithubUser githubUser = gitHubProvider.githubUser(accessToken);
//        System.out.println(user.getName());

            if (githubUser != null) {
//            获取第三方账户信息并插入数据库实现持久化
                User user = new User();
                String token = UUID.randomUUID().toString();
                user.setToken(token);
                user.setName(githubUser.getName());
                user.setAccountId(String.valueOf(githubUser.getId()));
                user.setAvatarUrl(githubUser.getAvatarUrl());
                if (user.getName() == null){
                    return "redirect:/";
                }
                userService.createOrUpdateUser(user);
                response.addCookie(new Cookie("token", token));
                return "redirect:/";
            }else {
                return "/";
            }

*/

    }

    @GetMapping("/logout")
    public String logout(HttpServletRequest request,HttpServletResponse response){
        request.getSession().removeAttribute("user");
        Cookie cookie = new Cookie("token", null);
        cookie.setMaxAge(0);
        response.addCookie(cookie);
        return "redirect:/";
    }
}
