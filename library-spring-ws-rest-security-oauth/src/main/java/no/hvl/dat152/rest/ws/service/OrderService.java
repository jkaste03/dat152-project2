/**
 * 
 */
package no.hvl.dat152.rest.ws.service;

import java.util.List;

import java.time.LocalDate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import no.hvl.dat152.rest.ws.exceptions.OrderNotFoundException;
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

	// DONE public void deleteOrder(Long id)
	public void deleteOrder(Long id) throws OrderNotFoundException {
		Order order = orderRepository.findById(id)
				.orElseThrow(() -> new OrderNotFoundException("Order with id: " + id + " not found in the order list!"));

		orderRepository.delete(order);
		;
	}

	// DONE public List<Order> findAllOrders()
	public List<Order> findAllOrders() throws OrderNotFoundException {
		if (orderRepository.findAll().isEmpty()) {
			throw new OrderNotFoundException("Found no orders!");
		}
		return orderRepository.findAll();
	}

	// DONE public List<Order> findByExpiryDate(LocalDate expiry, Pageable page)

	public List<Order> findByExpiryDate(LocalDate expiry, Pageable pageable) throws OrderNotFoundException {
		Page<Order> result;

		if (expiry == null) {
			result = orderRepository.findAll(pageable);
		} else {
			result = orderRepository.findByExpiryBefore(expiry, pageable);
		}

		if (result.isEmpty()) {
			throw new OrderNotFoundException("No orders found for the given expiry date!");
		}

		return result.getContent();
	}

	// DONE public Order updateOrder(Order order, Long id)
	public Order updateOrder(Order order, Long id) throws OrderNotFoundException {
		Order eOrder = orderRepository.findById(id)
				.orElseThrow(() -> new OrderNotFoundException("Order with id: " + id + " not found in the order list!"));
		eOrder.setIsbn(order.getIsbn());
		eOrder.setExpiry(order.getExpiry());
		orderRepository.save(eOrder);

		return eOrder;
	}
}
