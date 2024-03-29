package com.systems.integrated.wineshopbackend.repository;

import com.systems.integrated.wineshopbackend.models.users.Postman;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface PostmanJPARepository extends JpaRepository<Postman, Long> {
    Optional<List<Postman>> findAllByCity(String city);

    Postman findPostmenByUser_Id(Long id);
}


