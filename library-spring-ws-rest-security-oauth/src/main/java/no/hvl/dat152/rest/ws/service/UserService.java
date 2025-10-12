/**
 * 
 */
package no.hvl.dat152.rest.ws.service;

import java.util.List;
import java.util.Set;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataAccessException;
import org.springframework.stereotype.Service;

import no.hvl.dat152.rest.ws.exceptions.OrderNotFoundException;
import no.hvl.dat152.rest.ws.exceptions.UpdateUserFailedException;
import no.hvl.dat152.rest.ws.exceptions.UserNotFoundException;
import no.hvl.dat152.rest.ws.model.Order;
import no.hvl.dat152.rest.ws.model.User;
import no.hvl.dat152.rest.ws.repository.UserRepository;

/**
 * @author tdoy
 */
@Service
public class UserService {

	private final UserRepository userRepository;
	private final OrderService orderService;

	public UserService(UserRepository userRepository, OrderService orderService) {
		this.userRepository = userRepository;
		this.orderService = orderService;
	}

	public User saveUser(User user) {
		return userRepository.save(user);
	}

	public List<User> findAllUsers() {

		List<User> allUsers = (List<User>) userRepository.findAll();

		return allUsers;
	}

	public User findUser(Long userid) throws UserNotFoundException {

		User user = userRepository.findById(userid)
				.orElseThrow(() -> new UserNotFoundException("User with id: " + userid + " not found"));

		return user;
	}

	public void deleteUser(Long id) throws UserNotFoundException {
		userRepository.delete(findUser(id));
	}

	public User updateUser(User user, long id)
			throws UpdateUserFailedException, UserNotFoundException {
		findUser(id);
		if (id != user.getUserid()) {
			throw new UpdateUserFailedException(
					"Id mismatch between provided user (" + user.getUserid() + ") and provided id (" + id + ")");
		}
		try {
			return userRepository.save(user);
		} catch (DataAccessException e) {
			throw new UpdateUserFailedException(
					"Failed to update user with id " + id + ": " + e.getMessage(), e);
		}
	}

	public Set<Order> getUserOrders(Long userid) throws UserNotFoundException {
		User existing = findUser(userid);
		return existing.getOrders();
	}

	public Order getUserOrder(Long userid, Long oid) throws UserNotFoundException, OrderNotFoundException {
		return getUserOrders(userid).stream().filter(o -> o.getId() == oid).findAny()
				.orElseThrow(() -> new OrderNotFoundException(
						"User with id: " + userid + " does not have an order with id: " + oid));
	}

	public void deleteOrderForUser(Long userid, Long oid) throws UserNotFoundException, OrderNotFoundException {
		User user = findUser(userid);
		Order order = getUserOrder(userid, oid);
		user.removeOrder(order);
		orderService.deleteOrder(oid);
	}

	public User createOrdersForUser(Long userid, Order order) throws UserNotFoundException {
		User user = findUser(userid);
		user.getOrders().add(order);
		orderService.saveOrder(order);
		return user;
	}
}
