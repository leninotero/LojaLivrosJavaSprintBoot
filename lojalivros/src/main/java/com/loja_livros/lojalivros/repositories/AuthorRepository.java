package com.loja_livros.lojalivros.repositories;

import com.loja_livros.lojalivros.models.AuthorModel;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.UUID;

public interface AuthorRepository extends JpaRepository<AuthorModel, UUID> {
}
