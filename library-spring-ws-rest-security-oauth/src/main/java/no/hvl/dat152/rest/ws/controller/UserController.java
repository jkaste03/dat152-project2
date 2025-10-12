/**
 * 
 */
package no.hvl.dat152.rest.ws.controller;

import java.util.List;
import java.util.Set;

import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.hateoas.Link;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import no.hvl.dat152.rest.ws.controller.hateoas.OrderLinkAdder;
import no.hvl.dat152.rest.ws.exceptions.OrderNotFoundException;
import no.hvl.dat152.rest.ws.exceptions.UnauthorizedOrderActionException;
import no.hvl.dat152.rest.ws.exceptions.UpdateOrderFailedException;
import no.hvl.dat152.rest.ws.exceptions.UpdateUserFailedException;
import no.hvl.dat152.rest.ws.exceptions.UserNotFoundException;
import no.hvl.dat152.rest.ws.model.Order;
import no.hvl.dat152.rest.ws.model.User;
import no.hvl.dat152.rest.ws.service.UserService;

/**
 * @author tdoy
 */
@RestController
@RequestMapping("/elibrary/api/v1")
@PreAuthorize("hasRole('ADMIN')")
public class UserController {

	// TODO authority annotation

	@Autowired
	private UserService userService;

	private final OrderLinkAdder orderLinkAdder;

	public UserController(OrderLinkAdder userLinkAdder) {
		this.orderLinkAdder = userLinkAdder;
	}

	@GetMapping("/users")
	public ResponseEntity<Object> getUsers() {

		List<User> users = userService.findAllUsers();

		if (users.isEmpty())

			return new ResponseEntity<>(HttpStatus.NO_CONTENT);
		else
			return new ResponseEntity<>(users, HttpStatus.OK);
	}

	@GetMapping(value = "/users/{id}")
	@PreAuthorize("hasRole('ADMIN') or #id == authentication.details.userid")
	public ResponseEntity<Object> getUser(@PathVariable long id)
			throws UserNotFoundException {

		User user = userService.findUser(id);

		return new ResponseEntity<>(user, HttpStatus.OK);

	}

	@PostMapping("/users")
	public ResponseEntity<User> createUser(@RequestBody User user) {
		User nUser = userService.saveUser(user);

		return new ResponseEntity<>(nUser, HttpStatus.CREATED);
	}

	@PutMapping("/users/{id}")
	@PreAuthorize("hasRole('ADMIN') or #id == authentication.details.userid")
	public ResponseEntity<User> updateUser(@PathVariable Long id, @RequestBody User user)
			throws UserNotFoundException, UpdateUserFailedException {
		User uUser = userService.updateUser(user, id);
		return new ResponseEntity<>(uUser, HttpStatus.OK);
	}

	@DeleteMapping("/users/{id}")
	@PreAuthorize("hasRole('ADMIN') or #id == authentication.details.userid")
	public ResponseEntity<Void> deleteUser(@PathVariable long id) throws UserNotFoundException {
		userService.findUser(id); // Will catch exception if not found
		userService.deleteUser(id);

		return new ResponseEntity<>(HttpStatus.OK);
	}

	@GetMapping(value = "/users/{id}/orders")
	@PreAuthorize("hasRole('ADMIN') or #id == authentication.details.userid")
	public ResponseEntity<Object> getUserOrders(@PathVariable long id)
			throws UserNotFoundException, OrderNotFoundException {

		Set<Order> orders = userService.getUserOrders(id);

		return new ResponseEntity<>(orders, HttpStatus.OK);
	}

	@GetMapping(value = "/users/{uid}/orders/{oid}")
	@PreAuthorize("hasRole('ADMIN') or #uid == authentication.details.userid")
	public ResponseEntity<Order> getUserOrder(@PathVariable long uid, @PathVariable long oid)
			throws UserNotFoundException {

		Order order = userService.getUserOrder(uid, oid);

		return new ResponseEntity<>(order, HttpStatus.OK);
	}

	@DeleteMapping(value = "/users/{uid}/orders/{oid}")
	@PreAuthorize("hasRole('ADMIN') or #uid == authentication.details.userid")
	public ResponseEntity<Void> deleteUserOrder(@PathVariable long uid, @PathVariable long oid)
			throws UserNotFoundException, OrderNotFoundException {

		userService.findUser(uid); // Will catch exception if not found
		userService.deleteOrderForUser(uid, oid);

		return new ResponseEntity<>(HttpStatus.OK);

	}

	@PostMapping(value = "/users/{uid}/orders")
	@PreAuthorize("hasRole('ADMIN') or #uid == authentication.details.userid")
	public ResponseEntity<Object> createUserOrder(@PathVariable long uid, @RequestBody Order order)
			throws UserNotFoundException, OrderNotFoundException, UpdateOrderFailedException {

		User user = userService.createOrdersForUser(uid, order);
		Set<Order> orders = user.getOrders();

		orderLinkAdder.addLinks(orders);

		return new ResponseEntity<>(orders, HttpStatus.CREATED);
	}
}
