package no.hvl.dat152.rest.ws.controller.hateoas;

import org.springframework.hateoas.Link;
import org.springframework.stereotype.Component;

import no.hvl.dat152.rest.ws.controller.OrderController;
import no.hvl.dat152.rest.ws.exceptions.OrderNotFoundException;
import no.hvl.dat152.rest.ws.model.Order;
import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.*;

import java.util.Set;

@Component
public class OrderLinkAdder {
    public void addLinks(Set<Order> orders) throws OrderNotFoundException {
        for (Order order : orders) {
            addLinks(order);
        }
    }

    public void addLinks(Order order) throws OrderNotFoundException {
        Link link = linkTo(methodOn(OrderController.class)
                .getBorrowOrder(order.getId()))
                .withRel("Get_Order");

        Link link2 = linkTo(methodOn(OrderController.class)
                .updateOrder(order.getId(), order))
                .withRel("Update_Order");

        Link link3 = linkTo(methodOn(OrderController.class)
                .deleteBookOrder(order.getId()))
                .withRel("Delete_Order");

        order.add(link, link2, link3);
    }
}
