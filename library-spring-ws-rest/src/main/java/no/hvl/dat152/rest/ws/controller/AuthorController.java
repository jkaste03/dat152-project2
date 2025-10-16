/**
 * 
 */
package no.hvl.dat152.rest.ws.controller;

import java.util.List;
import java.util.Set;

import org.apache.catalina.connector.Response;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import no.hvl.dat152.rest.ws.exceptions.AuthorNotFoundException;
import no.hvl.dat152.rest.ws.model.Author;
import no.hvl.dat152.rest.ws.model.Book;
import no.hvl.dat152.rest.ws.service.AuthorService;
import no.hvl.dat152.rest.ws.repository.AuthorRepository;
import no.hvl.dat152.rest.ws.service.AuthorService;

/**
 * 
 */
@RestController
@RequestMapping("/elibrary/api/v1")
public class AuthorController {

	private final AuthorService authorService;

	public AuthorController(AuthorService authorService) {
		this.authorService = authorService;
	}

	// DONE - getAllAuthor (@Mappings, URI, and method)
	@GetMapping("/authors")
	public ResponseEntity<Object> getAllAuthors() throws AuthorNotFoundException {
		List<Author> allAuthors = authorService.findAll();

		if (allAuthors.isEmpty()) {
			return new ResponseEntity<>(HttpStatus.NO_CONTENT);
		}
		return new ResponseEntity<>(allAuthors, HttpStatus.OK);
	}

	// TODO - getAuthor (@Mappings, URI, and method)

	// TODO - getBooksByAuthorId (@Mappings, URI, and method)

	// TODO - createAuthor (@Mappings, URI, and method)
	@PostMapping("/authors")
	public ResponseEntity<Author> createAuthor(
			@RequestBody Author author) throws AuthorNotFoundException {
		authorService.saveAuthor(author);
		return new ResponseEntity<>(author, HttpStatus.CREATED);
	}

	// DONE - updateAuthor (@Mappings, URI, and method)
	@PutMapping(path = "/authors/{aid}")
	public ResponseEntity<Author> updateAuthor(
			@PathVariable int aid,
			@RequestBody Author author) throws AuthorNotFoundException {
		Author updated = authorService.updateAuthor(author, aid);
		return new ResponseEntity<>(updated, HttpStatus.OK);
	}
}
