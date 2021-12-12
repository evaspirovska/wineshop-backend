package com.systems.integrated.wineshopbackend.repository;

import com.systems.integrated.wineshopbackend.models.users.Postman;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface PostmanJPARepository extends JpaRepository<Postman, Long> {
}
