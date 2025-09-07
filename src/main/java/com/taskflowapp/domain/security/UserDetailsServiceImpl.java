package com.taskflowapp.domain.security;

import com.taskflowapp.domain.user.entity.User;
import com.taskflowapp.domain.user.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class UserDetailsServiceImpl implements UserDetailsService {

    private final UserRepository userRepository;

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        User user = userRepository.findByUsernameAndDeletedFalse(username).orElseThrow(
                        () -> new UsernameNotFoundException("Not Found" + username)
        );

        return new UserDetailsImpl(user);
    }
}
