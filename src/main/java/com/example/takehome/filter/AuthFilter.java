package com.example.takehome.filter;

import com.example.takehome.dto.User;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.core.annotation.Order;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.concurrent.atomic.AtomicLong;

import javax.servlet.Filter;
import javax.servlet.FilterChain;
import javax.servlet.FilterConfig;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

@Component
@Order(1)
public class AuthFilter implements Filter {

    private static final Logger LOGGER = LoggerFactory.getLogger(AuthFilter.class);

    private final ScheduledExecutorService scheduler = Executors.newScheduledThreadPool(1);

    private Map<String, User> users = new ConcurrentHashMap<>();

    @Override
    public void init(FilterConfig filterConfig) {
        LOGGER.trace("Initializing filter...");
        scheduler.schedule(()-> users.forEach((key, value) -> {
                               if (System.currentTimeMillis() - value.getLastRequestTime().get() > 1000) {
                                   users.remove(key);
                               }
                           }),
                           1, TimeUnit.MINUTES);
    }

    @Override
    public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain) throws IOException, ServletException {
        HttpServletResponse httpServletResponse = (HttpServletResponse) response;
        HttpServletRequest httpServletRequest =  (HttpServletRequest) request;

        if(!users.containsKey(httpServletRequest.getSession().getId())) {
            users.put(httpServletRequest.getSession().getId(), createUser());
            httpServletResponse.sendRedirect(
                    httpServletRequest.getRequestURL()
                                      .append("?")
                                      .append(httpServletRequest.getQueryString()).toString());
            return;
        } else {
            User user = users.get(httpServletRequest.getSession().getId());
            synchronized (user) {
                if((System.currentTimeMillis() - user.getLastRequestTime().get()) < 1000) {
                    int requestAttempt = user.getRequestCount().incrementAndGet();
                    int maxAttemptsPerSecond = "Authorized".equals(httpServletRequest.getHeader("AuthToken")) ? 20 : 5;
                    if(requestAttempt > maxAttemptsPerSecond) {
                        httpServletResponse.sendError(403, String.format("No more than %d requests attempt per second for user", maxAttemptsPerSecond));
                        return;
                    }
                } else {
                    user.getRequestCount().set(0);
                    user.setLastRequestTime(new AtomicLong(System.currentTimeMillis()));
                }
            }
        }
        chain.doFilter(request, response);
    }

    private User createUser() {
        var user = new User();
        user.setRequestCount(new AtomicInteger(0));
        user.setLastRequestTime(new AtomicLong(System.currentTimeMillis()));
        return user;
    }

    @Override
    public void destroy() {
        // clean up any resource being held by the filter here
        LOGGER.trace("Destroying filter...");
    }

}
