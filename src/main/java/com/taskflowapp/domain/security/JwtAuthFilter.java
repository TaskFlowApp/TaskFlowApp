package com.taskflowapp.domain.security;

import io.jsonwebtoken.Claims;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.util.StringUtils;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;

@Slf4j
@RequiredArgsConstructor
public class JwtAuthFilter extends OncePerRequestFilter {

    private final JwtProvider jwtProvider;
    private final UserDetailsServiceImpl userDetailsService;

    @Override
    protected void doFilterInternal(
            HttpServletRequest request,
            HttpServletResponse response,
            FilterChain filterChain)
            throws ServletException, IOException {

        if (shouldSkipAuthentication(request)) {
            filterChain.doFilter(request, response);
            return;
        }

        String accessToken = jwtProvider.getJwtFromHeader(request);
        if (StringUtils.hasText(accessToken)) {
            if (!jwtProvider.validateToken(accessToken)) {
                log.error("AccessToken Error");
                return;
            }

            Claims info = jwtProvider.getUserInfoFromToken(accessToken);

            try {
                setAuthentication(info.getSubject());
            } catch (Exception e) {
                log.error(e.getMessage());
                return;
            }
        }

        filterChain.doFilter(request, response);
    }

    public void setAuthentication(String username) {
        SecurityContext context = SecurityContextHolder.createEmptyContext();
        Authentication authentication = createAuthentication(username);
        context.setAuthentication(authentication);

        SecurityContextHolder.setContext(context);
    }

    private Authentication createAuthentication(String username) {
        UserDetails userDetails = userDetailsService.loadUserByUsername(username);
        return new UsernamePasswordAuthenticationToken(
                userDetails,
                null,
                userDetails.getAuthorities());
    }

    private boolean shouldSkipAuthentication(HttpServletRequest request) {
        String path = request.getRequestURI();

        String[] publicPaths = {
                "/api/auth/register",
                "/api/auth/login"
        };

        for (String publicPath : publicPaths) {
            if (path.startsWith(publicPath)) {
                log.debug("Skipping JWT authentication for public path: {}", path);
                return true;
            }
        }

        return false;
    }
}
