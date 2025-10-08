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
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import no.hvl.dat152.rest.ws.model.Author;
import no.hvl.dat152.rest.ws.model.Book;
import no.hvl.dat152.rest.ws.service.AuthorService;

/**
 * 
 */
@RestController
@RequestMapping("/elibrary/api/v1")
public class AuthorController {

	@Autowired
	private AuthorService authorService;

	@GetMapping("/authors")
	public ResponseEntity<Object> getAllAuthors() {

		List<Author> authors = authorService.findAll();

		if (authors.isEmpty())
			return new ResponseEntity<>(HttpStatus.NO_CONTENT);

		return new ResponseEntity<>(authors, HttpStatus.OK);
	}

	@GetMapping("/authors/{id}")
	public ResponseEntity<Author> getAuthorById(@PathVariable Integer id) {
		try {
			Author author = authorService.findById(id);
			return new ResponseEntity<>(author, HttpStatus.OK);
		} catch (Exception e) {
			return new ResponseEntity<>(HttpStatus.NOT_FOUND);
		}
	}

	@DeleteMapping("/authors/{id}")
	public ResponseEntity<Void> deleteAuthor(@PathVariable Integer id) {
		try {
			authorService.deleteById(id);
			return new ResponseEntity<>(HttpStatus.NO_CONTENT);
		} catch (Exception e) {
			return new ResponseEntity<>(HttpStatus.NOT_FOUND);
		}
	}

	@PutMapping("/authors/{id}")
	public ResponseEntity<Author> updateAuthor(
			@PathVariable Integer id,
			@RequestBody Author author) {
		try {
			author.setAuthorId(id);
			Author updated = authorService.updateAuthor(author);
			return new ResponseEntity<>(updated, HttpStatus.OK);
		} catch (Exception e) {
			return new ResponseEntity<>(HttpStatus.NOT_FOUND);
		}
	}

	@PostMapping("/authors")
	public ResponseEntity<Author> createAuthor(@RequestBody Author author) {

		Author author2 = authorService.saveAuthor(author);

		return new ResponseEntity<>(author2, HttpStatus.CREATED);
	}

	@GetMapping("/authors/{id}/books")
	public ResponseEntity<List<Book>> getBooksByAuthorId(@PathVariable Integer id) {
		try {
			List<Book> books = authorService.getBooksByAuthorId(id);
			return new ResponseEntity<>(books, HttpStatus.OK);
		} catch (Exception e) {
			return new ResponseEntity<>(HttpStatus.NOT_FOUND);
		}
	}
}
