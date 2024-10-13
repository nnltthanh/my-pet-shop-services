package ct553.backend.auth;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod; 
import org.springframework.security.config.annotation.method.configuration.EnableMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.config.core.GrantedAuthorityDefaults;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.util.matcher.AntPathRequestMatcher;

import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
@Configuration
@EnableWebSecurity
@EnableMethodSecurity(prePostEnabled = true, securedEnabled = true, jsr250Enabled = true)
public class SecurityConfig {

    private final JwtConverter jwtConverter;
    
    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {

        http.csrf(AbstractHttpConfigurer::disable).authorizeHttpRequests((authz) -> {
            // authz.requestMatchers(AntPathRequestMatcher.antMatcher(HttpMethod.POST, "/api/users")).permitAll()
            //     .requestMatchers(AntPathRequestMatcher.antMatcher(HttpMethod.POST, "/api/customers")).permitAll()
            //     .requestMatchers(AntPathRequestMatcher.antMatcher(HttpMethod.POST, "/api/import")).permitAll()
            //         .anyRequest().authenticated();
            authz.anyRequest().permitAll();
        });

        http.sessionManagement(sess -> sess.sessionCreationPolicy(
                SessionCreationPolicy.STATELESS));
        http.oauth2ResourceServer(oauth2 -> oauth2.jwt(jwt -> jwt.jwtAuthenticationConverter(jwtConverter)));

        return http.build();
    }
    
    @Bean
    public GrantedAuthorityDefaults grantedAuthorityDefaults() {
        return  new GrantedAuthorityDefaults("");
    }

}