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

import pt.codeflex.databasemodels.Users;
import pt.codeflex.jsonresponses.GenericResponse;
import pt.codeflex.jsonresponses.GenericResponse;
import pt.codeflex.models.UserLessInfo;
import pt.codeflex.repositories.UsersRepository;

@RestController
@CrossOrigin
@RequestMapping("/api")
public class UsersController {

	@Autowired
	private UsersRepository usersRepository;

	@PostMapping("/login")
	public GenericResponse login(@RequestBody Users user) {
		Users currentUser = usersRepository.findByUsername(user.getUsername());

		if (currentUser != null) {
			MessageDigest digest = null;

			try {
				digest = MessageDigest.getInstance("SHA-256");
			} catch (NoSuchAlgorithmException e) {
				e.printStackTrace();
			}
			byte[] hash = digest.digest(user.getPassword().getBytes(StandardCharsets.UTF_8));
			String encoded = Base64.getEncoder().encodeToString(hash);

			if (currentUser.getPassword().equals(encoded)) {

				UserLessInfo finalUser = new UserLessInfo();
				finalUser.convert(currentUser);
				
				return new GenericResponse(finalUser, "Logged in");
			}
		} 
		
		return new GenericResponse(null, "Invalid username or password");
	}

	@PostMapping("/register")
	public GenericResponse register(@RequestBody Users user) {
		GenericResponse genericResponse = null;

		Users findByUsername = usersRepository.findByUsername(user.getUsername());
		Users findByEmail = usersRepository.findByEmail(user.getEmail());

		if (findByUsername != null) {
			genericResponse = new GenericResponse(0, "Username already in use");

			if (findByEmail != null) {
				genericResponse = new GenericResponse(2, "Username and email in use");
			}
		} else {
			if (findByEmail != null) {
				genericResponse = new GenericResponse(1, "Email in use");
			}
		}

		if (genericResponse == null) {
			Users newUser = new Users(user.getUsername(), user.getEmail(), user.getPassword());
			usersRepository.save(newUser);
			
			UserLessInfo finalUser = new UserLessInfo();
			finalUser.convert(newUser);
			
			genericResponse = new GenericResponse(3, finalUser, "Account created");
		}

		return genericResponse;
	}
}
