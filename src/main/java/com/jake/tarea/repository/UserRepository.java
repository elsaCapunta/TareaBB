package com.jake.tarea.repository;

import com.jake.tarea.model.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface UserRepository extends JpaRepository<User,Long> {
    boolean existsByEmail(String email);

    Object findByNameIgnoreCase(String name);

    User findByEmail(String email);
}
