package com.wishlist.wishlists.repository;

import com.wishlist.wishlists.domain.User;
import org.springframework.data.mongodb.repository.ReactiveMongoRepository;

public interface UserRepository extends ReactiveMongoRepository<User, String> {
}
