package org.constantcontact.advertiser.config;

import org.constantcontact.advertiser.service.v2.ConstantContactService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.PropertySource;
import org.springframework.core.env.Environment;

@Configuration
@PropertySource("classpath:cc_additional.properties")
public class ConstantContactAccountAdditionalConfig {
    @Autowired
    Environment env;

    @Bean(name="additionalAccountService")
    public ConstantContactService additionalAccountService() {
        return new ConstantContactService(env);
    }
}
