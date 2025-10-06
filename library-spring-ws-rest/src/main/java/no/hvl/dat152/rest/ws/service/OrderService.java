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

import no.hvl.dat152.rest.ws.exceptions.BookNotFoundException;
import no.hvl.dat152.rest.ws.exceptions.OrderNotFoundException;
import no.hvl.dat152.rest.ws.exceptions.UserNotFoundException;
import no.hvl.dat152.rest.ws.model.Book;
import no.hvl.dat152.rest.ws.model.Order;
import no.hvl.dat152.rest.ws.model.User;
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

		return orderRepository.findOrderByExpiry(expiry, page.getPageSize(), (int) page.getOffset());

	}

	public Order updateOrder(Order order, Long id) throws OrderNotFoundException {
		Order existing = findOrder(id);

		existing.setExpiry(order.getExpiry());
		existing.setIsbn(order.getIsbn());

		Order uOrder = saveOrder(existing);

		return uOrder;
	}

}
