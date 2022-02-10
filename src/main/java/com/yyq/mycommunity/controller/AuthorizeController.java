package com.yyq.mycommunity.controller;

import com.yyq.mycommunity.Provider.GitHubProvider;
import com.yyq.mycommunity.dto.AccessTokenDTO;
import com.yyq.mycommunity.dto.GithubUser;
import com.yyq.mycommunity.mapper.UserMapper;
import com.yyq.mycommunity.model.User;
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

    @Autowired
    private GitHubProvider gitHubProvider;

//  从配置文件中加载值-----方便统一管理
    @Value("${github.client.id}")
    private String clientId;
    //  从配置文件中加载值-----方便统一管理
    @Value("${github.client.secret}")
    private String ClientSecret;
    //  从配置文件中加载值-----方便统一管理
    @Value("${github.Redirect.uri}")
    private String RedirectUri;

    @Autowired
    private UserMapper userMapper;

    @GetMapping("/callback")
    public String callback(@RequestParam(name = "code") String code,
                           @RequestParam(name = "state") String state,
                           HttpServletRequest request,
                           HttpServletResponse response){

        AccessTokenDTO accessTokenDTO = new AccessTokenDTO();
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
            user.setGmtCreate(System.currentTimeMillis());
            user.setGmtModified(user.getGmtModified());
//            向数据库中插入信息，相当于写入session，
            userMapper.insert(user);

//          登录成功，把token写入cookie中，cookie在response中
            response.addCookie(new Cookie("token", token));

//          把信息写到session中
//            request.getSession().setAttribute("user", githubUser);
            return "redirect:/";
        }else {
            return "/";
        }



    }
}
