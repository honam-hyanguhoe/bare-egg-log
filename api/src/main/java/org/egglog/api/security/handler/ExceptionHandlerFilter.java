package org.egglog.api.security.handler;

import com.fasterxml.jackson.databind.ObjectMapper;
import io.jsonwebtoken.ExpiredJwtException;
import io.jsonwebtoken.MalformedJwtException;
import io.jsonwebtoken.UnsupportedJwtException;
import jakarta.servlet.FilterChain;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.extern.slf4j.Slf4j;
import org.egglog.api.security.exception.AuthException;
import org.egglog.api.security.exception.JwtException;
import org.egglog.utility.utils.MessageUtils;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;
import java.util.Arrays;

import static org.egglog.api.security.exception.JwtErrorCode.*;

@Slf4j
@Configuration
public class ExceptionHandlerFilter extends OncePerRequestFilter {
    @Override
    protected void doFilterInternal(
            HttpServletRequest request,
            HttpServletResponse response,
            FilterChain filterChain
    ) {
        try{
            filterChain.doFilter(request, response);
        }catch (JwtException e){
            log.debug("============================== [ ** 예외 처리 ** ] =============================");
            log.debug("[EXCEPTION] : {}", e.getMessage(), e);
            log.debug("============================== [ Stack trace END ] =============================");
            setErrorResponse(response, e.getErrorCode().getHttpStatus(), e.getMessage());
        } catch (AuthException e){
            log.debug("============================== [ ** 예외 처리 ** ] =============================");
            log.debug("[EXCEPTION] : {}", e.getMessage(), e);
            log.debug("============================== [ Stack trace END ] =============================");
            setErrorResponse(response, e.getErrorCode().getHttpStatus(), e.getMessage());
        }
        catch (Exception e){
            log.debug("============================== [ ** 예상하지 못한 예외 처리 ** ] =============================");
            log.debug("[EXCEPTION] : {}", e.getMessage(), e);
            log.debug("============================== [ Stack trace END ] =============================");
            setErrorResponse(response, HttpStatus.INTERNAL_SERVER_ERROR, e.getMessage());
        }
    }
    private void setErrorResponse(
            HttpServletResponse response,
            HttpStatus status,
            String errorMassage
    ){
        ObjectMapper objectMapper = new ObjectMapper();
        response.setStatus(status.value());
        response.setContentType(MediaType.APPLICATION_JSON_UTF8_VALUE);
        try{
            String jsonResponse = objectMapper.writeValueAsString(MessageUtils.fail(status.name(), errorMassage));
            response.getWriter().write(jsonResponse);
        }catch (IOException e){
            log.error(e.toString());
        }
    }

}

