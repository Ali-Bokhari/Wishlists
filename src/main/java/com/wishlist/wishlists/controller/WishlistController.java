package com.wishlist.wishlists.controller;

import com.wishlist.wishlists.domain.Item;
import com.wishlist.wishlists.domain.User;
import com.wishlist.wishlists.service.WishlistService;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.*;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.time.Duration;

@RestController
public class WishlistController {

    private WishlistService wishlistService;

    public WishlistController(WishlistService wishlistService) {
        this.wishlistService = wishlistService;
    }

    @GetMapping("/items")
    public Flux<Item> getAllItems(){
        return wishlistService.getAllItems().log();
    }

    @GetMapping("/users")
    public Flux<User> getAllUsers(){
        return wishlistService.getAllUsers().log();
    }

    @GetMapping("/items/{id}")
    public Mono<Item> getItemById(@PathVariable String id) {
        return wishlistService.getItemById(id).log();
    }

    @GetMapping("/users/{id}")
    public Mono<User> getUserById(@PathVariable String id) {
        return wishlistService.getUserById(id).log();
    }

    @PostMapping("/items")
    @ResponseStatus(HttpStatus.CREATED)
    public Mono<Item> addItem(@RequestBody Item item) {
        return wishlistService.addItem(item).log();
    }

    @PostMapping("users/{id}/add")
    public Mono<User> addToWishlist(@RequestBody String itemId, @PathVariable String id) {
        return wishlistService.addToWishlist(id, itemId).log();
    }

    @GetMapping("/users/{id}/wishlist")
    public Flux<Item> getWishList(@PathVariable String id) {
        return wishlistService.getWishList(id).log();
    }

    @GetMapping(value = "/stream", produces = MediaType.TEXT_EVENT_STREAM_VALUE)
    public Flux<Long> stream() {
        return Flux.interval(Duration.ofSeconds(1))
                .log();
    }
}