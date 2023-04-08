package az.cmammad.lilo.controller;

import az.cmammad.common.constant.AuthorityName;
import az.cmammad.lilo.dto.SignUpRequestDto;
import az.cmammad.lilo.dto.SignUpResponseDto;
import az.cmammad.lilo.service.UserService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@Slf4j
@RestController
@RequiredArgsConstructor
@RequestMapping("/v1/users")
public class UserController {

    private final UserService userService;

    @PostMapping("/sign-up")
    public ResponseEntity<SignUpResponseDto> signUp(@RequestBody SignUpRequestDto signUp) {
        log.info("This User is Registered: {}", signUp);
        return new ResponseEntity<>(userService
                .registerUser(signUp, AuthorityName.USER), HttpStatus.CREATED);
    }
}
