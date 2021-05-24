package twins.controllers;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.*;
import twins.boundaries.NewUserDetails;
import twins.boundaries.UserBoundary;
import twins.boundaries.id.UserId;
import twins.logic.UsersService;


@RestController
public class UserController {
	private final UsersService userService;

	@Autowired
	public UserController(UsersService userService) {
		this.userService = userService;
	}

	@RequestMapping(
			path = "/twins/users",
			method = RequestMethod.POST,
			consumes = MediaType.APPLICATION_JSON_VALUE)
	public UserBoundary createNewUser (@RequestBody NewUserDetails input) {
		return userService.createUser(new UserBoundary(
				new UserId("",input.getEmail()),
				input.getRole(),
				input.getUsername(),
				input.getAvatar()));
	}

	@RequestMapping(
			path = "/twins/users/login/{userSpace}/{userEmail}",
			method = RequestMethod.GET,
			produces = MediaType.APPLICATION_JSON_VALUE)
	public UserBoundary userLogin(
			@PathVariable("userSpace") String userSpace,
			@PathVariable("userEmail") String userEmail) {
		return userService.login(userSpace, userEmail);
	}

	@RequestMapping(
			path = "/twins/users/{userSpace}/{userEmail}",
			method = RequestMethod.PUT,
			consumes = MediaType.APPLICATION_JSON_VALUE)
	public void updateUserDetails(
			@PathVariable("userSpace") String userSpace,
			@PathVariable("userEmail") String userEmail,
			@RequestBody UserBoundary update){
		userService.updateUser(userSpace, userEmail, update);
	}
}
