/**
 * 
 */
package no.hvl.dat152.rest.ws.service;

import java.util.List;
import java.util.Set;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import no.hvl.dat152.rest.ws.exceptions.BookNotFoundException;
import no.hvl.dat152.rest.ws.exceptions.UpdateBookFailedException;
import no.hvl.dat152.rest.ws.model.Author;
import no.hvl.dat152.rest.ws.model.Book;
import no.hvl.dat152.rest.ws.repository.AuthorRepository;
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
	
	public List<Book> findAll(){
		
		return (List<Book>) bookRepository.findAll();
		
	}
	
	
	public Book findByISBN(String isbn) throws BookNotFoundException {
		
		Book book = bookRepository.findByIsbn(isbn)
				.orElseThrow(() -> new BookNotFoundException("Book with isbn = "+isbn+" not found!"));
		
		return book;
	}
	
	// DONE public Book updateBook(Book book, String isbn)
	public Book updateBook(Book book, String isbn) throws BookNotFoundException {
		Book eBook = bookRepository.findBookByISBN(isbn);
		if (eBook == null) {
			throw new BookNotFoundException("Book with isbn = " + isbn + " not found!");
		}
		eBook.setAuthors(book.getAuthors());
		eBook.setIsbn(isbn);
		eBook.setTitle(book.getTitle());
		eBook.setId(book.getId());
		return bookRepository.save(eBook);
	}
	
	// DONE public List<Book> findAllPaginate(Pageable page)
	public List<Book> findAllPaginate(Pageable page) {
		Page<Book> fPage = bookRepository.findAll(page);

		return fPage.getContent();
	}
	
	// DONE public Set<Author> findAuthorsOfBookByISBN(String isbn)
	Set<Author> findAuthorsOfBookByISBN(String isbn) throws BookNotFoundException {
		Book eBook = bookRepository.findBookByISBN(isbn);
		if (eBook == null) {
			throw new BookNotFoundException("Book with isbn = " + isbn + " not found!");
		}
		return eBook.getAuthors();
	}
	// DONE public void deleteById(long id)
	public void deleteById(long id) throws BookNotFoundException {
		Book eBook = bookRepository.findById(id)
			.orElseThrow(() -> new BookNotFoundException("Book with id = " + id + " not found!"));
		bookRepository.delete(eBook);
	}
	
	// DONE public void deleteByISBN(String isbn) 
	public void deleteByISBN(String isbn) throws BookNotFoundException {
		Book eBook = bookRepository.findBookByISBN(isbn);
		if (eBook == null) {
			throw new BookNotFoundException("Book with isbn = " + isbn + " not found!");
		}
		bookRepository.delete(eBook);
	}
	
}
