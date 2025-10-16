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

	public Author findById(int id) throws AuthorNotFoundException {

		Author author = authorRepository.findById(id)
				.orElseThrow(() -> new AuthorNotFoundException("Author with the id: " + id + "not found!"));

		return author;
	}

	// TODO public saveAuthor(Author author)

	// TODO public Author updateAuthor(Author author, int id)

	// DONE public List<Author> findAll()
	public List<Author> findAll() throws AuthorNotFoundException {
		List<Author> allAuthors = (List<Author>) authorRepository.findAll();

		if (allAuthors.isEmpty()) {
			throw new AuthorNotFoundException("Could not find any authors!");
		}

		return allAuthors;
	}

	// TODO public void deleteById(int id) throws AuthorNotFoundException

	// TODO public Set<Book> findBooksByAuthorId(int id)
}
