package com.loja_livros.lojalivros.services;

import com.loja_livros.lojalivros.dtos.AuthorRecordDto;
import com.loja_livros.lojalivros.models.AuthorModel;
import com.loja_livros.lojalivros.repositories.AuthorRepository;
import com.loja_livros.lojalivros.repositories.BookRepository;
import jakarta.transaction.Transactional;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Service
public class AuthorService {
    private final BookRepository bookRepository;
    private final AuthorRepository authorRepository;

    public AuthorService(BookRepository bookRepository, AuthorRepository authorRepository) {
        this.bookRepository = bookRepository;
        this.authorRepository = authorRepository;
    }

    public List<AuthorModel> getAllAuthors(){
        return authorRepository.findAll();
    }

    public Optional<AuthorModel> getOneAuthor(UUID id){
        return authorRepository.findById(id);
    }

    @Transactional
    public AuthorModel saveAuthor(AuthorRecordDto authorRecordDto) {
        AuthorModel author = new AuthorModel();

//        if (authorRepository.existsById(author.getId())){
//            throw new Exception("Author already exists");
//        }

        author.setName(authorRecordDto.name());
        author.setNationality(authorRecordDto.nationality());
        author.setBirthDate(authorRecordDto.birthDate());

        return authorRepository.save(author);
    }

    @Transactional
    public void deleteAuthor(UUID id){
        authorRepository.deleteById(id);
    }

}
