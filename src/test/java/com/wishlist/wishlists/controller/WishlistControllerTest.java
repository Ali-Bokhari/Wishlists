package com.wishlist.wishlists.controller;

import com.wishlist.wishlists.domain.Item;
import com.wishlist.wishlists.domain.User;
import com.wishlist.wishlists.repository.ItemRepository;
import com.wishlist.wishlists.repository.UserRepository;
import com.wishlist.wishlists.service.WishlistService;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.reactive.AutoConfigureWebTestClient;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.web.reactive.server.WebTestClient;
import reactor.core.publisher.Flux;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@AutoConfigureWebTestClient
class WishlistControllerTest {

    @Autowired
    WebTestClient webTestClient;

    @Autowired
    WishlistService wishlistService;

    @Autowired
    private ItemRepository itemRepository;
    @Autowired
    private UserRepository userRepository;

    /*public WishlistControllerTest(ItemRepository itemRepository, UserRepository userRepository) {
        this.itemRepository = itemRepository;
        this.userRepository = userRepository;
    }*/

    @BeforeEach
    void before() {
        User user = new User("44", "Bilal", "Bola");
        User user1 = new User("54", "John", "Thompson");
        User user2 = new User("57", "Kiran", "Velensky");
        userRepository.save(user).block();
        userRepository.save(user1).block();
        userRepository.save(user2).block();

        Item item = new Item("98", "Toothbrush", 4326);
        Item item1 = new Item("142", "Projector", 9978);
        Item item2 = new Item("143", "Book", 9979);
        Item item3 = new Item("144", "Pencil", 9980);
        Item item4 = new Item("145", "Marker", 9981);
        itemRepository.save(item).block();
        itemRepository.save(item1).block();
        itemRepository.save(item2).block();
        itemRepository.save(item3).block();
        itemRepository.save(item4).block();

        user2.getWishlist().add(item2);
        user2.getWishlist().add(item3);
        user2.getWishlist().add(item4);

        userRepository.save(user2).block();

//        user.getWishlist().add(item1);
//        userRepository.save(user);
    }

    @AfterEach
    void tearDown() {
        userRepository.deleteAll().block();
        itemRepository.deleteAll().block();
    }

    @Test
    void getAllItems() {
        webTestClient
                .get()
                .uri("/items")
                .exchange()
                .expectBodyList(Item.class)
                .hasSize(5);
    }

    @Test
    void getAllUsers() {
        webTestClient
                .get()
                .uri("/users")
                .exchange()
                .expectBodyList(User.class)
                .hasSize(3);
    }

    @Test
    void addToWishlist() {
        var userId = "44";
        webTestClient
                .post()
                .uri("/users/{id}/add", userId)
                .bodyValue("98")
                .exchange()
                .expectStatus()
                .is2xxSuccessful()
                .expectBody(User.class)
                .consumeWith(entityExchangeResult -> {
                    assert entityExchangeResult.getResponseBody().getWishlist().size() != 0;
                });
        getAllUsers();
    }

    @Test
    void getItemById() {
        webTestClient
                .get()
                .uri("/items/{id}", "142")
                .exchange()
                .expectBody()
                .jsonPath("itemNum")
                .isEqualTo(9978);
    }

    @Test
    void getUserById() {
        webTestClient
                .get()
                .uri("/users/{id}", "54")
                .exchange()
                .expectBody()
                .jsonPath("firstName")
                .isEqualTo("John");
    }

    @Test
    void addItem() {
        var item = new Item(null, "Eraser", 7722);

        webTestClient
                .post()
                .uri("/items")
                .bodyValue(item)
                .exchange()
                .expectStatus()
                .isCreated()
                .expectBody(Item.class)
                .consumeWith(ritem -> {
                    assert ritem.getResponseBody().getId() != null;
                });
    }

    @Test
    void getWishList() {
        webTestClient
                .get()
                .uri("/users/{id}/wishlist", "57")
                .exchange()
                .expectStatus()
                .is2xxSuccessful()
                .expectBodyList(Item.class)
                .hasSize(3);
    }

    @Test
    void findItemName() {
        var u = wishlistService.findItemName("Projector").blockLast();
        assert u.getItemNum() == 9978;
    }

    @Test
    void findItemNum() {
        var u = wishlistService.findItemNum(9978).blockLast();
        assert u.getItemName().equals("Projector");
    }
}