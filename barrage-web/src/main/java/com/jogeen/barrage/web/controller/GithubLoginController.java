package com.jogeen.barrage.web.controller;

import com.google.gson.Gson;
import com.jogeen.barrage.web.model.Auth;
import com.jogeen.barrage.web.model.User;
import com.jogeen.barrage.web.service.AuthService;
import com.jogeen.barrage.web.service.UserService;
import com.jogeen.barrage.web.util.HttpUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.servlet.http.HttpServletRequest;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

@Controller
@RequestMapping("/account/github")
public class GithubLoginController {

    @Autowired
    HttpServletRequest httpServletRequest;


    @Autowired
    UserService userService;

    @Autowired
    AuthService authService;


    @GetMapping(value = "/login")
    public String githubLogin() {
        String githubState = "adgasgdsdhgi";
        return "redirect:https://github.com/login/oauth/authorize?client_id=4ab8de7fdee52522d17d&redirect_uri=http://127.0.0.1:8080/account/github/callback&state=" + githubState;
    }

    @GetMapping(value = "/callback")
    public String githubCallback(String code, String state) {
        System.out.println("==>state:" + state);
        System.out.println("==>code:" + code);
        // Step2：通过 Authorization Code 获取 AccessToken
        String accessTokenResult = getAccessTokenResult(code);

        // Step2：通过 access_token 获取用户信息
        String githubInfoResult = HttpUtil.sendGet("https://api.github.com/user", "access_token=" + accessTokenResult);
        Map userMap = new Gson().fromJson(githubInfoResult, Map.class);
        String login = (String) userMap.get("login");
        String name = (String) userMap.get("name");
        Double id = (Double) userMap.get("id");
        String email = (String) userMap.get("email");

        Auth auth = authService.getByThridId(id.intValue());
        if (auth == null) {
            User user = new User();
            user.setUsername(name);
            user.setType("user");
            user.setPhone("");
            user.setPhone("");
            userService.register(user);
            Auth auth1 = new Auth();
            auth1.setThirdId(id.intValue());
            auth1.setUid(user.getId());
            auth1.setLogin(login);
            auth1.setEmail(email);
            auth1.setThirdType("github");
            auth1.setName(name);
            authService.insert(auth1);
            setUserToSession(user);
        } else {
            User user = userService.getUserById(auth.getUid());
            setUserToSession(user);
        }
        return "index.html";
    }


    private String getAccessTokenResult(String code) {
        String githubAccessTokenResult = HttpUtil.sendGet("https://github.com/login/oauth/access_token",
                "client_id=4ab8de7fdee52522d17d&client_secret=51a7d376a104ae00836f9f89c69029594c8db26c&code=" + code +
                        "&redirect_uri=http://127.0.0.1:8080/account/github/callback");
        System.out.println("==>githubAccessTokenResult: " + githubAccessTokenResult);
        /**
         * 以 & 为分割字符分割 result
         */
        String[] githubResultList = githubAccessTokenResult.split("&");
        List<String> params = new ArrayList<>();

        //此时 params.get(0) 为 access_token;  params.get(1) 为 token_type
        // paramMap 是分割后得到的参数对, 比说 access_token=ad5f4as6gfads4as98fg
        for (String paramMap : githubResultList) {
            if ("access_token".equals(paramMap.split("=")[0])) {
                // 再以 = 为分割字符分割, 并加到 params 中
                return paramMap.split("=")[1];
            }
        }
        return null;
    }

    private void setUserToSession(User user) {
        httpServletRequest.getSession().setAttribute("user", new Gson().toJson(user));
    }
}
