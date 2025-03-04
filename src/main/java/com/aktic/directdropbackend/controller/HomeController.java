package com.aktic.directdropbackend.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
@RequestMapping("/")
public class HomeController {

    // Test route
    @RequestMapping("/")
    public ResponseEntity<String> test() {
        return ResponseEntity.ok("Welcome to DirectDrop");
    }
}
