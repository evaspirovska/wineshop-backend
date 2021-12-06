package com.systems.integrated.wineshopbackend.models.users;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;

@Entity
@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class Postman {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;

    @OneToOne
    private User user;

    private String city;

    private Integer count;

//    ne znam dali count ke treba
    public Postman(User user, String city) {
        this.user = user;
        this.city = city;
        this.count = 0;
    }

    public void updateCount() {
        this.count++;
    }
}
