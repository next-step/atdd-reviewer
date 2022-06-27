package nextstep.auth;

import nextstep.auth.ui.authentication.TokenAuthenticationInterceptor;
import nextstep.auth.ui.authentication.UsernamePasswordAuthenticationInterceptor;
import nextstep.auth.ui.convert.SessionAuthenticationConverter;
import nextstep.auth.ui.convert.TokenAuthenticationConverter;
import nextstep.auth.ui.securityAuthentication.BasicAuthenticationInterceptor;
import nextstep.auth.ui.securityAuthentication.BearerTokenAuthenticationInterceptor;
import nextstep.auth.authorization.AuthenticationPrincipalArgumentResolver;
import nextstep.auth.context.SecurityContextPersistenceFilter;
import nextstep.auth.token.JwtTokenProvider;
import nextstep.member.application.LoginMemberService;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

import java.util.List;

@Configuration
public class AuthConfig implements WebMvcConfigurer {
    private final LoginMemberService loginMemberService;
    private final JwtTokenProvider jwtTokenProvider;

    private final SessionAuthenticationConverter sessionAuthenticationConverter;

    private final TokenAuthenticationConverter tokenAuthenticationConverter;

    public AuthConfig(LoginMemberService loginMemberService, JwtTokenProvider jwtTokenProvider, SessionAuthenticationConverter sessionAuthenticationConverter, TokenAuthenticationConverter tokenAuthenticationConverter) {
        this.loginMemberService = loginMemberService;
        this.jwtTokenProvider = jwtTokenProvider;
        this.sessionAuthenticationConverter = sessionAuthenticationConverter;
        this.tokenAuthenticationConverter = tokenAuthenticationConverter;
    }

    @Override
    public void addInterceptors(InterceptorRegistry registry) {
        registry.addInterceptor(new SecurityContextPersistenceFilter());
        registry.addInterceptor(new UsernamePasswordAuthenticationInterceptor(loginMemberService, sessionAuthenticationConverter)).addPathPatterns("/login/form");
        registry.addInterceptor(new TokenAuthenticationInterceptor(loginMemberService, tokenAuthenticationConverter, jwtTokenProvider)).addPathPatterns("/login/token");
        registry.addInterceptor(new BasicAuthenticationInterceptor(loginMemberService));
        registry.addInterceptor(new BearerTokenAuthenticationInterceptor(jwtTokenProvider));
    }

    @Override
    public void addArgumentResolvers(List argumentResolvers) {
        argumentResolvers.add(new AuthenticationPrincipalArgumentResolver());
    }
}
