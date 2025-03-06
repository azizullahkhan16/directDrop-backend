package com.aktic.directdropbackend.service.discovery;

import com.aktic.directdropbackend.util.ApiResponse;
import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.net.InetAddress;
import java.util.ArrayList;
import java.util.List;

@Service
@Slf4j
@RequiredArgsConstructor
public class DiscoveryService {

    private String extractClientIP(HttpServletRequest request) {
        String ip = request.getHeader("X-Forwarded-For");
        if (ip == null || ip.isEmpty() || "unknown".equalsIgnoreCase(ip)) {
            ip = request.getHeader("Proxy-Client-IP");
        }
        if (ip == null || ip.isEmpty() || "unknown".equalsIgnoreCase(ip)) {
            ip = request.getHeader("WL-Proxy-Client-IP");
        }
        if (ip == null || ip.isEmpty() || "unknown".equalsIgnoreCase(ip)) {
            ip = request.getRemoteAddr();
        }

        // Handle multiple IPs in X-Forwarded-For
        if (ip.contains(",")) {
            ip = ip.split(",")[0].trim();
        }

        return ip;
    }

    public ResponseEntity<ApiResponse<String>> getMyIP(HttpServletRequest request) {
        try{
            String clientIp = extractClientIP(request);
            return ResponseEntity.ok(new ApiResponse<>(true, "Your IP address fetched successfully", clientIp));
        }catch (Exception e) {
            log.error("Unexpected error occurred while getting plans", e);
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(new ApiResponse<>(false, "Internal server error", null));
        }
    }
}
