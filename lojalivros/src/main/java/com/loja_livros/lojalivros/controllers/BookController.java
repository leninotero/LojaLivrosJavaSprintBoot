package com.loja_livros.lojalivros.controllers;

import com.loja_livros.lojalivros.dtos.BookRecordDto;
import com.loja_livros.lojalivros.exceptions.BadRequestException;
import com.loja_livros.lojalivros.models.BookModel;
import com.loja_livros.lojalivros.services.BookService;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import com.loja_livros.lojalivros.exceptions.*;

import java.net.URI;
import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/api/bookstore/books")
@Tag(name = "books")
public class BookController {

    private final BookService bookService;

    public BookController(BookService bookservice) {
        this.bookService = bookservice;
    }

    @GetMapping
    public ResponseEntity<List<BookModel>> getAllBooks(){
        try{
            return ResponseEntity.ok(bookService.getAllBooks()); // HTTP 200
        } catch (Exception e) {
            return ResponseEntity.internalServerError().build(); // HTTP 500
        }
    }

    @GetMapping("{id}")
    public ResponseEntity<Object> getOneBook(@PathVariable UUID id){
        BookModel book = bookService.getOneBook(id);
        if (book == null){
            return ResponseEntity.notFound().build(); // HTTP 404
        }
        return ResponseEntity.ok(bookService.getOneBook(id)); // HTTP 200
    }

    @PostMapping
    public ResponseEntity<BookModel> saveBook(@RequestBody BookRecordDto bookRecordDto){
        if (bookRecordDto.title() == null || bookRecordDto.title().isEmpty()){
            throw new BadRequestException("Title of Book is Mandatory");
        }
        BookModel savedBook = bookService.saveBook(bookRecordDto);
        return ResponseEntity.created(URI.create("/api/bookstore/books/" + savedBook.getId())).body(savedBook);
    }

    @PutMapping("/{id}")
    public ResponseEntity<BookModel> updateBook(@PathVariable UUID id, @RequestBody BookRecordDto bookRecordDto){
        if (bookRecordDto.title() == null || bookRecordDto.title().isEmpty()){
            throw new BadRequestException("Title of Book is Mandatory");
        }
        BookModel existingBook = bookService.getOneBook(id);
        if (existingBook == null) {
            throw new ResourceNotFoundException("Book not found with id: " + id);
        }
        BookModel updatedBook = bookService.updateBook(id, bookRecordDto);
        return ResponseEntity.ok(updatedBook);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteBook(@PathVariable UUID id){
        BookModel book = bookService.getOneBook(id);
        if (book == null) {
            throw new ResourceNotFoundException("Book not found with id: " + id);
        }
        bookService.deleteBook(id);
        return ResponseEntity.noContent().build();
    }
}
