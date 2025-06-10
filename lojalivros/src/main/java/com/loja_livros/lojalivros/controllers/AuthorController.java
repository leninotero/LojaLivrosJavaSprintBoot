package com.loja_livros.lojalivros.controllers;

import com.loja_livros.lojalivros.dtos.AuthorRecordDto;
import com.loja_livros.lojalivros.models.AuthorModel;
import com.loja_livros.lojalivros.services.AuthorService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

@RestController
@RequestMapping("/bookstore/authors")
public class AuthorController {

    private final AuthorService authorService;

    public AuthorController(AuthorService authorService) {
        this.authorService = authorService;
    }

    @GetMapping
    public ResponseEntity<List<AuthorModel>> getAllAuthors(){
        return ResponseEntity.status(HttpStatus.OK).body(authorService.getAllAuthors());
    }

    @GetMapping("/{id}")
    public ResponseEntity<Object> getUniqueAuthor(@PathVariable UUID id){
        Optional<AuthorModel> author =authorService.getOneAuthor(id);
        if (author.isEmpty()){
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Author not found!");
        }
        return ResponseEntity.status(HttpStatus.OK).body(authorService.getOneAuthor(id));
    }

    //swagger
    @Operation(summary = "Cadastro de Autores", method = "POST")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "201", description = "Autor Cadastrado"),
            @ApiResponse(responseCode = "400", description = "Nome de autor não pode ser nulo"),
            @ApiResponse(responseCode = "409", description = "Autor já existente")
    })
    //API
    @PostMapping
    public ResponseEntity<?> saveAuthor(@RequestBody @Validated AuthorRecordDto authorRecordDto) {
        if (authorRecordDto.name() == null || authorRecordDto.name().isEmpty()){
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Nome é obrigatório"); // HTTP 400
        }
        try {
            return ResponseEntity.status(HttpStatus.CREATED).body(authorService.saveAuthor(authorRecordDto)); // HTTP 201
        }catch (Exception e){
            return ResponseEntity.status(HttpStatus.CONFLICT).body("Author already exist"); // HTTP 409
        }
    }

    @PutMapping("/{id}")
    public ResponseEntity<?> atualizarAutor(@PathVariable UUID id, @RequestBody AuthorRecordDto authorRecordDto) {
        try {
            return ResponseEntity.status(HttpStatus.OK).body(authorService.updateAuthor(id, authorRecordDto));
        } catch (RuntimeException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(e.getMessage()); // HTTP 404
        }
    }

    //swagger
    @Operation(summary = "Remoção de Autores", method = "DELETE")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "204", description = "Not Content"),
            @ApiResponse(responseCode = "404", description = "Autor não encontrado")
    })
    //API
    @DeleteMapping("/{id}")
    public ResponseEntity<String> deleteAuthor(@PathVariable UUID id){
        Optional<AuthorModel> author = authorService.getOneAuthor(id);
        if (author.isEmpty()){
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Author not found!");
        }
        authorService.deleteAuthor(author.get().getId());
        return ResponseEntity.status(HttpStatus.NO_CONTENT).build(); //status(HttpStatus.OK).body("Author deleted successfully.");
    }
}
