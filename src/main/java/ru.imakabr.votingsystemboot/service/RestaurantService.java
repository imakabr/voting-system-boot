package ru.imakabr.votingsystemboot.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.util.Assert;
import ru.imakabr.votingsystemboot.model.Restaurant;
import ru.imakabr.votingsystemboot.repository.RestaurantRepository;


import java.time.LocalDate;
import java.util.List;

import static ru.imakabr.votingsystemboot.util.ValidationUtil.*;


@Service
public class RestaurantService {

    private static final Sort SORT_NAME = Sort.by(Sort.Direction.ASC, "name");

    private final RestaurantRepository repository;

    @Autowired
    public RestaurantService(RestaurantRepository repository) {
        this.repository = repository;
    }

    public Restaurant create(Restaurant restaurant) {
        Assert.notNull(restaurant, "restaurant must not be null");
        return repository.save(restaurant);
    }

    public void delete(int id) {
        checkNotFoundWithId(repository.delete(id) != 0, id);
    }

    public Restaurant get(int id) {
        return checkNotFoundWithId(repository.findById(id).orElse(null), id);
    }

    public List<Restaurant> getAll() {
        return repository.findAll(SORT_NAME);
    }

    public void update(Restaurant restaurant, int id) {
        Assert.notNull(restaurant, "restaurant must not be null");
        checkNotFoundWithId(repository.save(restaurant), restaurant.getId());
    }

    @Cacheable("allRestaurantsWithItemsByDate")
    public List<Restaurant> getAllWithItemsByDate(LocalDate date) {
        return repository.getAllWithItemsByDate(date);
    }

    public Restaurant getWithItems(int id) {
        return checkNotFoundWithId(repository.getWithItems(id), id);
    }

    @Cacheable("oneRestaurantWithItemsByDate")
    public Restaurant getWithItemsByDate(int id, LocalDate date) {
        return checkNotFoundWithId(repository.getWithItemsByDate(id, date), id);
    }

}
