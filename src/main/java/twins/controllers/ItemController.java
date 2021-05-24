package twins.controllers;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import twins.boundaries.ItemBoundary;
import org.springframework.http.MediaType;
import twins.logic.ItemsServiceExtended;

import java.util.List;

@RestController
public class ItemController {
	private final ItemsServiceExtended itemService;

	@Autowired
	public ItemController(ItemsServiceExtended itemService) {
		this.itemService = itemService;
	}

	@RequestMapping(
			path = "/twins/items/{userSpace}/{userEmail}",
			method = RequestMethod.POST,
			consumes = MediaType.APPLICATION_JSON_VALUE)
	public ItemBoundary createNewItem(
			@PathVariable("userSpace") String userSpace,
		  	@PathVariable("userEmail") String userEmail,
		  	@RequestBody ItemBoundary input) throws Exception {
		return itemService.createItem(userSpace,userEmail,input);
	}

	@RequestMapping(
			path = "/twins/items/{userSpace}/{userEmail}/{itemSpace}/{itemId}",
			method = RequestMethod.PUT,
			produces = MediaType.APPLICATION_JSON_VALUE)
	public void updateAnItem(
			@PathVariable("userSpace") String userSpace,
			@PathVariable("userEmail") String userEmail,
			@PathVariable("itemSpace") String itemSpace,
			@PathVariable("itemId") String itemId,
			@RequestBody ItemBoundary update) throws Exception {
		itemService.updateItem(userSpace,userEmail,itemSpace,itemId,update);
	}

	@RequestMapping(
			path = "/twins/items/{userSpace}/{userEmail}/{itemSpace}/{itemId}",
			method = RequestMethod.GET,
			produces = MediaType.APPLICATION_JSON_VALUE)
	public ItemBoundary retriveSpecificItem(
			@PathVariable("userSpace") String userSpace,
			@PathVariable("userEmail") String userEmail,
			@PathVariable("itemSpace") String itemSpace,
			@PathVariable("itemId") String itemId) throws Exception {
		return itemService.getSpecificItem(userSpace,userEmail,itemSpace,itemId);
	}

	@RequestMapping(
			path = "/twins/items/{userSpace}/{userEmail}",
			method = RequestMethod.GET,
			produces = MediaType.APPLICATION_JSON_VALUE)
	public List<ItemBoundary> getAllItems(
			@PathVariable("userSpace") String userSpace,
			@PathVariable("userEmail") String userEmail,
			@RequestParam(name="size", required = false, defaultValue = "5") int size,
			@RequestParam(name="page", required = false, defaultValue = "0") int page) throws Exception {
		return itemService.getAllItems(userSpace,userEmail,page,size);
	}
}
