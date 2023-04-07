package az.cmammad.lilo.security;

import az.cmammad.common.UserDto;
import az.cmammad.lilo.entity.UserEntity;
import az.cmammad.lilo.exception.UserNotFoundException;
import az.cmammad.lilo.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class CustomUserDetailsService implements UserDetailsService {

    private final UserRepository userRepository;

    @Override
    public UserDto loadUserByUsername(final String username) throws UsernameNotFoundException {
        return convertUser(userRepository
                .findByUsername(username)
                .orElseThrow(UserNotFoundException::new));
    }

    private UserDto convertUser(UserEntity userEntity) {
        return new UserDto(
                userEntity.getUsername(),
                userEntity.getPassword(),
                userEntity.getFullName(),
                userEntity.getAddress(),
                userEntity.getBirthday(),
                userEntity.getPhone(),
                List.of()
        );
    }
}
