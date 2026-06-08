package com.loja_livros.lojalivros.controllers;

import com.loja_livros.lojalivros.dtos.AuthorRecordDto;
import com.loja_livros.lojalivros.models.AuthorModel;
import com.loja_livros.lojalivros.services.AuthorService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import jakarta.validation.Valid;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import com.loja_livros.lojalivros.exceptions.*;

import java.net.URI;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

@RestController
@RequestMapping("/api/bookstore/authors")
public class AuthorController {

    private final AuthorService authorService;

    public AuthorController(AuthorService authorService) {
        this.authorService = authorService;
    }

    //Swagger
    @Operation(summary = "List cadaster Users")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Authors list"),
            @ApiResponse(responseCode = "404", description = "Authors list not found")
    })
    @GetMapping
    public ResponseEntity<List<AuthorModel>> getAllAuthors(){
        if (authorService.getAllAuthors().isEmpty()) {
            return ResponseEntity.notFound().build();
        }
        return ResponseEntity.ok(authorService.getAllAuthors());
    }

    //Swagger
    @Operation(summary = "Find Author by Id")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Author found"),
            @ApiResponse(responseCode = "404", description = "Author not found")
    })
    @GetMapping("/{id}")
    public ResponseEntity<Object> getUniqueAuthor(@PathVariable UUID id){
        Optional<AuthorModel> author = authorService.getOneAuthor(id);
        if (author.isEmpty()){
            return ResponseEntity.notFound().build();
        }
        return ResponseEntity.ok(authorService.getOneAuthor(id));
    }

    //swagger
    @Operation(summary = "Register os Authors", method = "POST")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "201", description = "Author registration"),
            @ApiResponse(responseCode = "400", description = "Invalid data"),
            @ApiResponse(responseCode = "409", description = "Autor already exists"),
            @ApiResponse(responseCode = "500", description = "Service inavalible")
    })
    @PostMapping
    public ResponseEntity<AuthorModel> saveAuthor(@RequestBody @Validated AuthorRecordDto authorRecordDto) {
        if (authorRecordDto.name() == null || authorRecordDto.name().isBlank()) {
            throw new BadRequestException("Author name is required");
        }
        AuthorModel savedAuthor = authorService.saveAuthor(authorRecordDto);
        return ResponseEntity.created(URI.create("/api/bookstore/authors/" + savedAuthor.getId())).body(savedAuthor);
    }

    //Swagger
    @Operation(summary = "Update Author by Id", method = "PUT")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Author updated"),
            @ApiResponse(responseCode = "400", description = "Invalid data"),
            @ApiResponse(responseCode = "404", description = "Author not found"),
            @ApiResponse(responseCode = "500", description = "Service inavalible")
    })
    @PutMapping("/{id}")
    public ResponseEntity<AuthorModel> atualizarAutor(@PathVariable UUID id, @RequestBody @Valid AuthorRecordDto authorRecordDto) {
        if (authorRecordDto.name() == null || authorRecordDto.name().isBlank()) {
            throw new BadRequestException("Author name is required");
        }
        AuthorModel updatedAuthor = authorService.updateAuthor(id, authorRecordDto);
        if (updatedAuthor == null) {
            throw new ResourceNotFoundException("Author not found with id: " + id);
        }
        return ResponseEntity.ok(updatedAuthor);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteAuthor(@PathVariable UUID id){
        Optional<AuthorModel> author = authorService.getOneAuthor(id);
        if (author.isEmpty()){
            throw new ResourceNotFoundException("Author not found with id: " + id);
        }
        authorService.deleteAuthor(author.get().getId());
        return ResponseEntity.noContent().build();
    }
}
