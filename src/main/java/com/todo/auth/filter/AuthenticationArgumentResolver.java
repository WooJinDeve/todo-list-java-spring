package com.todo.auth.filter;

import com.todo.auth.annotation.AuthenticationPrincipal;
import com.todo.auth.dto.LoginUser;
import com.todo.auth.service.JwtTokenProvider;
import com.todo.auth.support.AuthenticationExtractor;
import jakarta.servlet.http.HttpServletRequest;
import java.util.List;
import org.springframework.core.MethodParameter;
import org.springframework.stereotype.Component;
import org.springframework.web.bind.support.WebDataBinderFactory;
import org.springframework.web.context.request.NativeWebRequest;
import org.springframework.web.method.support.HandlerMethodArgumentResolver;
import org.springframework.web.method.support.ModelAndViewContainer;

@Component
public class AuthenticationArgumentResolver implements HandlerMethodArgumentResolver {
    private JwtTokenProvider jwtTokenProvider;

    @Override
    public boolean supportsParameter(MethodParameter parameter) {
        return parameter.hasParameterAnnotation(AuthenticationPrincipal.class);
    }

    @Override
    public Object resolveArgument(MethodParameter parameter, ModelAndViewContainer mavContainer,
                                  NativeWebRequest webRequest, WebDataBinderFactory binderFactory) throws Exception {
        final var request = (HttpServletRequest) webRequest.getNativeRequest();
        final var token = AuthenticationExtractor.extract(request);
        return createLoginUserFromToken(token);
    }

    private LoginUser createLoginUserFromToken(final String token){
        String payload = jwtTokenProvider.getPayloadFormToken(token);
        List<String> roles = jwtTokenProvider.getRolesFromToken(token);
        return new LoginUser(Long.parseLong(payload), roles);
    }
}
