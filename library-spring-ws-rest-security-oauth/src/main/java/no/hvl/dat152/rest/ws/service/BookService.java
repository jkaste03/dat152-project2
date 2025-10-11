/**
 * 
 */
package no.hvl.dat152.rest.ws.service;

import java.util.List;
import java.util.Set;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataAccessException;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import no.hvl.dat152.rest.ws.exceptions.BookNotFoundException;
import no.hvl.dat152.rest.ws.exceptions.UpdateBookFailedException;
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

	public void deleteByISBN(String isbn) throws BookNotFoundException {
		bookRepository.delete(findByISBN(isbn));
	}

	public Book updateBook(Book book, String isbn) throws BookNotFoundException, UpdateBookFailedException {
		findByISBN(isbn);
		if (!isbn.equals(book.getIsbn())) {
			throw new UpdateBookFailedException(
					"ISBN mismatch between provided book (" + book.getIsbn() + ") and provided isbn (" + isbn + ")");
		}
		try {
			return bookRepository.save(book);
		} catch (DataAccessException e) {
			throw new UpdateBookFailedException(
					"Failed to update book with ISBN " + isbn + ": " + e.getMessage(), e);
		}
	}

	public List<Book> findAllPaginate(Pageable page) {
		return bookRepository.findAllPaginate(page.getPageSize(), (int) page.getOffset());
	}

	public Set<Author> findAuthorsOfBookByISBN(String isbn) throws BookNotFoundException {
		Book book = findByISBN(isbn);
		return book.getAuthors();
	}

	public void deleteById(long id) {
		Book book = null;
		try {
			book = bookRepository.findById(id)
					.orElseThrow(() -> new BookNotFoundException("Book with id " + id + " does not exist"));
		} catch (BookNotFoundException e) {
			e.printStackTrace();
		}
		bookRepository.delete(book);
	}

}
