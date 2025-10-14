/**
 * 
 */
package no.hvl.dat152.rest.ws.controller;

import java.util.List;
import java.util.Optional;
import java.util.Set;

import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.hateoas.Link;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

import no.hvl.dat152.rest.ws.exceptions.OrderNotFoundException;
import no.hvl.dat152.rest.ws.exceptions.UserNotFoundException;
import no.hvl.dat152.rest.ws.model.Order;
import no.hvl.dat152.rest.ws.model.User;
import no.hvl.dat152.rest.ws.service.UserService;
import no.hvl.dat152.rest.ws.service.OrderService;

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

	@PostMapping("/users")
	public ResponseEntity<User> createUser(@RequestBody User user) {
		User newUser = userService.saveUser(user);

		return new ResponseEntity<>(newUser, HttpStatus.CREATED);
	}

	@PutMapping("/users/{uid}")
	public ResponseEntity<User> updateUser(@PathVariable Long uid, @RequestBody User user)
			throws UserNotFoundException {
		User updatedUser = userService.updateUser(uid, user);
		return ResponseEntity.ok(updatedUser);
	}

	@DeleteMapping("/users/{uid}")
	public ResponseEntity<User> deleteUser(@PathVariable Long uid)
			throws UserNotFoundException {
		userService.findUser(uid);
		userService.deleteUser(uid);
		return new ResponseEntity<>(HttpStatus.OK);
	}

	@GetMapping("/users/{id}/orders")
	public ResponseEntity<List<Order>> getUserOrders(@PathVariable Long uid)
			throws UserNotFoundException, OrderNotFoundException {
		userService.findUser(uid);
		userService.getUserOrders(uid);
		return new ResponseEntity<>(HttpStatus.OK);

	}

	@GetMapping("/users/{uid}/orders/{oid}")
	public ResponseEntity<Order> getUserOrder(@PathVariable Long uid, Long oid)
			throws UserNotFoundException, OrderNotFoundException {
		userService.findUser(uid);
		userService.getUserOrder(uid, oid);

		return new ResponseEntity<>(HttpStatus.OK);

	}

	@DeleteMapping("/user/{uid}/orders/{oid}")
	public ResponseEntity<Order> deleteUserOrder(@PathVariable Long uid, Long oid)
			throws UserNotFoundException, OrderNotFoundException {
		userService.findUser(uid);
		userService.deleteOrderForUser(uid, oid);

		return new ResponseEntity<>(HttpStatus.OK);
	}

	@PostMapping("/users/{uid}/orders")
	public ResponseEntity<User> createUserOrder(@PathVariable Long uid, @RequestBody Order order)
			throws UserNotFoundException, OrderNotFoundException {

		User user = userService.createOrdersForUser(uid, order);

		Link selfLink = linkTo(methodOn(UserController.class)
				.getUserOrders(uid)).withSelfRel();

		Link userLink = linkTo(methodOn(UserController.class)
				.getUser(uid)).withRel("user");

		Link allOrdersLink = linkTo(methodOn(UserController.class)
				.getUserOrders(uid)).withRel("all-orders");

		user.add(selfLink);
		user.add(userLink);
		user.add(allOrdersLink);

		return new ResponseEntity<>(user, HttpStatus.CREATED);
	}

}
