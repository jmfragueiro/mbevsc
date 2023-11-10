package ar.com.mbe.core.extradata;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.context.annotation.RequestScope;

@Configuration
public class ReqScopeExtraDataService {
    @Bean
    @RequestScope
    public ReqScopeExtraData getRequestScopeED() {
        return new ReqScopeExtraData();
    }
}