package com.loja_livros.lojalivros.dtos;

import java.util.Date;
import java.util.Set;
import java.util.UUID;

public record AuthorRecordDto(String name, String nationality, Date birthDate) {
}
