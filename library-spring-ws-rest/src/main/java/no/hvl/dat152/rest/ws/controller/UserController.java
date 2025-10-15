/**
 * 
 */
package no.hvl.dat152.rest.ws.controller;

import java.net.http.HttpResponse;
import java.util.List;
import java.util.Set;

import org.apache.catalina.connector.Request;
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
import org.springframework.web.bind.annotation.RestController;

import no.hvl.dat152.rest.ws.exceptions.OrderNotFoundException;
import no.hvl.dat152.rest.ws.exceptions.UserNotFoundException;
import no.hvl.dat152.rest.ws.model.Order;
import no.hvl.dat152.rest.ws.model.User;
import no.hvl.dat152.rest.ws.service.UserService;

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

	@PutMapping("/users/{id}")
	public ResponseEntity<User> updateUser(@PathVariable Long id, @RequestBody User user) throws UserNotFoundException {
		User updatedUser = userService.updateUser(user, id);
		return new ResponseEntity<>(updatedUser, HttpStatus.OK);
	}

	@DeleteMapping("/users/{id}")
	public ResponseEntity<Void> deleteUser(@PathVariable Long id) throws UserNotFoundException {
		userService.deleteUser(id);
		return new ResponseEntity<>(HttpStatus.OK);
	}

	@GetMapping("/users/{id}/orders")
	public ResponseEntity<Set<Order>> getUserOrders(@PathVariable Long id) throws UserNotFoundException {
		Set<Order> orders = userService.getUserOrders(id);
		return new ResponseEntity<>(orders, HttpStatus.OK);
	}

	@GetMapping("/users/{uid}/orders/{oid}")
	public ResponseEntity<Order> getUserOrder(@PathVariable Long uid, @PathVariable Long oid)
			throws OrderNotFoundException, UserNotFoundException {
		Order order = userService.getUserOrder(uid, oid);
		return new ResponseEntity<>(order, HttpStatus.OK);
	}

	@DeleteMapping("/users/{uid}/orders/{oid}")
	public ResponseEntity<Void> deleteUserOrder(@PathVariable Long uid, @PathVariable Long oid)
			throws UserNotFoundException, OrderNotFoundException {

		userService.deleteOrderForUser(uid, oid);
		return new ResponseEntity<>(HttpStatus.OK);
	}

	@PostMapping("/users/{id}/orders")
	public ResponseEntity<Set<Order>> createUserOrder(@PathVariable Long id, @RequestBody Order order)
			throws UserNotFoundException, OrderNotFoundException {

		User user = userService.createOrdersForUser(id, order);

		user.getOrders().forEach(o -> {

			o.addLinks();

		});
		return new ResponseEntity<>(user.getOrders(), HttpStatus.CREATED);
	}

}
