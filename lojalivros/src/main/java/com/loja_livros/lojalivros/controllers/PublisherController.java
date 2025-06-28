package com.loja_livros.lojalivros.controllers;

import com.loja_livros.lojalivros.dtos.PublisherRecordDto;
import com.loja_livros.lojalivros.models.PublisherModel;
import com.loja_livros.lojalivros.services.PublisherService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.NoSuchElementException;
import java.util.Optional;
import java.util.UUID;

@RestController
@RequestMapping("/api/bookstore/publishers")
public class PublisherController {

    @Autowired
    PublisherService publisherService;

    @GetMapping
    public ResponseEntity<List<PublisherModel>> listAllPublishers() {
        if (publisherService.getAllPublishers().isEmpty()) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(List.of());
        }
        return ResponseEntity.status(HttpStatus.OK).body(publisherService.getAllPublishers());
    }

    @GetMapping("/{id}")
    public ResponseEntity<Object> getOnePublisher(@PathVariable UUID id) {
        Optional<PublisherModel> publisher = publisherService.getOnePublisher(id);
        return publisher.<ResponseEntity<Object>>map(
                publisherModel -> ResponseEntity.status(HttpStatus.OK)
                        .body(publisherModel))
                        .orElseGet(() -> ResponseEntity.status(HttpStatus.NOT_FOUND).body("Publisher not found!"));
    }

    @PostMapping
    public ResponseEntity<?> savePublisher(@RequestBody PublisherRecordDto publisherRecordDto) {

        try {
            return ResponseEntity.status(HttpStatus.CREATED).body(publisherService.savePublisher(publisherRecordDto));
        } catch (IllegalArgumentException e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(e.getMessage());
        } catch (RuntimeException e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Error saving publisher: " + e.getMessage());
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.CONFLICT).body("Publisher already exists or error occurred: ");
        }
    }

    @PutMapping("/{id}")
    public ResponseEntity<?> updatePublisher(@PathVariable UUID id, @RequestBody @Valid PublisherRecordDto publisherRecordDto) {
        try {
            return ResponseEntity.status(HttpStatus.OK).body(publisherService.updatePublisher(id, publisherRecordDto));
        } catch (NoSuchElementException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Publisher not found with ID: " + id);
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Error updating publisher: " + e.getMessage());
        }
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<?> deletePublisher(@PathVariable UUID id) {
        try {
            publisherService.deletePublisher(id);
            return ResponseEntity.status(HttpStatus.NO_CONTENT).build(); // HTTP 204
        } catch (RuntimeException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Publisher not found with ID: " + id); // HTTP 404
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Error deleting publisher: " + e.getMessage()); // HTTP 500
        }
    }
}
