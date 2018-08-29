package org.springframework.social.vkontakte.config.boot;

import org.springframework.boot.context.properties.ConfigurationProperties;

@ConfigurationProperties("spring.social.vkontakte")
public class VKontakteProperties {
    private String appId;
    private String appSecret;
    private String serviceToken;

    public VKontakteProperties() {
    }

    public String getAppId() {
        return appId;
    }

    public VKontakteProperties setAppId(String appId) {
        this.appId = appId;
        return this;
    }

    public String getAppSecret() {
        return appSecret;
    }

    public VKontakteProperties setAppSecret(String appSecret) {
        this.appSecret = appSecret;
        return this;
    }

    public String getServiceToken() {
        return serviceToken;
    }

    public VKontakteProperties setServiceToken(String serviceToken) {
        this.serviceToken = serviceToken;
        return this;
    }
}
