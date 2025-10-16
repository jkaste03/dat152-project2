/**
 * 
 */
package no.hvl.dat152.rest.ws.service;

import java.util.Iterator;
import java.util.List;
import java.util.Set;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import no.hvl.dat152.rest.ws.exceptions.OrderNotFoundException;
import no.hvl.dat152.rest.ws.exceptions.UserNotFoundException;
import no.hvl.dat152.rest.ws.model.Order;
import no.hvl.dat152.rest.ws.model.User;
import no.hvl.dat152.rest.ws.repository.OrderRepository;
import no.hvl.dat152.rest.ws.repository.UserRepository;

/**
 * @user tdoy
 */
@Service
public class UserService {

	@Autowired
	private UserRepository userRepository;

	public List<User> findAllUsers() {

		List<User> allUsers = (List<User>) userRepository.findAll();

		return allUsers;
	}

	public User findUser(Long userid) throws UserNotFoundException {

		User user = userRepository.findById(userid)
				.orElseThrow(() -> new UserNotFoundException("User with id: " + userid + " not found"));

		return user;
	}

	// DONE public User saveUser(User user)
	public User saveUser(User user) {
		return userRepository.save(user);
	}

	// DONE public void deleteUser(Long id) throws UserNotFoundException
	public void deleteUser(Long id) throws UserNotFoundException {
		User user = userRepository.findById(id)
				.orElseThrow(() -> new UserNotFoundException("User with the id: " + id + "not found!"));
		userRepository.delete(user);
	}

	// DONE public User updateUser(User user, Long id)
	public User updateUser(User user, Long id) throws UserNotFoundException {
		User eUser = userRepository.findById(id)
				.orElseThrow(() -> new UserNotFoundException("User with id " + id + " not found!"));
		eUser.setFirstname(user.getFirstname());
		eUser.setLastname(user.getLastname());
		eUser.setOrders(user.getOrders());

		return userRepository.save(eUser);
	}

	// DONE public Set<Order> getUserOrders(Long userid)
	Set<Order> getUserOrders(Long userid) throws UserNotFoundException, OrderNotFoundException {

		User user = userRepository.findById(userid)
				.orElseThrow(() -> new UserNotFoundException("User with id " + userid + " not found!"));
		if (user.getOrders().isEmpty()) {
			throw new OrderNotFoundException("Found no orders for user!");
		}
		return user.getOrders();
	}

	// TODO public Order getUserOrder(Long userid, Long oid)
	public Order getUserOrder(Long uid, Long oid) throws UserNotFoundException, OrderNotFoundException {
		User eUser = userRepository.findById(uid)
				.orElseThrow(() -> new UserNotFoundException("User with id " + uid + " not found!"));
		Set<Order> userOrders = eUser.getOrders();
		Order userOrder = userOrders.stream()
				.filter(o -> o.getId().equals(oid))
				.findFirst()
				.orElse(null);
		if (userOrder == null) {
			throw new OrderNotFoundException("Could not find order with ID " + oid + " for user with ID " + uid);
		}
		return userOrder;
	}

	// TODO public void deleteOrderForUser(Long userid, Long oid)
	public void deleteOrderForUser(Long uid, Long oid) throws UserNotFoundException {
		User eUser = userRepository.findById(uid)
				.orElseThrow(() -> new UserNotFoundException("User with id " + uid + " not found!"));
		Set<Order> userOrders = eUser.getOrders();
	}

	// TODO public User createOrdersForUser(Long userid, Order order)
}
