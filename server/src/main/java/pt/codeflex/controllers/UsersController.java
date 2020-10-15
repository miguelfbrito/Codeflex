package pt.codeflex.controllers;

import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.security.core.Authentication;

import pt.codeflex.models.Role;
import pt.codeflex.models.Users;
import pt.codeflex.models.UsersRoles;
import pt.codeflex.jsonresponses.GenericResponse;
import pt.codeflex.dto.UserLessInfo;
import pt.codeflex.repositories.RoleRepository;
import pt.codeflex.repositories.UsersRepository;
import pt.codeflex.repositories.UsersRolesRepository;
import pt.codeflex.services.CRUDService;

@RestController
@RequestMapping("/users")
public class UsersController extends RESTController<Users, Long> {

    private UsersRepository usersRepository;
    private RoleRepository roleRepository;
    private UsersRolesRepository usersRolesRepository;
    private BCryptPasswordEncoder bCryptPasswordEncoder;

    @Autowired
    public UsersController(CRUDService<Users, Long> service) {
        super(service);
    }

    @PostMapping("/login")
    public GenericResponse login(@RequestBody Users users) {
        Users currentUsers = usersRepository.findByUsername(users.getUsername());

        System.out.println("Logging in /api/account/login");
        System.out.println(users.getUsername());

        if (currentUsers != null) {
            if (bCryptPasswordEncoder.matches(users.getPassword(), currentUsers.getPassword())) {
                Authentication auth = SecurityContextHolder.getContext().getAuthentication();
                System.out.println(auth.getAuthorities());

                UserLessInfo finalUser = new UserLessInfo();
                finalUser.convert(currentUsers);

                return new GenericResponse(finalUser, "Logged in");
            }
        }

        return new GenericResponse(null, "Invalid username or password");
    }

    @PostMapping("/register")
    public ResponseEntity<?> register(@RequestBody Users users) {
        Users findByUsername = usersRepository.findByUsername(users.getUsername());
        Users findByEmail = usersRepository.findByEmail(users.getEmail());

        if (findByUsername != null) {
            System.out.println(findByUsername.toString());
            return new ResponseEntity<>(HttpStatus.CONFLICT);
        }

        if (findByEmail != null) {
            System.out.println(findByEmail.toString());
            return new ResponseEntity<>(HttpStatus.FORBIDDEN);
        }

        Users newUsers = new Users(users.getUsername(), users.getEmail(),
                bCryptPasswordEncoder.encode(users.getPassword()));

        Users savedUsers = usersRepository.save(newUsers);

        // Debug purposes
        if (savedUsers.getUsername().equals("admin")) {
            addUsersRoles(new UsersRoles(savedUsers, viewRoleById((long) 2)));
        } else {
            addUsersRoles(new UsersRoles(savedUsers, viewRoleById((long) 1)));
        }

        return new ResponseEntity<>(HttpStatus.OK);
    }

    private Role viewRoleById(long id) {
        Optional<Role> byId = roleRepository.findById(id);
        return byId.orElse(null);
    }


    // TODO: Refactor into context from token
    @GetMapping("/isregistered/{username}")
    public boolean isUserRegistered(@PathVariable String username) {
        Users users = usersRepository.findByUsername(username);
        if (users != null) {
            return true;
        }
        return false;
    }

    public UsersRoles addUsersRoles(@RequestBody UsersRoles usersRoles) {
        return usersRolesRepository.save(usersRoles);
    }

}
