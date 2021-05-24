package twins.controllers;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import twins.boundaries.OperationBoundary;
import twins.boundaries.UserBoundary;
import org.springframework.http.MediaType;
import twins.logic.*;

import java.util.List;

@RestController
public class AdminController {
	private final UsersServiceExtended userService;
	private final ItemsServiceExtended itemService;
	private final OperationsServiceExtended operationService;

	@Autowired
	public AdminController(UsersServiceExtended userService,
						   ItemsServiceExtended itemService,
						   OperationsServiceExtended operationService) {
		this.userService = userService;
		this.itemService = itemService;
		this.operationService = operationService;
	}

	@RequestMapping(
			path = "/twins/admin/users/{userSpace}/{userEmail}",
			method = RequestMethod.DELETE)
	public void deleteAllUsers(
			@PathVariable("userSpace") String userSpace,
			@PathVariable("userEmail") String userEmail) throws Exception {
		userService.deleteAllUsers(userSpace,userEmail);
	}

	@RequestMapping(
			path = "/twins/admin/items/{userSpace}/{userEmail}",
			method = RequestMethod.DELETE)
	public void deleteAllItems(
			@PathVariable("userSpace") String userSpace,
			@PathVariable("userEmail") String userEmail) throws Exception {
		itemService.deleteAllItems(userSpace,userEmail);
	}

	@RequestMapping(
			path = "/twins/admin/operations/{userSpace}/{userEmail}",
			method = RequestMethod.DELETE)
	public void deleteAllOperations(
			@PathVariable("userSpace") String userSpace,
			@PathVariable("userEmail") String userEmail) throws Exception {
		operationService.deleteAllOperations(userSpace,userEmail);
	}
	
	@RequestMapping(
			path="/twins/admin/users/{userSpace}/{userEmail}",
			method = RequestMethod.GET,
			produces = MediaType.APPLICATION_JSON_VALUE)
	public List<UserBoundary> ExportAllUsers(
			@PathVariable("userSpace") String userSpace,
			@PathVariable("userEmail") String userEmail,
			@RequestParam(name="size", required = false, defaultValue = "5") int size,
			@RequestParam(name="page", required = false, defaultValue = "0") int page) throws Exception {
		return userService.getAllUsers(userSpace,userEmail,page,size);

	}
	
	@RequestMapping(
			path="/twins/admin/operations/{userSpace}/{userEmail}",
			method = RequestMethod.GET,
			produces = MediaType.APPLICATION_JSON_VALUE)
	public List<OperationBoundary> ExportAllOperations(
			@PathVariable("userSpace") String userSpace,
			@PathVariable("userEmail") String userEmail,
			@RequestParam(name="size", required = false, defaultValue = "5") int size,
			@RequestParam(name="page", required = false, defaultValue = "0") int page) throws Exception {
		return operationService.getAllOperations(userSpace,userEmail,page,size);
	}
}
