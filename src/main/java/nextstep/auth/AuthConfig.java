package nextstep.auth;

import nextstep.auth.authentication.BasicAuthenticationConverter;
import nextstep.auth.authentication.BasicAuthenticationFilter2;
import nextstep.auth.authentication.BearerAuthenticationConverter;
import nextstep.auth.authentication.BearerTokenAuthenticationFilter2;
import nextstep.auth.authentication.FormAuthenticationConverter;
import nextstep.auth.authentication.TokenAuthenticationConverter;
import nextstep.auth.authentication.UsernamePasswordAuthenticationFilter;
import nextstep.auth.authorization.AuthenticationPrincipalArgumentResolver;
import nextstep.auth.context.SecurityContextPersistenceFilter;
import nextstep.auth.token.JwtTokenProvider;
import nextstep.auth.token.TokenAuthenticationInterceptor;
import nextstep.member.application.LoginMemberService;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

import java.util.List;

@Configuration
public class AuthConfig implements WebMvcConfigurer {
    private LoginMemberService loginMemberService;
    private JwtTokenProvider jwtTokenProvider;

    public AuthConfig(LoginMemberService loginMemberService, JwtTokenProvider jwtTokenProvider) {
        this.loginMemberService = loginMemberService;
        this.jwtTokenProvider = jwtTokenProvider;
    }

    @Override
    public void addInterceptors(InterceptorRegistry registry) {
        registry.addInterceptor(new SecurityContextPersistenceFilter());
        registry.addInterceptor(new UsernamePasswordAuthenticationFilter(loginMemberService,
                new FormAuthenticationConverter())).addPathPatterns("/login/form");
        registry.addInterceptor(new TokenAuthenticationInterceptor(loginMemberService, jwtTokenProvider,
                new TokenAuthenticationConverter())).addPathPatterns("/login/token");
//        registry.addInterceptor(new BasicAuthenticationFilter(loginMemberService));
        registry.addInterceptor(new BasicAuthenticationFilter2(loginMemberService, new BasicAuthenticationConverter()));
//        registry.addInterceptor(new BearerTokenAuthenticationFilter(jwtTokenProvider));
        registry.addInterceptor(new BearerTokenAuthenticationFilter2(jwtTokenProvider,
                new BearerAuthenticationConverter()));
    }

    @Override
    public void addArgumentResolvers(List argumentResolvers) {
        argumentResolvers.add(new AuthenticationPrincipalArgumentResolver());
    }
}
