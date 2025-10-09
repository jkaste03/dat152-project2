/**
 * 
 */
package no.hvl.dat152.rest.ws.service;

import java.util.List;
import java.util.Optional;
import java.time.LocalDate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import no.hvl.dat152.rest.ws.exceptions.OrderNotFoundException;
import no.hvl.dat152.rest.ws.exceptions.NoOrdersFoundException;
import no.hvl.dat152.rest.ws.model.Order;
import no.hvl.dat152.rest.ws.repository.OrderRepository;

/**
 * @author tdoy
 */
@Service
public class OrderService {

	@Autowired
	private OrderRepository orderRepository;

	public Order saveOrder(Order order) {

		order = orderRepository.save(order);

		return order;
	}

	public Order findOrder(Long id) throws OrderNotFoundException {

		Order order = orderRepository.findById(id)
				.orElseThrow(() -> new OrderNotFoundException("Order with id: " + id + " not found in the order list!"));

		return order;
	}

	public void deleteOrder(Long id) throws OrderNotFoundException {
		Order order = orderRepository.findById(id)
				.orElseThrow(() -> new OrderNotFoundException("Order with id: " + id + " not found in the order list!"));
		orderRepository.delete(order);
	}

	public List<Order> findAllOrders() throws NoOrdersFoundException {
		List<Order> allOrders = orderRepository.findAll();

		if (allOrders.isEmpty()) {
			throw new NoOrdersFoundException("Could not find any orders in the order list.");
		}

		return allOrders;
	}

	public List<Order> findByExpiryDate(LocalDate expiry, Pageable page) throws NoOrdersFoundException {
		Page<Order> orderPage = orderRepository.findByExpiryBefore(expiry, page);
		List<Order> orders = orderPage.getContent();

		if (orders.isEmpty()) {
			throw new NoOrdersFoundException("No orders found before " + expiry);
		}

		return orders;
	}

	public Order updateOrder(Order order, Long id) throws OrderNotFoundException {

		Order existing = orderRepository.findById(id)
				.orElseThrow(() -> new OrderNotFoundException("Order with id: " + id + " not found in the order list!"));

		existing.setIsbn(order.getIsbn());
		existing.setExpiry(order.getExpiry());

		return orderRepository.save(existing);
	}
}
