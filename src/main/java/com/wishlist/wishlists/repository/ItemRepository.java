package com.wishlist.wishlists.repository;

import com.wishlist.wishlists.domain.Item;
import org.springframework.data.mongodb.repository.ReactiveMongoRepository;
import org.springframework.data.querydsl.ReactiveQuerydslPredicateExecutor;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

public interface ItemRepository extends ReactiveMongoRepository<Item, String> {
    public Flux<Item> findByItemNum(int itemNum);

    public Flux<Item> findByItemName(String itemName);
}
