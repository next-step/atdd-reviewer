package nextstep.auth;

import nextstep.auth.application.AuthenticationUserService;
import nextstep.auth.authentication.BasicAuthenticationConverter;
import nextstep.auth.authentication.BasicAuthenticationFilter;
import nextstep.auth.authentication.BearerAuthenticationConverter;
import nextstep.auth.authentication.BearerTokenAuthenticationFilter;
import nextstep.auth.authentication.FormAuthenticationConverter;
import nextstep.auth.authentication.TokenAuthenticationConverter;
import nextstep.auth.authentication.UsernamePasswordAuthenticationFilter;
import nextstep.auth.authorization.AuthenticationPrincipalArgumentResolver;
import nextstep.auth.context.SecurityContextPersistenceFilter;
import nextstep.auth.token.JwtTokenProvider;
import nextstep.auth.token.TokenAuthenticationInterceptor;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

import java.util.List;

@Configuration
public class AuthConfig implements WebMvcConfigurer {

    private final AuthenticationUserService authenticationUserService;
    private final JwtTokenProvider jwtTokenProvider;

    public AuthConfig(AuthenticationUserService authenticationUserService, JwtTokenProvider jwtTokenProvider) {
        this.authenticationUserService = authenticationUserService;
        this.jwtTokenProvider = jwtTokenProvider;
    }

    @Override
    public void addInterceptors(InterceptorRegistry registry) {
        registry.addInterceptor(new SecurityContextPersistenceFilter());
        registry.addInterceptor(new UsernamePasswordAuthenticationFilter(authenticationUserService,
                new FormAuthenticationConverter())).addPathPatterns("/login/form");
        registry.addInterceptor(new TokenAuthenticationInterceptor(authenticationUserService, jwtTokenProvider,
                new TokenAuthenticationConverter())).addPathPatterns("/login/token");
        registry.addInterceptor(new BasicAuthenticationFilter(authenticationUserService, new BasicAuthenticationConverter()));
        registry.addInterceptor(new BearerTokenAuthenticationFilter(jwtTokenProvider,
                new BearerAuthenticationConverter()));
    }

    @Override
    public void addArgumentResolvers(List argumentResolvers) {
        argumentResolvers.add(new AuthenticationPrincipalArgumentResolver());
    }
}
