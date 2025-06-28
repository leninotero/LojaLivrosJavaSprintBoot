package com.loja_livros.lojalivros.services;

import com.loja_livros.lojalivros.dtos.PublisherRecordDto;
import com.loja_livros.lojalivros.models.BookModel;
import com.loja_livros.lojalivros.models.PublisherModel;
import com.loja_livros.lojalivros.repositories.BookRepository;
import com.loja_livros.lojalivros.repositories.PublisherRepository;
import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.awt.print.Book;
import java.util.HashSet;
import java.util.List;
import java.util.Optional;
import java.util.UUID;
import java.util.stream.Collectors;

@Service
public class PublisherService {

    @Autowired
    BookRepository bookRepository;

    @Autowired
    PublisherRepository publisherRepository;

    public List<PublisherModel> getAllPublishers() {
        return publisherRepository.findAll();
    }

    public Optional<PublisherModel> getOnePublisher(UUID id) {
        return publisherRepository.findById(id);
    }

    @Transactional
    public PublisherModel savePublisher(PublisherRecordDto publisherRecordDto) {
        PublisherModel publisher = new PublisherModel();

        publisher.setName(publisherRecordDto.name());
        publisher.setCountry(publisherRecordDto.country());

        return publisherRepository.save(publisher);
    }

    @Transactional
    public PublisherModel updatePublisher(UUID id, PublisherRecordDto publisherRecordDto) {
        Optional<PublisherModel> optionalPublisher = publisherRepository.findById(id);

        if (optionalPublisher.isPresent()) {
            PublisherModel publisher = optionalPublisher.get();
            publisher.setName(publisherRecordDto.name());
            publisher.setCountry(publisherRecordDto.country());
            return publisherRepository.save(publisher);
        } else {
            throw new RuntimeException("Publisher not found with id: " + id);
        }
    }

    @Transactional
    public void deletePublisher(UUID id) {
        publisherRepository.deleteById(id);
    }

}
