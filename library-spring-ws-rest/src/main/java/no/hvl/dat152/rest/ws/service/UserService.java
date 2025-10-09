/**
 * 
 */
package no.hvl.dat152.rest.ws.service;

import java.util.HashSet;
import java.util.List;
import java.util.Optional;
import java.util.Set;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import no.hvl.dat152.rest.ws.exceptions.UserNotFoundException;
import no.hvl.dat152.rest.ws.model.Order;
import no.hvl.dat152.rest.ws.model.User;
import no.hvl.dat152.rest.ws.repository.OrderRepository;
import no.hvl.dat152.rest.ws.repository.UserRepository;

/**
 * @author tdoy
 */
@Service
public class UserService {

	@Autowired
	private UserRepository userRepository;

	@Autowired
	private OrderRepository OrderRepository;

	public List<User> findAllUsers() {

		List<User> allUsers = (List<User>) userRepository.findAll();

		return allUsers;
	}

	public User findUser(Long userid) throws UserNotFoundException {

		User user = userRepository.findById(userid)
				.orElseThrow(() -> new UserNotFoundException("User with id: " + userid + " not found"));

		return user;
	}

	public User saveUser(User user) {
		return userRepository.save(user);
	}

	public void deleteUser(Long id) throws UserNotFoundException {

		User user = userRepository.findById(id)
				.orElseThrow(() -> new UserNotFoundException("User with id = " + id + " not found"));
		userRepository.delete(user);
	}

	public User updateUser(User user, Long id) throws UserNotFoundException {
		Optional<User> optionalUser = userRepository.findById(user.getUserid());

		if (optionalUser.isEmpty()) {
			throw new UserNotFoundException("Author with id = " + user.getUserid() + " not found!");
		}
		User existingUser = optionalUser.get();
		existingUser.setFirstname(user.getFirstname());
		existingUser.setLastname(user.getLastname());
		existingUser.setOrders(user.getOrders());

		return userRepository.save(existingUser);
	}

	public Set<Order> getUserOrders(Long id) {
		User user = userRepository.findById(id)
				.orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "User not found"));
		return new HashSet<>(user.getOrders());
	}

	public Order getUserOrder(Long userid, Long oid) {
		User user = userRepository.findById(userid)
				.orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "User not found"));

		Order order = user.getOrders().stream()
				.filter(o -> o.getId().equals(oid))
				.findFirst()
				.orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Order not found"));

		return order;
	}

	public void deleteOrderForUser(Long userid, Long oid) {
		User user = userRepository.findById(userid)
				.orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "User not found"));

		Order order = user.getOrders().stream()
				.filter(o -> o.getId().equals(oid))
				.findFirst()
				.orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Order not found"));

		OrderRepository.delete(order);
	}

	public User createOrdersForUser(Long userid, Order order) {
		User user = userRepository.findById(userid)
				.orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "User not found"));

		user.addOrder(order);
		return user;
	}
}
