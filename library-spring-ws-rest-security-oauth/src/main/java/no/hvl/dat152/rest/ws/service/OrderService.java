/**
 * 
 */
package no.hvl.dat152.rest.ws.service;

import java.util.List;

import java.time.LocalDate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataAccessException;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.oauth2.server.resource.authentication.JwtAuthenticationToken;
import org.springframework.stereotype.Service;

import no.hvl.dat152.rest.ws.exceptions.OrderNotFoundException;
import no.hvl.dat152.rest.ws.exceptions.UnauthorizedOrderActionException;
import no.hvl.dat152.rest.ws.exceptions.UpdateOrderFailedException;
import no.hvl.dat152.rest.ws.model.Order;
import no.hvl.dat152.rest.ws.repository.OrderRepository;
import no.hvl.dat152.rest.ws.security.UserDetailsImpl;

/**
 * @author tdoy
 */
@Service
@PreAuthorize("hasRole('ADMIN')")
public class OrderService {

	@Autowired
	private OrderRepository orderRepository;

	public Order saveOrder(Order order) {

		order = orderRepository.save(order);

		return order;
	}

	public Order findOrder(Long id) throws OrderNotFoundException {

		Order order = orderRepository.findById(id)
				.orElseThrow(
						() -> new OrderNotFoundException("Order with id: " + id + " not found in the order list!"));

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
}
