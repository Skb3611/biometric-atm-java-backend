package com.biometricatm.middleware;

import com.biometricatm.entity.User;
import com.biometricatm.repository.UserRepository;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.web.servlet.HandlerInterceptor;

@Component
public class AuthInterceptor implements HandlerInterceptor {

    @Autowired
    private UserRepository userRepository;

    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {
        // Allow OPTIONS requests for CORS preflight
        if ("OPTIONS".equals(request.getMethod())) {
            response.setStatus(HttpServletResponse.SC_OK);
            return true;
        }

        String fingerprintId = request.getHeader("fingerprintId");
        
        if (fingerprintId == null || fingerprintId.trim().isEmpty()) {
            response.setStatus(HttpServletResponse.SC_BAD_REQUEST);
            response.setContentType("application/json");
            response.getWriter().write("{\"error\": \"Fingerprint ID is required\"}");
            return false;
        }

        User user = userRepository.findByFingerprintId(fingerprintId).orElse(null);
        if (user == null) {
            response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
            response.setContentType("application/json");
            response.getWriter().write("{\"error\": \"Unauthorized\"}");
            return false;
        }

        // Attach user to request attribute
        request.setAttribute("user", user);
        return true;
    }
}
