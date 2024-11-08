package com.randomstuff.lists.repositories;

import com.randomstuff.lists.entities.Role;
import com.randomstuff.lists.projections.UserDetailsProjection;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface RoleRepository extends JpaRepository<Role, Long> {

    @Query(nativeQuery = true, value = """
            select tb_user.email as username,tb_user.password, tb_role.id as roleId, tb_role.authority from
              tb_user INNER JOIN
              tb_user_role ON tb_user.id = tb_user_role.user_id INNER JOIN
              tb_role ON tb_role.id = tb_user_role.role_id WHERE
              tb_user.email = :email
           """)
    List<UserDetailsProjection> searchUserAndRolesByEmail(String email);

}
