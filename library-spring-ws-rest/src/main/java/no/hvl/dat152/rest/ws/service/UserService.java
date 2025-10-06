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
import no.hvl.dat152.rest.ws.model.Author;
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
	private OrderService orderService;

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

	public User updateUser(User user, Long id) throws UserNotFoundException {
		User existing = findUser(id);

		existing.setFirstname(user.getFirstname());
		existing.setLastname(user.getLastname());
		existing.setOrders(user.getOrders());

		User uUser = saveUser(existing);

		return uUser;
	}

	public Set<Order> getUserOrders(Long userid) throws UserNotFoundException {
		User existing = findUser(userid);
		return existing.getOrders();
	}

	public Order getUserOrder(Long userid, Long oid) throws UserNotFoundException {
		return getUserOrders(userid).stream().filter(o -> o.getId() == oid).findAny().orElse(null);
	}

	public void deleteOrderForUser(Long userid, Long oid) throws UserNotFoundException {
		Order order = getUserOrder(userid, oid);
		orderService.deleteOrder(order.getId());
	}

	public User createOrdersForUser(Long userid, Order order) throws UserNotFoundException {
		User user = findUser(userid);
		user.getOrders().add(order);
		orderService.saveOrder(order);
		return user;
	}
}
