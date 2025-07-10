package com.loja_livros.lojalivros.services;

import com.loja_livros.lojalivros.dtos.BookRecordDto;
import com.loja_livros.lojalivros.models.AuthorModel;
import com.loja_livros.lojalivros.models.BookModel;
import com.loja_livros.lojalivros.models.ReviewModel;
import com.loja_livros.lojalivros.repositories.AuthorRepository;
import com.loja_livros.lojalivros.repositories.PublisherRepository;
import com.loja_livros.lojalivros.repositories.BookRepository;
import jakarta.transaction.Transactional;
import org.springframework.stereotype.Service;

import java.util.HashSet;
import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

@Service
public class BookService {
    private final BookRepository bookRepository;
    private final AuthorRepository authorRepository;
    private final PublisherRepository publisherRepository;

    public BookService(BookRepository bookRepository, AuthorRepository authorRepository, PublisherRepository publisherRepository) {
        this.bookRepository = bookRepository;
        this.authorRepository = authorRepository;
        this.publisherRepository = publisherRepository;
    }

    public List<BookModel> getAllBooks(){
        return bookRepository.findAll();
    }

    public BookModel getOneBook(UUID id){
        return bookRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("Book not found with id: " + id));
    }

    @Transactional
    public BookModel saveBook(BookRecordDto bookRecordDto){
        BookModel book = new BookModel();

        book.setTitle(bookRecordDto.title());
        book.setPublisherYear(bookRecordDto.publisherYear());

        var publisher = publisherRepository.findById(bookRecordDto.publisherId())
                .orElseThrow(() -> new IllegalArgumentException("Publisher not found with id: " + bookRecordDto.publisherId()));
        book.setPublisher(publisher);

        var authors = authorRepository.findAllById(bookRecordDto.authorIds());
        if (authors.size() != bookRecordDto.authorIds().size()) {
            throw new IllegalArgumentException("One or more authors not found with the provided IDs.");
        }
        book.setAuthors(new HashSet<>(authors));

        ReviewModel reviewModel = new ReviewModel(); //instancia criada para relacionar um livro a um review
        reviewModel.setComment(bookRecordDto.reviewComment()); //aqui vai ser setado o resumo do livro na classe review
        reviewModel.setBook(book); //aqui vai ser setado o id do livro na classe review
        book.setReview(reviewModel); //aqui vai ser setado o resumo da classe resumo no livro

        try{
            return bookRepository.save(book);
        } catch (Exception e) {
            throw new RuntimeException("Error saving book: " + e.getMessage());
        }
    }

    @Transactional
    public BookModel updateBook(UUID id, BookRecordDto bookRecordDto) {
        BookModel book = bookRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("Book not found with id: " + id));

        book.setTitle(bookRecordDto.title());
        book.setPublisherYear(bookRecordDto.publisherYear());

        var publisher = publisherRepository.findById(bookRecordDto.publisherId())
                .orElseThrow(() -> new IllegalArgumentException("Publisher not found with id: " + bookRecordDto.publisherId()));
        book.setPublisher(publisher);

        var authors = authorRepository.findAllById(bookRecordDto.authorIds());
        if (authors.size() != bookRecordDto.authorIds().size()) {
            throw new IllegalArgumentException("One or more authors not found with the provided IDs.");
        }
        book.setAuthors(new HashSet<>(authors));

        ReviewModel reviewModel = new ReviewModel();
        reviewModel.setComment(bookRecordDto.reviewComment());
        reviewModel.setBook(book);
        book.setReview(reviewModel);

        try {
            return bookRepository.save(book);
        } catch (Exception e) {
            throw new RuntimeException("Error updating book: " + e.getMessage());
        }
    }


    @Transactional
    public void deleteBook(UUID id){
        try {
            bookRepository.deleteById(id);
        } catch (Exception e) {
            throw new RuntimeException("Error deleting book: " + e.getMessage());
        }
    }
}
