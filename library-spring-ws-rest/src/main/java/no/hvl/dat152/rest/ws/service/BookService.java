/**
 * 
 */
package no.hvl.dat152.rest.ws.service;

import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import no.hvl.dat152.rest.ws.exceptions.BookNotFoundException;
import no.hvl.dat152.rest.ws.model.Author;
import no.hvl.dat152.rest.ws.model.Book;
import no.hvl.dat152.rest.ws.repository.BookRepository;

/**
 * @author tdoy
 */
@Service
public class BookService {

	@Autowired
	private BookRepository bookRepository;

	@Autowired
	private AuthorService authorService;

	public Book saveBook(Book book) {

		return bookRepository.save(book);

	}

	public List<Book> findAll() {

		return (List<Book>) bookRepository.findAll();

	}

	public Book findByISBN(String isbn) throws BookNotFoundException {

		Book book = null;
		try {
			book = bookRepository.findBookByISBN(isbn);
		} catch (Exception e) {
			throw new BookNotFoundException("Book with isbn = " + isbn + " not found!");
		}

		if (book == null)
			throw new BookNotFoundException("Book with isbn = " + isbn + " not found!");
		else
			return book;
	}

	public Book updateBook(Book bookDetails) throws BookNotFoundException {
		Book existing = bookRepository.findBookByISBN(bookDetails.getIsbn());

		if (existing == null) {
			throw new BookNotFoundException("Book with isbn = " + bookDetails.getIsbn() + " not found!");
		}

		existing.setTitle(bookDetails.getTitle());
		existing.setAuthors(bookDetails.getAuthors());

		return bookRepository.save(existing);
	}

	public void deleteByISBN(String isbn) throws BookNotFoundException {
		if (!bookRepository.existsByISBN(isbn)) {
			throw new BookNotFoundException("Book with isbn = " + isbn + " not found!");
		}
		bookRepository.deleteByISBN(isbn);
	}

	// public List<Author> getAuthorsOfBookByISBN(String isbn) {
	// return authorService.findAll().stream()
	// .filter(author -> author.getBooks().stream()
	// .anyMatch(book -> isbn.equals(book.getIsbn())))
	// .toList();
	// }

	@Transactional(readOnly = true)
	public List<Author> getAuthorsOfBookByISBN(String isbn) throws BookNotFoundException {
		Book book = bookRepository.findByIsbn(isbn)
				.orElseThrow(() -> new BookNotFoundException("Book with ISBN = " + isbn + " not found"));

		return new ArrayList<>(book.getAuthors());
	}

	// trying out springs "deleteByX"
	public void deleteById(long id) throws BookNotFoundException {
		if (!bookRepository.existById(id)) {
			throw new BookNotFoundException("Book with id = " + id + " not found!");
		}
		bookRepository.deleteById(id);
	}


	// TODO public List<Book> findAllPaginate(Pageable page)

	// TODO public void deleteById(long id) - kanskje ferdig?

	// TODO public void deleteByISBN(String isbn) - kanskje ferdig

}
