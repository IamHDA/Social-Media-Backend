package com.example.backend.security;

import com.example.backend.entity.mySQL.User;
import com.example.backend.repository.mySQL.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

@Service
public class MyUserDetailService implements UserDetailsService {
    @Autowired
    private UserRepository userRepo;

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        User user = userRepo.findByEmail(username).orElse(null);

        if(user == null){
            System.out.println("Account not found");
            throw new UsernameNotFoundException("Account not found");
        }

        return new UserPrinciple(user);
    }
}
