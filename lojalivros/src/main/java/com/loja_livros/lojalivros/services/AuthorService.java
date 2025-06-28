package com.loja_livros.lojalivros.services;

import com.loja_livros.lojalivros.dtos.AuthorRecordDto;
import com.loja_livros.lojalivros.models.AuthorModel;
import com.loja_livros.lojalivros.repositories.AuthorRepository;
import com.loja_livros.lojalivros.repositories.BookRepository;
import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Service
public class AuthorService {
    @Autowired
     BookRepository bookRepository;

    @Autowired
    AuthorRepository authorRepository;


    public List<AuthorModel> getAllAuthors(){
        return authorRepository.findAll();
    }

    public Optional<AuthorModel> getOneAuthor(UUID id){
        return authorRepository.findById(id);
    }

    @Transactional
    public AuthorModel saveAuthor(AuthorRecordDto authorRecordDto) {
        AuthorModel author = new AuthorModel();
       
        author.setName(authorRecordDto.name());
        author.setNationality(authorRecordDto.nationality());
        author.setBirthDate(authorRecordDto.birthDate());

        return authorRepository.save(author);
    }

    @Transactional
    public AuthorModel updateAuthor(UUID id, AuthorRecordDto authorRecordDto) {
        Optional<AuthorModel> optionalAuthor = authorRepository.findById(id);

        if (optionalAuthor.isPresent()) {
            AuthorModel author = optionalAuthor.get();
            author.setName(authorRecordDto.name());
            author.setNationality(authorRecordDto.nationality());
            author.setBirthDate(authorRecordDto.birthDate());
            return authorRepository.save(author);
        } else {
            throw new RuntimeException("Author not found with id: " + id);
        }
    }

    @Transactional
    public void deleteAuthor(UUID id){
        authorRepository.deleteById(id);
    }

}
