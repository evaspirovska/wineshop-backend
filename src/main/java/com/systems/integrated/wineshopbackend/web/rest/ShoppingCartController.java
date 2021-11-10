package com.systems.integrated.wineshopbackend.web.rest;

import com.systems.integrated.wineshopbackend.models.exceptions.EntityNotFoundException;
import com.systems.integrated.wineshopbackend.models.orders.DTO.ProductInShoppingCartDTO;
import com.systems.integrated.wineshopbackend.models.orders.ProductInShoppingCart;
import com.systems.integrated.wineshopbackend.models.orders.ShoppingCart;
import com.systems.integrated.wineshopbackend.service.intef.ShoppingCartService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/shoppingCart")
@CrossOrigin(value = "*")
public class ShoppingCartController {

    private final ShoppingCartService shoppingCartService;

    @GetMapping
    public ResponseEntity<?> getShoppingCart(HttpServletRequest req) {

        ShoppingCart shoppingCart;
        String username = req.getRemoteUser();
        try {
            shoppingCart = this.shoppingCartService.getShoppingCart(username);
        }
        catch (UsernameNotFoundException ex) {
            return new ResponseEntity<>(ex.getMessage(), HttpStatus.NOT_FOUND);
        }
        return new ResponseEntity<>(shoppingCart, HttpStatus.OK);
    }

    @DeleteMapping("/delete/{id}")
    public ResponseEntity<?> deleteProductFromShoppingCart(@PathVariable Long id, HttpServletRequest req) {

        String username = req.getRemoteUser();
        try {
            shoppingCartService.deleteProductFromShoppingCart(id, username);
        }
        catch (UsernameNotFoundException | EntityNotFoundException ex) {
            return new ResponseEntity<>(ex.getMessage(), HttpStatus.NOT_FOUND);
        }
        return new ResponseEntity<>("Product in shopping cart with id " + id + " deleted.", HttpStatus.OK);
    }

    @PostMapping("/addToShoppingCart")
    public ResponseEntity<?> addProductToShoppingCart(@RequestBody ProductInShoppingCartDTO productInShoppingCartDTO,
                                                      HttpServletRequest req) {

        String username = req.getRemoteUser();
        ShoppingCart shoppingCart;
        try{
            shoppingCart = shoppingCartService.addProductToShoppingCart(productInShoppingCartDTO, username);
        }
        catch (UsernameNotFoundException | EntityNotFoundException ex) {
            return new ResponseEntity<>(ex.getMessage(), HttpStatus.NOT_FOUND);
        }
        return new ResponseEntity<>(shoppingCart, HttpStatus.OK);
    }

}
