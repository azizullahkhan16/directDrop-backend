package com.aktic.directdropbackend.controller.user;

import com.aktic.directdropbackend.model.request.UpdateUserRequest;
import com.aktic.directdropbackend.model.response.UserInfoResponse;
import com.aktic.directdropbackend.service.user.UserService;
import com.aktic.directdropbackend.util.ApiResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/user")
public class UserController {
    private final UserService userService;

    @PatchMapping("/update/{userId}")
    public ResponseEntity<ApiResponse<UserInfoResponse>>  updateUserInfoController(@PathVariable Long userId,
                                                                                   @RequestBody(required = false) UpdateUserRequest request) {
        return userService.updateUserInfoService(userId, request);
    }

    @GetMapping("/find-user/{userId}")
    public ResponseEntity<ApiResponse<UserInfoResponse[]>> findUser(@PathVariable Long userId,
                                                                    @RequestParam(value = "username", required = false) String username,
                                                                    @RequestParam(value = "ipAddress", required = false) String ipAddress) {
        return userService.findUser(userId, username, ipAddress);
    }
}
