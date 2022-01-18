package com.maxzap.bserv.filter;

import com.maxzap.bserv.service.TokenService;
import lombok.NonNull;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpHeaders;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

@Slf4j
@Component
@RequiredArgsConstructor
public class AuthorizationFilter extends OncePerRequestFilter {

    private final TokenService tokenService;

    @Value("${auth.enabled}")
    private boolean enabled;

    @Override
    protected void doFilterInternal(@NonNull HttpServletRequest request,
                                    @NonNull HttpServletResponse response,
                                    @NonNull FilterChain filterChain) throws ServletException, IOException {
        if (!enabled)
            filterChain.doFilter(request, response);

        String authHeader = request.getHeader(HttpHeaders.AUTHORIZATION);

        if (authHeader == null || authHeader.isBlank())
            response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
        else if (!checkAuthorization(authHeader))
            response.setStatus(HttpServletResponse.SC_FORBIDDEN);
        else
            filterChain.doFilter(request, response);
    }

    private boolean checkAuthorization(String auth) {
        if (!auth.startsWith("Bearer "))
            return false;
        String token = auth.substring(7);
        return tokenService.checkToken(token);
    }
}
