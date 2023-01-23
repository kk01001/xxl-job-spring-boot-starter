package com.kk.xxljob.service;

import cn.hutool.http.HttpRequest;
import cn.hutool.http.HttpResponse;
import cn.hutool.json.JSONArray;
import cn.hutool.json.JSONObject;
import cn.hutool.json.JSONUtil;
import com.kk.xxljob.constants.XxlJobConstants;
import com.kk.xxljob.model.XxlJobGroup;
import com.kk.xxljob.properties.XxlJobProperties;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

/**
 * @author linshiqiang
 * date 2023-01-23 15:42:00
 * 执行器管理
 */
@Slf4j
@Service
@RequiredArgsConstructor
public class XxlJobGroupService {

    private final XxlJobLoginService xxlJobLoginService;

    private final XxlJobProperties xxlJobProperties;

    /**
     * 获取所有执行器
     */
    public List<XxlJobGroup> getJobGroup() {
        String url = xxlJobProperties.getAdminAddresses() + XxlJobConstants.JOB_GROUP_PAGE_LIST;
        try (HttpResponse response = HttpRequest.post(url)
                .form("appname", xxlJobProperties.getAppName())
                .form("title", xxlJobProperties.getTitle())
                .cookie(xxlJobLoginService.getCookie())
                .execute()) {
            String body = response.body();
            JSONArray array = JSONUtil.parse(body).getByPath("data", JSONArray.class);
            return array.stream()
                    .map(o -> JSONUtil.toBean((JSONObject) o, XxlJobGroup.class))
                    .collect(Collectors.toList());
        } catch (Exception e) {
            throw new RuntimeException("xxl-job job group page list request error: " + e.getMessage());
        }
    }

    /**
     * 检查当前服务是否已经注册
     *
     * @return 是否注册
     */
    public boolean preciselyCheck() {
        List<XxlJobGroup> jobGroup = getJobGroup();
        Optional<XxlJobGroup> has = jobGroup.stream()
                .filter(xxlJobGroup -> xxlJobGroup.getAppname().equals(xxlJobProperties.getAppName())
                        && xxlJobGroup.getTitle().equals(xxlJobProperties.getTitle()))
                .findAny();
        return has.isPresent();
    }

    /**
     * 添加当前服务为执行器
     */
    public boolean autoRegisterGroup() {
        String url = xxlJobProperties.getAdminAddresses() + XxlJobConstants.JOB_GROUP_SAVE;
        try (HttpResponse response = HttpRequest.post(url)
                .form("appname", xxlJobProperties.getAppName())
                .form("title", xxlJobProperties.getTitle())
                .cookie(xxlJobLoginService.getCookie())
                .execute()) {
            Object code = JSONUtil.parse(response.body()).getByPath("code");
            return code.equals(200);
        } catch (Exception e) {
            throw new RuntimeException("xxl-job job group save request error: " + url);
        }
    }

}
