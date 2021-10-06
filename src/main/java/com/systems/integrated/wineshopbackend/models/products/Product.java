package com.systems.integrated.wineshopbackend.models.products;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;

/*
 * TODO: Marko ova smisli go ti kako da bide, dali product da bide entitet ili samo nekoja abstraktna klasa bidejki
 *  Wine, Glass i site drugi proizvodi ke treba da nasleduvaat od ovaa klasa
 */
@Entity
@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
@Table(name = "Products")
public class Product {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
}
