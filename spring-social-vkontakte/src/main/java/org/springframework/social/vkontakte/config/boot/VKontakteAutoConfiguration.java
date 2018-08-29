package org.springframework.social.vkontakte.config.boot;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.AutoConfigureAfter;
import org.springframework.boot.autoconfigure.AutoConfigureBefore;
import org.springframework.boot.autoconfigure.condition.ConditionalOnClass;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.boot.autoconfigure.condition.ConditionalOnWebApplication;
import org.springframework.boot.autoconfigure.social.SocialWebAutoConfiguration;
import org.springframework.boot.autoconfigure.web.WebMvcAutoConfiguration;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Scope;
import org.springframework.context.annotation.ScopedProxyMode;
import org.springframework.core.env.Environment;
import org.springframework.social.config.annotation.ConnectionFactoryConfigurer;
import org.springframework.social.config.annotation.EnableSocial;
import org.springframework.social.config.annotation.SocialConfigurerAdapter;
import org.springframework.social.connect.Connection;
import org.springframework.social.connect.ConnectionRepository;
import org.springframework.social.connect.web.GenericConnectionStatusView;
import org.springframework.social.vkontakte.api.VKontakte;
import org.springframework.social.vkontakte.connect.VKontakteConnectionFactory;

@Configuration
@ConditionalOnClass({SocialConfigurerAdapter.class, VKontakteConnectionFactory.class})
@ConditionalOnProperty(
        prefix = "spring.social.vkontakte",
        name = {"app-id"}
)
@AutoConfigureBefore({SocialWebAutoConfiguration.class})
@AutoConfigureAfter({WebMvcAutoConfiguration.class})
public class VKontakteAutoConfiguration {


    @Configuration
    @EnableSocial
    @EnableConfigurationProperties({VKontakteProperties.class})
    @ConditionalOnWebApplication
    protected static class VKontakteConfigurerAdapter extends SocialConfigurerAdapter {
        @Autowired
        private VKontakteProperties properties;

        protected VKontakteConfigurerAdapter() {
        }

        @Bean
        @ConditionalOnMissingBean
        @Scope(
                value = "request",
                proxyMode = ScopedProxyMode.INTERFACES
        )
        public VKontakte vkontakte(ConnectionRepository repository) {
            Connection<VKontakte> connection = repository.findPrimaryConnection(VKontakte.class);
            return connection != null ? (VKontakte)connection.getApi() : null;
        }

        @Bean(
                name = {"connect/vkontakteConnect", "connect/vkontakteConnected"}
        )
        @ConditionalOnProperty(
                prefix = "spring.social",
                name = {"auto-connection-views"}
        )
        public GenericConnectionStatusView vkontakteConnectView() {
            return new GenericConnectionStatusView("vkontakte", "VKontakte");
        }

        public void addConnectionFactories(ConnectionFactoryConfigurer configurer, Environment environment) {
            VKontakteConnectionFactory factory = new VKontakteConnectionFactory(this.properties.getAppId(), this.properties.getAppSecret());
            factory.setScope("email profile");  // FIXME ?  video wall
            configurer.addConnectionFactory(factory);
        }
    }
}
