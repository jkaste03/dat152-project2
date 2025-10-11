/**
 * 
 */
package no.hvl.dat152.rest.ws.controller;

import java.util.List;
import java.util.Set;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import no.hvl.dat152.rest.ws.exceptions.AuthorNotFoundException;
import no.hvl.dat152.rest.ws.exceptions.UpdateAuthorFailedException;
import no.hvl.dat152.rest.ws.model.Author;
import no.hvl.dat152.rest.ws.model.Book;
import no.hvl.dat152.rest.ws.service.AuthorService;

/**
 * 
 */
@RestController
@PreAuthorize("hasRole('ADMIN')")
@RequestMapping("/elibrary/api/v1")
public class AuthorController {

	private final AuthorService authorService;

	public AuthorController(AuthorService authorService) {
		this.authorService = authorService;
	}

	@GetMapping("/authors")
	public ResponseEntity<Object> getAllAuthors() {
		List<Author> authors = authorService.findAll();

		if (authors.isEmpty()) {
			return new ResponseEntity<>(HttpStatus.NO_CONTENT);
		}
		return new ResponseEntity<>(authors, HttpStatus.OK);
	}

	@GetMapping("/authors/{id}")
	public ResponseEntity<Author> getAuthor(@PathVariable int id) throws AuthorNotFoundException {
		Author author = authorService.findById(id);

		if (author == null) {
			return new ResponseEntity<>(HttpStatus.NO_CONTENT);
		}
		return new ResponseEntity<>(author, HttpStatus.OK);
	}

	@GetMapping("authors/{id}/books")
	public ResponseEntity<Object> getBooksByAuthorId(@PathVariable int id) throws AuthorNotFoundException {
		Author author = authorService.findById(id);
		if (author == null) {
			return new ResponseEntity<>(HttpStatus.NO_CONTENT);
		}
		Set<Book> books = author.getBooks();

		return new ResponseEntity<>(books, HttpStatus.OK);
	}

	@PostMapping("/authors")
	public ResponseEntity<Author> createAuthor(@RequestBody Author author) {
		Author nAuthor = authorService.saveAuthor(author);
		return new ResponseEntity<>(nAuthor, HttpStatus.CREATED);
	}

	@PutMapping("/authors/{id}")
	public ResponseEntity<Author> updateAuthor(@PathVariable int id, @RequestBody Author author)
			throws AuthorNotFoundException, UpdateAuthorFailedException {
		Author uAuthor = authorService.updateAuthor(author, id);
		return new ResponseEntity<>(uAuthor, HttpStatus.OK);
	}

}
