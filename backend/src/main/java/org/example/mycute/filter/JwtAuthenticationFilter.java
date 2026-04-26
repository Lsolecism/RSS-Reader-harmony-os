package org.example.mycute.filter;

import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.example.mycute.utils.JwtTokenProvider;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;

@Slf4j
@Component
@RequiredArgsConstructor
public class JwtAuthenticationFilter extends OncePerRequestFilter {

    private final JwtTokenProvider jwtTokenProvider;
    private final UserDetailsService userDetailsService;

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain)
            throws ServletException, IOException {
        try {
            log.info("Processing request for URL: {}", request.getRequestURL());
            log.info("Request method: {}", request.getMethod());
            log.info("All headers:");
            java.util.Enumeration<String> headerNames = request.getHeaderNames();
            while (headerNames.hasMoreElements()) {
                String headerName = headerNames.nextElement();
                log.info("{}: {}", headerName, request.getHeader(headerName));
            }
            
            String jwt = getJwtFromRequest(request);
            log.info("Received JWT token: {}", jwt);

            if (StringUtils.hasText(jwt)) {
                if (jwtTokenProvider.validateToken(jwt)) {
                    Integer userId = jwtTokenProvider.getUserIdFromToken(jwt);
                    log.debug("Valid JWT token for userId: {}", userId);
                    
                    UserDetails userDetails = userDetailsService.loadUserByUsername(String.valueOf(userId));
                    
                    UsernamePasswordAuthenticationToken authentication = new UsernamePasswordAuthenticationToken(
                            userDetails, null, userDetails.getAuthorities());
                    authentication.setDetails(new WebAuthenticationDetailsSource().buildDetails(request));

                    SecurityContextHolder.getContext().setAuthentication(authentication);
                    log.debug("Successfully set authentication in SecurityContext");
                } else {
                    log.warn("Invalid JWT token");
                }
            } else {
                log.debug("No JWT token found in request");
            }
        } catch (Exception ex) {
            log.error("Could not set user authentication in security context", ex);
        }

        filterChain.doFilter(request, response);
    }

    @Nullable
    private String getJwtFromRequest(@NotNull HttpServletRequest request) {
        String bearerToken = request.getHeader("Authorization");
        log.info("Raw Authorization header: {}", bearerToken);
        
        if (StringUtils.hasText(bearerToken) && bearerToken.startsWith("Bearer ")) {
            String token = bearerToken.substring(7);
            log.info("Extracted token: {}", token);
            return token;
        }
        log.warn("Authorization header is either missing, empty, or doesn't start with 'Bearer '");
        return null;
    }
} 