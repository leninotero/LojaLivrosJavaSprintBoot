package com.loja_livros.lojalivros.controllers;

import com.loja_livros.lojalivros.dtos.AuthorRecordDto;
import com.loja_livros.lojalivros.models.AuthorModel;
import com.loja_livros.lojalivros.services.AuthorService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.crossstore.ChangeSetPersister;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.NoSuchElementException;
import java.util.Optional;
import java.util.UUID;

@RestController
@RequestMapping("/api/bookstore/authors")
public class AuthorController {

    @Autowired
    AuthorService authorService;

    @GetMapping
    public ResponseEntity<List<AuthorModel>> getAllAuthors(){
        if (authorService.getAllAuthors().isEmpty()) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(List.of());
        }
        return ResponseEntity.status(HttpStatus.OK).body(authorService.getAllAuthors());
    }

    @GetMapping("/{id}")
    public ResponseEntity<Object> getUniqueAuthor(@PathVariable UUID id){
        Optional<AuthorModel> author = authorService.getOneAuthor(id);
        if (author.isEmpty()){
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Author not found!");
        }
        return ResponseEntity.status(HttpStatus.OK).body(authorService.getOneAuthor(id));
    }

    @PostMapping
    public ResponseEntity<?> saveAuthor(@RequestBody @Validated AuthorRecordDto authorRecordDto) {
        try {
            return ResponseEntity.status(HttpStatus.CREATED).body(authorService.saveAuthor(authorRecordDto)); // HTTP 201
        } catch (IllegalAccessError e){
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(e.getMessage()); // HTTP 400
        } catch (RuntimeException e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Error saving author: " + e.getMessage()); // HTTP 500
        } catch (Exception e){
            return ResponseEntity.status(HttpStatus.CONFLICT).body("Author already exist"); // HTTP 409
        }
    }

    @PutMapping("/{id}")
    public ResponseEntity<?> atualizarAutor(@PathVariable UUID id, @RequestBody @Valid AuthorRecordDto authorRecordDto) {
        try {
            return ResponseEntity.status(HttpStatus.OK).body(authorService.updateAuthor(id, authorRecordDto));
        } catch (NoSuchElementException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(e.getMessage()); // HTTP 404
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Error updating author: " + e.getMessage()); // HTTP 500
        }
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<String> deleteAuthor(@PathVariable UUID id){
        Optional<AuthorModel> author = authorService.getOneAuthor(id);
        if (author.isEmpty()){
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Author not found!");
        }
        try {
            authorService.deleteAuthor(author.get().getId());
            return ResponseEntity.status(HttpStatus.NO_CONTENT).build(); //status(HttpStatus.OK).body("Author deleted successfully.");
        } catch (RuntimeException e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Error deleting author: " + e.getMessage()); // HTTP 500
        }
    }
}
