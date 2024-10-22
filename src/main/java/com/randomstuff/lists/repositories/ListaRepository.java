package com.randomstuff.lists.repositories;

import com.randomstuff.lists.model.Lista;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ListaRepository extends JpaRepository<Lista,Long> {
}
