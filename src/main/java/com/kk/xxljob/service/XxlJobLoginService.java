package com.kk.xxljob.service;

import cn.hutool.core.util.StrUtil;
import cn.hutool.http.HttpRequest;
import cn.hutool.http.HttpResponse;
import com.kk.xxljob.constants.XxlJobConstants;
import com.kk.xxljob.properties.XxlJobProperties;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.net.HttpCookie;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;

/**
 * @author linshiqiang
 * date 2023-01-23 15:26:00
 */
@Slf4j
@Service
@RequiredArgsConstructor
public class XxlJobLoginService {

    private final XxlJobProperties xxlJobProperties;

    private final Map<String, String> LOGIN_COOKIE = new HashMap<>();

    /**
     * 登录获取cookie
     */
    public void login() {

        if (StrUtil.isEmpty(xxlJobProperties.getUserName())) {
            throw new RuntimeException("xxl-job properties userName not set!");
        }
        if (StrUtil.isEmpty(xxlJobProperties.getPassword())) {
            throw new RuntimeException("xxl-job properties password not set!");
        }

        String loginUrl = xxlJobProperties.getAdminAddresses() + XxlJobConstants.LOGIN_URI;
        try (HttpResponse response = HttpRequest.post(loginUrl)
                .form("userName", xxlJobProperties.getUserName())
                .form("password", xxlJobProperties.getPassword())
                .execute()) {
            List<HttpCookie> cookies = response.getCookies();
            Optional<HttpCookie> cookieOpt = cookies.stream()
                    .filter(cookie -> XxlJobConstants.XXL_JOB_LOGIN_IDENTITY.equals(cookie.getName()))
                    .findFirst();
            if (!cookieOpt.isPresent()) {
                throw new RuntimeException("get xxl-job cookie error!");
            }

            LOGIN_COOKIE.put(XxlJobConstants.XXL_JOB_LOGIN_IDENTITY, cookieOpt.get().getValue());
        } catch (Exception e) {
            throw new RuntimeException("xxl-job login request error: " + loginUrl);
        }
    }

    /**
     * 从map中获取cookie
     */
    public String getCookie() {
        for (int i = 0; i < XxlJobConstants.RETRY_GET_COOKIE; i++) {
            String cookieStr = LOGIN_COOKIE.get(XxlJobConstants.XXL_JOB_LOGIN_IDENTITY);
            if (cookieStr != null) {
                return XxlJobConstants.XXL_JOB_LOGIN_IDENTITY + "=" + cookieStr;
            }
            login();
        }
        throw new RuntimeException("get xxl-job cookie error!");
    }

}
