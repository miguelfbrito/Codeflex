package pt.codeflex.controllers.api;

import java.nio.charset.StandardCharsets;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.Base64;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonProperty;

import pt.codeflex.jsonresponses.RegisterResponse;
import pt.codeflex.models.Users;
import pt.codeflex.repositories.UsersRepository;

@RestController
@CrossOrigin
@RequestMapping("/api")
public class UsersController {

	@Autowired
	private UsersRepository usersRepository;

	@PostMapping("/login")
	public Users login(@RequestBody Users user) {
		Users currentUser = usersRepository.findByUsername(user.getUsername());
		System.out.println(user.toString());

		MessageDigest digest = null;

		try {
			digest = MessageDigest.getInstance("SHA-256");
		} catch (NoSuchAlgorithmException e) {
			e.printStackTrace();
		}
		byte[] hash = digest.digest(user.getPassword().getBytes(StandardCharsets.UTF_8));
		String encoded = Base64.getEncoder().encodeToString(hash);

		if (currentUser.getPassword().equals(user.getPassword())) {
			// Workaround to send a response with less data, TODO : check Json views,
			// desiarizable
			Users newUser = new Users(currentUser.getUsername(), currentUser.getEmail(), currentUser.getPassword());
			newUser.setId(currentUser.getId());
			return newUser;
		}

		return new Users();
	}

	@PostMapping("/register")
	public RegisterResponse register(@RequestBody Users user) {
		RegisterResponse registerResponse = null;

		Users findByUsername = usersRepository.findByUsername(user.getUsername());
		Users findByEmail = usersRepository.findByEmail(user.getEmail());

		if (findByUsername != null) {
			registerResponse = new RegisterResponse( 0, "Username already in use");

			if (findByEmail != null) {
				registerResponse = new RegisterResponse(2, "Username and email in use");
			}
		} else {
			if (findByEmail != null) {
				registerResponse = new RegisterResponse(1, "Email in use");
			}
		}
		
		if(registerResponse == null) {
			Users newUser = new Users(user.getUsername(), user.getEmail(), user.getPassword());
			newUser = usersRepository.save(newUser);
			registerResponse = new RegisterResponse(3, "Account created");
		}
		
		return registerResponse;
	}
}
