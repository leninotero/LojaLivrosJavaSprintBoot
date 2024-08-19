package com.loja_livros.lojalivros.repositories;

import com.loja_livros.lojalivros.models.ReviewModel;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.UUID;

public interface ReviewRepository extends JpaRepository<ReviewModel, UUID> {
}
