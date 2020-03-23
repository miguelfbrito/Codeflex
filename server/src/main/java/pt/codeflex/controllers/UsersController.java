package pt.codeflex.controllers;

import java.util.List;
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
import org.springframework.web.bind.annotation.RequestParam;
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
@RequestMapping("/api/account")
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

    @GetMapping("/Users/isRegistered/{username}")
    public boolean isUserRegistered(@PathVariable String username) {
        Users users = usersRepository.findByUsername(username);
        if (users != null) {
            return true;
        }
        return false;
    }

    // ROLE

    @GetMapping(path = "/Role/view")
    public Iterable<Role> getAllRole() {
        return roleRepository.findAll();
    }

    @PostMapping(path = "/Role/add")
    public void addRole(@RequestBody Role role) {
        roleRepository.save(role);
    }

    @PostMapping(path = "/Role/edit")
    public void editRole(@RequestParam long id) {
        Optional<Role> r = roleRepository.findById(id);

        if (r.isPresent()) {
            Role role = r.get();
            roleRepository.save(role);
        }
    }

    @PostMapping(path = "/Role/delete/{id}")
    public void deleteRole(@PathVariable long id) {
        roleRepository.deleteById(id);
    }

    @GetMapping(path = "/Role/view/{id}")
    public Role viewRoleById(@PathVariable long id) {

        Optional<Role> r = roleRepository.findById(id);
        if (r.isPresent()) {
            return r.get();
        }

        return null;
    }

    // USERSROLES

    @GetMapping(path = "/UsersRoles/view")
    public List<UsersRoles> getAllUsersRoles() {
        return usersRolesRepository.findAll();
    }

    @PostMapping(path = "/UsersRoles/add")
    public UsersRoles addUsersRoles(@RequestBody UsersRoles usersRoles) {
        return usersRolesRepository.save(usersRoles);
    }

    @PostMapping(path = "/UsersRoles/edit")
    public void editUsersRoles(@RequestParam long id) {
        Optional<UsersRoles> u = usersRolesRepository.findById(id);

        if (u.isPresent()) {
            UsersRoles usersRoles = u.get();
            usersRolesRepository.save(usersRoles);
        }
    }

    @PostMapping(path = "/UsersRoles/delete/{id}")
    public void deleteUsersRoles(@PathVariable long id) {
        usersRolesRepository.deleteById(id);
    }

    @GetMapping(path = "/UsersRoles/view/{id}")
    public Optional<UsersRoles> viewUsersRolesById(@PathVariable long id) {
        return usersRolesRepository.findById(id);
    }

}
