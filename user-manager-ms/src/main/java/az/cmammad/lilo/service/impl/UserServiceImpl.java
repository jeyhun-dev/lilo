package az.cmammad.lilo.service.impl;

import az.cmammad.lilo.dto.SignUpRequestDto;
import az.cmammad.lilo.dto.SignUpResponseDto;
import az.cmammad.lilo.repository.UserRepository;
import az.cmammad.lilo.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class UserServiceImpl implements UserService {

    private final UserRepository userRepository;

    @Override
    public SignUpResponseDto register(SignUpRequestDto signUpRequestDto) {
        return null;
    }
}
