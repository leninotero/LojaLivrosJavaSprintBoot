package com.loja_livros.lojalivros.services;

import com.loja_livros.lojalivros.dtos.BookRecordDto;
import com.loja_livros.lojalivros.models.BookModel;
import com.loja_livros.lojalivros.models.ReviewModel;
import com.loja_livros.lojalivros.repositories.AuthorRepository;
import com.loja_livros.lojalivros.repositories.PublisherRepository;
import com.loja_livros.lojalivros.repositories.BookRepository;
import jakarta.transaction.Transactional;
import org.springframework.stereotype.Service;

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

    @Transactional
    public BookModel saveBook(BookRecordDto bookRecordDto){
        BookModel book = new BookModel();
        book.setTitle(bookRecordDto.title());
        book.setPublisherYear(bookRecordDto.publisherYear());
        book.setPublisher(publisherRepository.findById(bookRecordDto.publisherId()).get());
        book.setAuthors(authorRepository.findAllById(bookRecordDto.authorIds()).stream().collect(Collectors.toSet()));

        ReviewModel resumoModel = new ReviewModel(); //instancia criada para relacionar um livro a um resumo
        resumoModel.setComment(bookRecordDto.reviewComment()); //aqui vai ser setado o resumo do livro na classe resumo
        resumoModel.setBook(book); //aqui vai ser setado o as infos do livro na classe resumo
        book.setReview(resumoModel); //aqui vai ser setado o resumo da classe resumo no livro

        return bookRepository.save(book);
    }

//    @Transactional
//    public BookModel updateBook(UUID id, BookRecordDto bookRecordDto){
//
//    }

    @Transactional
    public void deleteBook(UUID id){
        bookRepository.deleteById(id);
    }
}
