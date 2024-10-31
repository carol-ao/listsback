package com.randomstuff.lists.repositories;

import com.randomstuff.lists.entities.Lista;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ListaRepository extends JpaRepository<Lista,Long> {
}
