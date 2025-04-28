package com.example.demoPaymentGetway;

import com.paypal.base.rest.APIContext;
import com.paypal.base.rest.OAuthTokenCredential;
import com.paypal.base.rest.PayPalRESTException;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.util.HashMap;
import java.util.Map;

@Configuration
public class PaypalConfig {

    @Value("${paypal.mode}")
    private String mode;

    @Value("${paypal.client.id}")
    private String clientId;

    @Value("${paypal.client.secret}")
    private String clientSecret;

    @Bean
    public Map<String, String> paypalsdkConfig(){
    Map<String,String> ConfigMap = new HashMap<>();
    ConfigMap.put("mode", mode);
      return ConfigMap;
    }

        @Bean
        public OAuthTokenCredential oAuthTokenCredential(){
        return  new OAuthTokenCredential(clientId,clientSecret,paypalsdkConfig());
        }

        @Bean
        public APIContext apiContext() throws PayPalRESTException {

            APIContext context = new APIContext(oAuthTokenCredential().getAccessToken());
        context.setConfigurationMap(paypalsdkConfig());
        return context;
        }

}
