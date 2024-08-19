package com.loja_livros.lojalivros.repositories;

import com.loja_livros.lojalivros.models.PublisherModel;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.UUID;

public interface PublisherRepository extends JpaRepository<PublisherModel, UUID> {
}
