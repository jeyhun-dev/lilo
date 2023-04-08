package az.cmammad.lilo.service.impl;

import az.cmammad.lilo.dto.SignUpRequestDto;
import az.cmammad.lilo.dto.SignUpResponseDto;
import az.cmammad.lilo.entity.Authority;
import az.cmammad.lilo.entity.UserEntity;
import az.cmammad.lilo.repository.AuthorityRepository;
import az.cmammad.lilo.repository.UserRepository;
import az.cmammad.lilo.service.UserService;
import lombok.RequiredArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.Set;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class UserServiceImpl implements UserService {

    private final UserRepository userRepository;
    private final AuthorityRepository authorityRepository;
    private final PasswordEncoder passwordEncoder;
    private final ModelMapper mapper;

    @Override
    public SignUpResponseDto registerUser(final SignUpRequestDto signUpRequestDto,
                                          final String authorityName) {
        final UserEntity user = createUser(signUpRequestDto, authorityName);
        return mapper.map(user, SignUpResponseDto.class);
    }

    private UserEntity createUser(final SignUpRequestDto requestDto,
                                  final String authorityName) {
        UserEntity userBuilder = UserEntity.builder()
                .username(requestDto.getUsername())
                .password(passwordEncoder.encode(requestDto.getPassword()))
                .fullName(requestDto.getFullName())
                .address(requestDto.getAddress())
                .phone(requestDto.getPhone())
                .birthday(requestDto.getBirthday())
//                .authorities()  //getAuthority method write inside
                .build();
        userRepository.save(userBuilder);
        return userBuilder;
    }

    //buna yeniden bax, stream yazilmalidi map ile
    private Set<Authority> getAuthority(final String authorityName) {
        return authorityRepository
                .findByName(authorityName)
                .stream()
//                .map()
                .collect(Collectors.toSet());
    }
}
