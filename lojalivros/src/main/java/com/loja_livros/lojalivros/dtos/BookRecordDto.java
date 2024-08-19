package com.loja_livros.lojalivros.dtos;

import java.util.Set;
import java.util.UUID;

public record BookRecordDto(String title, int publisherYear, UUID publisherId, Set<UUID> authorIds, String reviewComment) {
}
