package com.loja_livros.lojalivros.repositories;

import com.loja_livros.lojalivros.models.BookModel;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.UUID;

public interface BookRepository extends JpaRepository<BookModel, UUID> {

//    BookModel findBookModelByTitle(String title);

//    @Query(value = "SELEC * FROM tb_livro WHERE editora_id = :id", nativeQuery = true)
//    List<LivroModel> findLivrosByEditoraId(@Param("id") UUID id);

}
