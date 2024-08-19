package com.loja_livros.lojalivros.controllers;

import com.loja_livros.lojalivros.dtos.BookRecordDto;
import com.loja_livros.lojalivros.models.BookModel;
import com.loja_livros.lojalivros.services.BookService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.apache.coyote.Response;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.sql.PreparedStatement;
import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/bookstore/books")
@Tag(name = "books")
public class BookController {

    private final BookService bookService;

    public BookController(BookService bookservice) {
        this.bookService = bookservice;
    }

    @Operation(summary = "Consulta de livros", method = "GET")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Lista de usuarios")
    })
    @GetMapping
    public ResponseEntity<List<BookModel>> getAllBooks(){
        return ResponseEntity.status(HttpStatus.OK).body(bookService.getAllBooks());
    }

    //swagger
    @Operation(summary = "Cadastro de livros", method = "POST")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "201", description = "Livro Cadastrado"),
            @ApiResponse(responseCode = "400", description = "Informações nulas ou vazias"),
            @ApiResponse(responseCode = "409", description = "Livro existente")
    })
    //API
    @PostMapping
    public ResponseEntity<?> saveBook(@RequestBody BookRecordDto bookRecordDto){
        if (bookRecordDto.title() == null || bookRecordDto.title().isEmpty()){
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("TItulo do Livro é obrigatório"); // HTTO 400
        }
        try {
            return ResponseEntity.status(HttpStatus.CREATED).body(bookService.saveBook(bookRecordDto));
        } catch (Exception e){
            return ResponseEntity.status(HttpStatus.CONFLICT).body(e.getMessage()); // HTTP 409
        }
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<String> deleteBook(@PathVariable UUID id){
        bookService.deleteBook(id);
        return ResponseEntity.status(HttpStatus.NO_CONTENT).build(); //status(HttpStatus.OK).body("Book deleted successfully.");
    }

}
