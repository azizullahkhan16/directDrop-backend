package com.aktic.directdropbackend.controller.discovery;

import com.aktic.directdropbackend.service.discovery.DiscoveryService;
import com.aktic.directdropbackend.util.ApiResponse;
import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/api/network")
@RequiredArgsConstructor
public class DiscoveryController {

    private final DiscoveryService discoveryService;

    @GetMapping("/my-ip")
    public ResponseEntity<ApiResponse<String>> getMyIP(HttpServletRequest request) {
        return discoveryService.getMyIP(request);
    }

//    @GetMapping("/active-ips")
//    public ResponseEntity<ApiResponse<List<String>>> getActiveIPs(HttpServletRequest request) {
//        return discoveryService.getActiveIPs(request);
//    }

}
