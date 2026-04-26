package org.example.mycute.security;

import lombok.RequiredArgsConstructor;
import org.example.mycute.domain.entity.User;
import org.example.mycute.mapper.UserMapper;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class CustomUserDetailsService implements UserDetailsService {

    private final UserMapper userMapper;

    @Override
    public UserDetails loadUserByUsername(String userId) throws UsernameNotFoundException {
        User user = userMapper.selectById(Integer.valueOf(userId));
        if (user == null) {
            throw new UsernameNotFoundException("User not found with userId: " + userId);
        }
        return new CustomUserDetails(user);
    }
} 