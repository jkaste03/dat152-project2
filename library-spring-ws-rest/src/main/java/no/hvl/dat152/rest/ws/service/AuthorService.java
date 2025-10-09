/**
 * 
 */
package no.hvl.dat152.rest.ws.service;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Optional;
import java.util.Set;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.server.ResponseStatusException;

import jakarta.persistence.EntityNotFoundException;
import no.hvl.dat152.rest.ws.exceptions.AuthorNotFoundException;
import no.hvl.dat152.rest.ws.model.Author;
import no.hvl.dat152.rest.ws.model.Book;
import no.hvl.dat152.rest.ws.repository.AuthorRepository;

/**
 * @author tdoy
 */
@Service
public class AuthorService {

	@Autowired
	private AuthorRepository authorRepository;

	public Author findById(Integer id) throws AuthorNotFoundException {

		Author author = authorRepository.findById(id)
				.orElseThrow(() -> new AuthorNotFoundException("Author with the id: " + id + "not found!"));

		return author;
	}

	public List<Author> findAll() {
		return (List<Author>) authorRepository.findAll();
	}

	public Author saveAuthor(Author author) {
		return authorRepository.save(author);
	}

	@Transactional
	public Author updateAuthor(Author author) throws Exception {
		Optional<Author> optionalAuthor = authorRepository.findById(author.getAuthorId());

		if (optionalAuthor.isEmpty()) {
			throw new Exception("Author with id = " + author.getAuthorId() + " not found!");
		}

		Author existingAuthor = optionalAuthor.get();
		existingAuthor.setFirstname(author.getFirstname());
		existingAuthor.setLastname(author.getLastname());
		existingAuthor.setBooks(author.getBooks());

		return authorRepository.save(existingAuthor);
	}

	@Transactional
	public void deleteById(Integer id) {
		Author author = authorRepository.findById(id)
				.orElseThrow(() -> new EntityNotFoundException("Author with id = " + id + " not found"));

		var booksCopy = new java.util.HashSet<>(author.getBooks());
		for (var book : booksCopy) {
			book.getAuthors().remove(author);
		}
		author.getBooks().clear();

		authorRepository.delete(author);
	}

	@Transactional(readOnly = true)
	public List<Book> getBooksByAuthorId(Integer id) throws Exception {
		Author author = authorRepository.findById(id)
				.orElseThrow(() -> new Exception("Author with id = " + id + " not found"));

		return new ArrayList<>(author.getBooks());
	}

	@Transactional(readOnly = true)
	public Set<Book> findBooksByAuthorId(Integer id) {
			Author author = authorRepository.findById(id)
					.orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Author not found"));
			return new HashSet<>(author.getBooks());
	}
	// Kan også ha dette i bookrepository:
  // Set<Book> findDistinctByAuthors_AuthorId(int authorId);

}
