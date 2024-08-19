package com.loja_livros.lojalivros.dtos;

import java.util.Set;
import java.util.UUID;

public record PublisherRecordDto(String name, String country, Set<UUID> booksIds) {
}
