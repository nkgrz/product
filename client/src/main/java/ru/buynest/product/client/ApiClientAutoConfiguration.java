package ru.buynest.product.client;

import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.boot.web.client.RestTemplateBuilder;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.util.DefaultUriBuilderFactory;

@Configuration
@EnableConfigurationProperties(ApiClientProperties.class)
public class ApiClientAutoConfiguration {

    @Bean
    @ConditionalOnMissingBean(RestTemplate.class)
    public RestTemplate restTemplate(ApiClientProperties properties) {
        return new RestTemplateBuilder()
                .uriTemplateHandler(new DefaultUriBuilderFactory(properties.getBaseUrl()))
                .build();
    }

    @Bean
    @ConditionalOnMissingBean(CategoryClient.class)
    public CategoryClient categoryClient(RestTemplate restTemplate) {
        return new CategoryClient(restTemplate);
    }
}