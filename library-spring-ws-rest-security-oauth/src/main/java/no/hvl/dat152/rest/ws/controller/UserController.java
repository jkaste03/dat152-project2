/**
 * 
 */
package no.hvl.dat152.rest.ws.controller;

import java.util.List;
import java.util.Set;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import no.hvl.dat152.rest.ws.exceptions.OrderNotFoundException;
import no.hvl.dat152.rest.ws.exceptions.UserNotFoundException;
import no.hvl.dat152.rest.ws.model.User;
import no.hvl.dat152.rest.ws.model.Order;
import no.hvl.dat152.rest.ws.service.UserService;

import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.*;
import org.springframework.hateoas.Link;

/**
 * @author tdoy
 */
@RestController
@RequestMapping("/elibrary/api/v1")
public class UserController {

	@Autowired
	private UserService userService;

	@GetMapping("/users")
	public ResponseEntity<Object> getUsers() {

		List<User> users = userService.findAllUsers();

		if (users.isEmpty())

			return new ResponseEntity<>(HttpStatus.NO_CONTENT);
		else
			return new ResponseEntity<>(users, HttpStatus.OK);
	}

	@GetMapping(value = "/users/{id}")
	public ResponseEntity<Object> getUser(@PathVariable Long id) throws UserNotFoundException, OrderNotFoundException {

		User user = userService.findUser(id);

		return new ResponseEntity<>(user, HttpStatus.OK);

	}

	// DONE - createUser (@Mappings, URI=/users, and method)
	@PostMapping("/users")
	public ResponseEntity<User> createUser(@RequestBody User user) {
		userService.saveUser(user); // CRUDrepo throws error if failed
		return new ResponseEntity<>(user, HttpStatus.CREATED);
	}

	// DONE - updateUser (@Mappings, URI, and method)
	@PutMapping("/users/{id}")
	public ResponseEntity<User> updateUser(@PathVariable Long id, @RequestBody User user) throws UserNotFoundException {
		return new ResponseEntity<>(userService.updateUser(user, id), HttpStatus.OK);
	}

	// DONE - deleteUser (@Mappings, URI, and method)
	@DeleteMapping("/users/{uid}")
	public ResponseEntity<User> deleteUser(@PathVariable Long uid) throws UserNotFoundException {
		User eUser = userService.findUser(uid);
		userService.deleteUser(uid);
		return new ResponseEntity<>(eUser, HttpStatus.OK);
	}

	// DONE - getUserOrders (@Mappings, URI=/users/{id}/orders, and method)
	@GetMapping(value = "/users/{id}/orders")
	public ResponseEntity<Object> getUserOrders(@PathVariable long id)
			throws UserNotFoundException, OrderNotFoundException {

		Set<Order> orders = userService.getUserOrders(id);

		return new ResponseEntity<>(orders, HttpStatus.OK);
	}

	// DONE - getUserOrder (@Mappings, URI=/users/{uid}/orders/{oid}, and method)

	@GetMapping(value = "/users/{uid}/orders/{oid}")
	public ResponseEntity<Order> getUserOrder(@PathVariable long uid, @PathVariable long oid)
			throws UserNotFoundException, OrderNotFoundException {

		Order order = userService.getUserOrder(uid, oid);
		return new ResponseEntity<>(order, HttpStatus.OK);
	}
	// DONE - deleteUserOrder (@Mappings, URI, and method)

	@DeleteMapping(value = "/users/{uid}/orders/{oid}")
	public ResponseEntity<Void> deleteUserOrder(@PathVariable long uid, @PathVariable long oid)
			throws UserNotFoundException, OrderNotFoundException {

		userService.findUser(uid);
		userService.deleteOrderForUser(uid, oid);

		return new ResponseEntity<>(HttpStatus.OK);
	}

	// DONE - createUserOrder (@Mappings, URI, and method) + HATEOAS links

	@PostMapping("/users/{uid}/orders")
	public ResponseEntity<Object> createUserOrder(@PathVariable Long uid, @RequestBody Order order)
			throws UserNotFoundException, OrderNotFoundException {

		User user = userService.createOrdersForUser(uid, order);
		Set<Order> orders = user.getOrders();

		for (Order o : orders) {
			o.add(linkTo(methodOn(UserController.class).getUserOrder(uid, o.getId())).withSelfRel());
			o.add(linkTo(methodOn(UserController.class).deleteUserOrder(uid, o.getId())).withRel("deleteOrder"));
			o.add(linkTo(methodOn(UserController.class).getUserOrders(uid)).withRel("allOrders"));
		}

		return new ResponseEntity<>(orders, HttpStatus.CREATED);
	}
}
