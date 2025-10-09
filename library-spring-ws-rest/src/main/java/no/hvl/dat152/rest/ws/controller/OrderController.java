/**
 * 
 */
package no.hvl.dat152.rest.ws.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import no.hvl.dat152.rest.ws.exceptions.NoOrdersFoundException;
import no.hvl.dat152.rest.ws.exceptions.OrderNotFoundException;
import no.hvl.dat152.rest.ws.model.Order;
import no.hvl.dat152.rest.ws.service.OrderService;

/**
 * @author tdoy
 */
@RestController
@RequestMapping("/elibrary/api/v1")
public class OrderController {

	@Autowired
	private OrderService orderService;

	@GetMapping("/orders")
	public ResponseEntity<List<Order>> getAllBorrowOrders() throws NoOrdersFoundException {
		List<Order> orders = orderService.findAllOrders();
		return new ResponseEntity<>(orders, HttpStatus.OK);
	}

	@GetMapping("/orders/{id}")
	public ResponseEntity<Order> getBorrowOrder(@PathVariable Long id) throws OrderNotFoundException {
		Order order = orderService.findOrder(id);
		return new ResponseEntity<>(order, HttpStatus.OK);
	}

	@PutMapping("/orders/{id}")
	public ResponseEntity<Order> updateOrder(@PathVariable Long id, @RequestBody Order order)
			throws OrderNotFoundException {
		Order updatedOrder = orderService.updateOrder(order, id);
		return new ResponseEntity<>(updatedOrder, HttpStatus.OK);
	}

	@DeleteMapping("/orders/{id}")
	public ResponseEntity<Void> deleteBookOrder(@PathVariable Long id) throws OrderNotFoundException {
		orderService.deleteOrder(id);
		return new ResponseEntity<>(HttpStatus.NO_CONTENT);
	}
}
