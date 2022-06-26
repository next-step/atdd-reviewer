package nextstep.auth.authentication;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import org.springframework.web.servlet.HandlerInterceptor;

public abstract class NonChainingAuthenticationInterceptor implements HandlerInterceptor {

	@Override
	public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) {
		try {
			authenticate(request, response, handler);
		} catch (Exception e) {
			return true;
		}

		return true;
	}

	protected abstract void authenticate(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception;
}
