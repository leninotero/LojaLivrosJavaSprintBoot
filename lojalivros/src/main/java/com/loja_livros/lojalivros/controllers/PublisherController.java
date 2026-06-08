package com.loja_livros.lojalivros.controllers;

import com.loja_livros.lojalivros.dtos.PublisherRecordDto;
import com.loja_livros.lojalivros.models.PublisherModel;
import com.loja_livros.lojalivros.services.PublisherService;
import com.loja_livros.lojalivros.exceptions.*;
import jakarta.validation.Valid;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import com.loja_livros.lojalivros.exceptions.*;

import java.net.URI;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

@RestController
@RequestMapping("/api/bookstore/publishers")
public class PublisherController {

    private final PublisherService publisherService;

    public PublisherController(PublisherService publisherService) {
        this.publisherService = publisherService;
    }

    @GetMapping
    public ResponseEntity<List<PublisherModel>> listAllPublishers() {
        List<PublisherModel> publishers = publisherService.getAllPublishers();
        if (publishers.isEmpty()) {
            throw new ResourceNotFoundException("No publishers found");
        }
        return ResponseEntity.ok(publishers);
    }

    @GetMapping("/{id}")
    public ResponseEntity<PublisherModel> getOnePublisher(@PathVariable UUID id) {
        Optional<PublisherModel> publisher = publisherService.getOnePublisher(id);
        return ResponseEntity.ok(publisher
                .orElseThrow(() -> new ResourceNotFoundException("Publisher not found with id: " + id)));
    }

    @PostMapping
    public ResponseEntity<PublisherModel> savePublisher(@RequestBody PublisherRecordDto publisherRecordDto) {
        if (publisherRecordDto.name() == null || publisherRecordDto.name().isBlank()) {
            throw new BadRequestException("Publisher name is required");
        }
        PublisherModel savedPublisher = publisherService.savePublisher(publisherRecordDto);
        return ResponseEntity.created(URI.create("/api/bookstore/publishers/" + savedPublisher.getId()))
                .body(savedPublisher);
    }

    @PutMapping("/{id}")
    public ResponseEntity<PublisherModel> updatePublisher(@PathVariable UUID id, @RequestBody @Valid PublisherRecordDto publisherRecordDto) {
        Optional<PublisherModel> existingPublisher = publisherService.getOnePublisher(id);
        if (existingPublisher.isEmpty()) {
            throw new ResourceNotFoundException("Publisher not found with id: " + id);
        }
        PublisherModel updatedPublisher = publisherService.updatePublisher(id, publisherRecordDto);
        return ResponseEntity.ok(updatedPublisher);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<?> deletePublisher(@PathVariable UUID id) {
        Optional<PublisherModel> publisher = publisherService.getOnePublisher(id);
        if (publisher.isEmpty()) {
            throw new ResourceNotFoundException("Publisher not found with id: " + id);
        }
        publisherService.deletePublisher(id);
        return ResponseEntity.noContent().build();
    }
}
