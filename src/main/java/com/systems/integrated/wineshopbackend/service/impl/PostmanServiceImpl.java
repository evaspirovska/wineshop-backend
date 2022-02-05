package com.systems.integrated.wineshopbackend.service.impl;

import com.systems.integrated.wineshopbackend.models.users.Postman;
import com.systems.integrated.wineshopbackend.repository.PostmanJPARepository;
import com.systems.integrated.wineshopbackend.service.intef.PostmanService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
@Transactional
public class PostmanServiceImpl implements PostmanService {

    private final PostmanJPARepository postmanJPARepository;

    @Override
    public Postman findPostmanById(Long id) {
        return this.postmanJPARepository.findPostmenByUser_Id(id);
    }
}
