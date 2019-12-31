package ru.imakabr.votingsystemboot.web.item;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;
import ru.imakabr.votingsystemboot.model.Item;
import ru.imakabr.votingsystemboot.model.Restaurant;
import ru.imakabr.votingsystemboot.service.ItemService;
import ru.imakabr.votingsystemboot.service.RestaurantService;


import javax.validation.Valid;
import java.net.URI;
import java.time.LocalDate;

@RestController
@RequestMapping(value = AdminItemRestController.REST_URL, produces = MediaType.APPLICATION_JSON_VALUE)
public class AdminItemRestController {

    public static final String REST_URL = "/rest/admin/restaurants";
    protected final Logger log = LoggerFactory.getLogger(getClass());

    @Autowired
    protected ItemService itemService;

    @Autowired
    protected RestaurantService restaurantService;

    @GetMapping("items/{id}")
    public Item get(@PathVariable int id) {
        return itemService.get(id);
    }

    @PostMapping(value = "/{restaurantId}/items", consumes = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<Item> create(@Valid @RequestBody Item item, @PathVariable int restaurantId) {
        log.info("create {}", item);
        Item created = itemService.create(item, restaurantId);
        URI uriOfNewResource = ServletUriComponentsBuilder.fromCurrentContextPath()
                .path(REST_URL + "/{id}")
                .buildAndExpand(created.getId()).toUri();
        return ResponseEntity.created(uriOfNewResource).body(created);
    }

    @PutMapping(value = "/{restaurantId}/items/{id}", consumes = MediaType.APPLICATION_JSON_VALUE)
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void update(@Valid @RequestBody Item item, @PathVariable int restaurantId, @PathVariable int id) {
        log.info("update {}", item);
        itemService.update(item, restaurantId, id);
    }

    @DeleteMapping("items/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void delete(@PathVariable int id) {
        log.info("delete item = " + id);
        itemService.delete(id);
    }

    @GetMapping("/{id}/items")
    public Restaurant getAllByRestaurantId(@PathVariable int id) {
        return restaurantService.getWithItems(id);
    }

    @GetMapping("/{id}/items/filter")
    public Restaurant getAllByRestaurantIdAndDate(@PathVariable int id, @RequestParam(required = false) LocalDate date) {
        return restaurantService.getWithItemsByDate(id, date);
    }

}