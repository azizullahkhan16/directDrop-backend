package com.aktic.directdropbackend.controller.user;

import com.aktic.directdropbackend.model.request.UpdateUserRequest;
import com.aktic.directdropbackend.model.response.UserInfoResponse;
import com.aktic.directdropbackend.service.userService.UserService;
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
}
