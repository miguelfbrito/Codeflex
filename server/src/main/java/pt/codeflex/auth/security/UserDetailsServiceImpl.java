package pt.codeflex.auth.security;

import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import pt.codeflex.models.Users;
import pt.codeflex.repositories.UsersRepository;

import static java.util.Collections.emptyList;

@Service
public class UserDetailsServiceImpl implements UserDetailsService {
    private UsersRepository applicationUserRepository;
    private String userId;
    
    public UserDetailsServiceImpl(UsersRepository applicationUserRepository) {
        this.applicationUserRepository = applicationUserRepository;
    }
    
    

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        Users applicationUsers = applicationUserRepository.findByUsername(username);
        if (applicationUsers == null) {
            throw new UsernameNotFoundException(username);
        }
        return new org.springframework.security.core.userdetails.User(applicationUsers.getUsername(), applicationUsers.getPassword(), emptyList());
    }
}