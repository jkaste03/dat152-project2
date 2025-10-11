/**
 * 
 */
package no.hvl.dat152.rest.ws.service;

import java.util.List;
import java.util.Set;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataAccessException;
import org.springframework.stereotype.Service;

import no.hvl.dat152.rest.ws.exceptions.AuthorNotFoundException;
import no.hvl.dat152.rest.ws.exceptions.UpdateAuthorFailedException;
import no.hvl.dat152.rest.ws.exceptions.UpdateBookFailedException;
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

	public Author saveAuthor(Author author) {
		return authorRepository.save(author);
	}

	public Author findById(int id) throws AuthorNotFoundException {
		Author author = authorRepository.findById(id)
				.orElseThrow(() -> new AuthorNotFoundException("Author with id " + id + " does not exist"));
		return author;
	}

	public Author updateAuthor(Author author, int id)
			throws UpdateAuthorFailedException, AuthorNotFoundException {
		findById(id);
		if (id != author.getAuthorId()) {
			throw new UpdateAuthorFailedException(
					"Id mismatch between provided author (" + author.getAuthorId() + ") and provided id (" + id + ")");
		}
		try {
			return authorRepository.save(author);
		} catch (DataAccessException e) {
			throw new UpdateAuthorFailedException(
					"Failed to update author with id " + id + ": " + e.getMessage(), e);
		}
	}

	public List<Author> findAll() {
		return (List<Author>) authorRepository.findAll();
	}

	public void deleteById(int id) throws AuthorNotFoundException {
		Author author = findById(id);
		authorRepository.delete(author);
	}

	public Set<Book> findBooksByAuthorId(int id) throws AuthorNotFoundException {
		Author author = findById(id);
		return author.getBooks();
	}
}
