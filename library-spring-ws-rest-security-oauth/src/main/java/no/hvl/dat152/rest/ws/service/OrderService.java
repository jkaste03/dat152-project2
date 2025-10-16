/**
 * 
 */
package no.hvl.dat152.rest.ws.service;

import java.util.List;
import java.util.Optional;
import java.time.LocalDate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Lazy;
import org.springframework.dao.DataAccessException;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.oauth2.jwt.Jwt;
import org.springframework.security.oauth2.server.resource.authentication.JwtAuthenticationToken;
import org.springframework.stereotype.Service;

import no.hvl.dat152.rest.ws.exceptions.OrderNotFoundException;
import no.hvl.dat152.rest.ws.exceptions.UnauthorizedOrderActionException;
import no.hvl.dat152.rest.ws.exceptions.UpdateOrderFailedException;
import no.hvl.dat152.rest.ws.exceptions.UserNotFoundException;
import no.hvl.dat152.rest.ws.model.Order;
import no.hvl.dat152.rest.ws.model.User;
import no.hvl.dat152.rest.ws.repository.OrderRepository;
import no.hvl.dat152.rest.ws.security.UserDetailsImpl;

/**
 * @author tdoy
 */
@Service
// @PreAuthorize("hasRole('ADMIN')")
public class OrderService {

	@Autowired
	private OrderRepository orderRepository;

	private final UserService userService;

	public OrderService(@Lazy UserService userService) {
		this.userService = userService;
	}

	public Order saveOrder(Order order) {

		order = orderRepository.save(order);

		return order;
	}

	public Order findOrder(Long id) throws OrderNotFoundException {

		Order order = orderRepository.findById(id)
				.orElseThrow(
						() -> new OrderNotFoundException("Order with id: " + id + " not found in the repository!"));

		return order;
	}

	public void deleteOrder(Long id) throws OrderNotFoundException {
		orderRepository.delete(findOrder(id));
	}

	public List<Order> findAllOrders() {
		return (List<Order>) orderRepository.findAll();
	}

	public List<Order> findByExpiryDate(LocalDate expiry, Pageable page) {
		if (expiry == null) {
			return orderRepository.findByExpiryBefore(LocalDate.now().plusYears(1000), page).getContent();
		}
		return orderRepository.findOrderByExpiry(expiry, page.getPageSize(), (int) page.getOffset());

	}

	public Order updateOrder(Order order, long id)
			throws UpdateOrderFailedException, OrderNotFoundException {
		findOrder(id);
		if (order.getId() == null) {
			order.setId(id);
		}
		if (id != order.getId()) {
			throw new UpdateOrderFailedException(
					"Id mismatch between provided order (" + order.getId() + ") and provided id (" + id + ")");
		}
		try {
			return orderRepository.save(order);
		} catch (DataAccessException e) {
			throw new UpdateOrderFailedException(
					"Failed to update order with id " + id + ": " + e.getMessage(), e);
		}
	}

	public User createOrder(String isbn) throws UserNotFoundException {
		Order order = new Order(isbn, LocalDate.now().plusMonths(1));
		System.out.println("Order: " + order);

		Authentication auth = SecurityContextHolder.getContext().getAuthentication();
		Long userId = null;

		Object details = auth.getDetails();
		if (details instanceof UserDetailsImpl ud) {
			userId = ud.getUserid();
		}

		if (userId == null) {
			throw new UserNotFoundException("Authenticated user id not found");
		}

		System.out.println("UserId: " + userId);

		User user = userService.findUser(userId);
		System.out.println("User: " + user);
		user.getOrders().add(order);
		saveOrder(order);
		return user;
	}
}
