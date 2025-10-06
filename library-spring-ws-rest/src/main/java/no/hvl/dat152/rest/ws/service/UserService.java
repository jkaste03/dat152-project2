/**
 * 
 */
package no.hvl.dat152.rest.ws.service;

import java.util.Iterator;
import java.util.List;
import java.util.Set;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import no.hvl.dat152.rest.ws.controller.UserController;
import no.hvl.dat152.rest.ws.exceptions.OrderNotFoundException;
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
	private OrderRepository orderRepository;

	public List<User> findAllUsers() {

		List<User> allUsers = (List<User>) userRepository.findAll();

		return allUsers;
	}

	public User findUser(Long id) throws UserNotFoundException {

		User user = userRepository.findById(id)
				.orElseThrow(() -> new UserNotFoundException("User with id: " + id + " not found"));

		return user;
	}

	public User saveUser(User user) {
		return userRepository.save(user);
	}

	public void deleteUser(Long id) throws UserNotFoundException {
		User user = userRepository.findById(id)
				.orElseThrow(() -> new UserNotFoundException("User not found for id: " + id));
		userRepository.delete(user);

	}

	public User updateUser(User user, Long id) throws UserNotFoundException {
		if (!userRepository.existsById(id) || user.getUserid() != id) {
			throw new UserNotFoundException("User not found for id: " + id);
		}

		return userRepository.save(user);
	}

	public Set<Order> getUserOrders(Long userid) throws UserNotFoundException {
		User user = userRepository.findById(userid)
				.orElseThrow(() -> new UserNotFoundException("User not found for id: " + userid));
		return user.getOrders();
	}

	public Order getUserOrder(Long userid, Long oid) throws OrderNotFoundException, UserNotFoundException {
		User user = userRepository.findById(userid)
				.orElseThrow(() -> new UserNotFoundException("User not found for id: " + userid));
		return user.getOrders().stream().filter(o -> o.getId() == oid).findFirst()
				.orElseThrow(() -> new OrderNotFoundException("Order not found for id: " + oid));
	}

	public void deleteOrderForUser(Long userid, Long oid) throws UserNotFoundException, OrderNotFoundException {
		User user = userRepository.findById(userid)
				.orElseThrow(() -> new UserNotFoundException("User not found for id: " + userid));

		Order order = user.getOrders().stream().filter(o -> o.getId() == oid).findFirst()
				.orElseThrow(() -> new OrderNotFoundException("Order not found for id: " + oid));
		user.getOrders().remove(order);
		userRepository.save(user);
	}

	public Set<Order> createOrdersForUser(Long userid, Order order) throws UserNotFoundException {
		User user = userRepository.findById(userid)
				.orElseThrow(() -> new UserNotFoundException("User not found for id: " + userid));
		orderRepository.save(order);
		user.getOrders().add(order);
		userRepository.save(user);
		return user.getOrders();
	}
}
