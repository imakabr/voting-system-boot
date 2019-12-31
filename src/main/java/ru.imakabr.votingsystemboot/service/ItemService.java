package ru.imakabr.votingsystemboot.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.stereotype.Service;
import org.springframework.util.Assert;
import ru.imakabr.votingsystemboot.model.Item;
import ru.imakabr.votingsystemboot.repository.ItemRepository;
import ru.imakabr.votingsystemboot.repository.RestaurantRepository;
import ru.imakabr.votingsystemboot.util.ValidationUtil;


import java.util.List;

import static ru.imakabr.votingsystemboot.util.ValidationUtil.*;


@Service
public class ItemService {

    private ItemRepository itemRepository;

    private RestaurantRepository restaurantRepository;

    @Autowired
    public ItemService(ItemRepository repository, RestaurantRepository restaurantRepository) {
        this.itemRepository = repository;
        this.restaurantRepository = restaurantRepository;
    }

    @CacheEvict(value = {"allRestaurantsWithItemsByDate", "oneRestaurantWithItemsByDate"}, allEntries = true)
    public Item create(Item item, int restaurantId) {
        Assert.notNull(item, "item must not be null");
        item.setRestaurant(restaurantRepository.getOne(restaurantId));
        return itemRepository.save(item);
    }

    @CacheEvict(value = {"allRestaurantsWithItemsByDate", "oneRestaurantWithItemsByDate"}, allEntries = true)
    public void delete(int id) {
        checkNotFoundWithId(itemRepository.delete(id) != 0, id);
    }

    public Item get(int id) {
        return checkNotFoundWithId(itemRepository.findById(id).orElse(null), id);
    }

    public List<Item> getAll() {
        return itemRepository.findAll();
    }

    @CacheEvict(value = {"allRestaurantsWithItemsByDate", "oneRestaurantWithItemsByDate"}, allEntries = true)
    public void update(Item item, int restaurantId, int itemId) {
        Assert.notNull(item, "item must not be null");
        ValidationUtil.assureItemIdConsistent(item, itemId);
        item.setRestaurant(restaurantRepository.getOne(restaurantId));
        checkNotFoundWithId(itemRepository.save(item), item.getId());
    }

}
