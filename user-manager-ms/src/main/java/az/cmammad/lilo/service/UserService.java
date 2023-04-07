package az.cmammad.lilo.service;

import az.cmammad.lilo.dto.SignUpRequestDto;
import az.cmammad.lilo.dto.SignUpResponseDto;

public interface UserService {

    SignUpResponseDto register(SignUpRequestDto signUpRequestDto);
}
