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

	@Autowired
	AuthorService authorService;

	@GetMapping("/authors")
	public ResponseEntity<List<Author>> getAllAuthors() {

		List<Author> authors = authorService.findAll();

		return new ResponseEntity<>(authors, HttpStatus.OK);
	}

	@GetMapping("/authors/{id}")
	public ResponseEntity<Author> getAuthor(@PathVariable Integer id) throws AuthorNotFoundException {

		Author author = authorService.findById(id);

		return new ResponseEntity<>(author, HttpStatus.OK);
	}

	@GetMapping("/authors/{id}/books")
	public ResponseEntity<Set<Book>> getBooksByAuthorId(@PathVariable Integer id) throws AuthorNotFoundException {

		Set<Book> authors = authorService.findBooksByAuthorId(id);

		return new ResponseEntity<>(authors, HttpStatus.OK);
	}

	@PostMapping("/authors")
	public ResponseEntity<Author> createAuthor(@RequestBody Author author) {
		Author saveAuthor = authorService.saveAuthor(author);

		return new ResponseEntity<>(saveAuthor, HttpStatus.CREATED);
	}

	@PutMapping("/authors/{id}")
	public ResponseEntity<Author> updateAuthor(@PathVariable Integer id, @RequestBody Author author)
			throws AuthorNotFoundException {

		Author updatedAuthor = authorService.updateAuthor(author, id);

		return new ResponseEntity<>(updatedAuthor, HttpStatus.OK);
	}
}
