package com.wishlist.wishlists.service;

import com.wishlist.wishlists.domain.Item;
import com.wishlist.wishlists.domain.User;
import com.wishlist.wishlists.repository.ItemRepository;
import com.wishlist.wishlists.repository.UserRepository;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.util.List;

@Service
public class WishlistService {

    private ItemRepository itemRepository;
    private UserRepository userRepository;

    public WishlistService(ItemRepository itemRepository, UserRepository userRepository) {
        this.itemRepository = itemRepository;
        this.userRepository = userRepository;
    }

    public Flux<Item> getAllItems() {
        return itemRepository.findAll();
    }

    public Flux<User> getAllUsers() {
        return userRepository.findAll();
    }

    public Mono<Item> addItem(Item item) {
        return itemRepository.save(item);
    }

    public Mono<Item> getItemById(String id) {
        return itemRepository.findById(id);
    }

    public Mono<User> getUserById(String id) {
        return userRepository.findById(id);
    }

    public Mono<User> addToWishlist(String userID, String itemId) {
        var item = itemRepository.findById(itemId).block();
        return userRepository.findById(userID)
                .flatMap(user -> {
                    user.getWishlist().add(item);
                    return userRepository.save(user);
                });
    }

    public Flux<Item> getWishList(String userId) {
        return userRepository.findById(userId)
                .flatMapIterable(User::getWishlist);
    }

    public Flux<Item> findItemName(String name) {
        return itemRepository.findByItemName(name).log();
    }
    public Flux<Item> findItemNum(int num) {
        return itemRepository.findByItemNum(num).log();
    }
}
