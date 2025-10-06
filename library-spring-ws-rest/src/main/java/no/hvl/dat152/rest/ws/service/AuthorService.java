/**
 * 
 */
package no.hvl.dat152.rest.ws.service;

import java.util.List;
import java.util.Set;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

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

	public Author saveAuthor(Author author) {
		return authorRepository.save(author);
	}

	public Author findById(int id) {
		Author author = null;
		try {
			author = authorRepository.findById(id)
					.orElseThrow(() -> new AuthorNotFoundException("Author with id " + id + " does not exist"));
		} catch (AuthorNotFoundException e) {
			e.printStackTrace();
		}
		return author;
	}

	// TODO public Author updateAuthor(Author author, int id)

	public List<Author> findAll() {
		return (List<Author>) authorRepository.findAll();
	}

	// TODO public void deleteById(Long id) throws AuthorNotFoundException

	// TODO public Set<Book> findBooksByAuthorId(Long id)
}
