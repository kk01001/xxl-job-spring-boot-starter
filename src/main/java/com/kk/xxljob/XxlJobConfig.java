package com.kk.xxljob;

import com.kk.xxljob.properties.XxlJobProperties;
import com.xxl.job.core.executor.impl.XxlJobSpringExecutor;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.cloud.commons.util.InetUtils;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;

/**
 * @author linshiqiang
 * @date 2022/2/14 10:34
 */
@Slf4j
@ComponentScan(basePackages = "com.kk.xxljob")
@Configuration
@ConditionalOnProperty(prefix = "xxljob", name = "admin-addresses")
@RequiredArgsConstructor
public class XxlJobConfig {

    private final XxlJobProperties xxlJobProperties;

    private final InetUtils inetUtils;

    @Bean
    public XxlJobSpringExecutor xxlJobExecutor() {
        String ipAddress = inetUtils.findFirstNonLoopbackHostInfo().getIpAddress();
        log.info(">>>>>>>>>>> xxl-job config init. {}", ipAddress);
        XxlJobSpringExecutor xxlJobSpringExecutor = new XxlJobSpringExecutor();
        xxlJobSpringExecutor.setAdminAddresses(xxlJobProperties.getAdminAddresses());
        xxlJobSpringExecutor.setAppname(xxlJobProperties.getAppName());
        xxlJobSpringExecutor.setIp(ipAddress);
        xxlJobSpringExecutor.setPort(xxlJobProperties.getPort());
        xxlJobSpringExecutor.setAccessToken(xxlJobProperties.getAccessToken());
        xxlJobSpringExecutor.setLogPath(xxlJobProperties.getLogPath());
        xxlJobSpringExecutor.setLogRetentionDays(xxlJobProperties.getLogRetentionDays());
        return xxlJobSpringExecutor;
    }

}
