package com.randomstuff.lists.repositories;

import com.randomstuff.lists.entities.Role;
import org.springframework.data.jpa.repository.JpaRepository;

public interface RoleRepository extends JpaRepository<Role, Long> {
}
