/**
 * 
 */
package no.hvl.dat152.rest.ws.controller;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.hateoas.Link;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import no.hvl.dat152.rest.ws.controller.hateoas.OrderLinkAdder;
import no.hvl.dat152.rest.ws.exceptions.OrderNotFoundException;
import no.hvl.dat152.rest.ws.exceptions.UnauthorizedOrderActionException;
import no.hvl.dat152.rest.ws.exceptions.UpdateOrderFailedException;
import no.hvl.dat152.rest.ws.exceptions.UserNotFoundException;
import no.hvl.dat152.rest.ws.model.Order;
import no.hvl.dat152.rest.ws.service.OrderService;

/**
 * @author tdoy
 */
@RestController
@RequestMapping("/elibrary/api/v1")
public class OrderController {

	// TODO authority annotation

	private final OrderService orderService;
	private final OrderLinkAdder orderLinkAdder;

	public OrderController(OrderService orderService, OrderLinkAdder orderLinkAdder) {
		this.orderService = orderService;
		this.orderLinkAdder = orderLinkAdder;
	}

	@GetMapping("/orders")
	public ResponseEntity<Object> getAllBorrowOrders(@RequestParam(required = false) LocalDate expiry,
			@RequestParam(required = false) int page,
			@RequestParam(required = false) int size) {
		List<Order> orders = orderService.findByExpiryDate(expiry, Pageable.ofSize(size));
		return new ResponseEntity<>(orders, HttpStatus.OK);
	}

	@GetMapping("orders/{id}")
	public ResponseEntity<Order> getBorrowOrder(@PathVariable long id)
			throws OrderNotFoundException, UpdateOrderFailedException {
		Order order = orderService.findOrder(id);
		orderLinkAdder.addLinks(order);
		return new ResponseEntity<>(order, HttpStatus.OK);
	}

	@PutMapping("/orders/{id}")
	public ResponseEntity<Order> updateOrder(@PathVariable long id, @RequestBody Order order)
			throws OrderNotFoundException, UpdateOrderFailedException {
		Order nOrder = orderService.updateOrder(order, id);
		return new ResponseEntity<>(nOrder, HttpStatus.OK);
	}

	@DeleteMapping("/orders/{id}")
	public ResponseEntity<Order> deleteBookOrder(@PathVariable long id) throws OrderNotFoundException {
		if (orderService.findOrder(id) == null) {
			return new ResponseEntity<>(HttpStatus.NOT_FOUND);
		}
		orderService.deleteOrder(id);
		return new ResponseEntity<>(HttpStatus.OK);
	}
}
