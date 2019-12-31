package ru.imakabr.votingsystemboot.model;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonManagedReference;
import org.hibernate.annotations.BatchSize;
import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;

import javax.persistence.*;
import java.util.List;


@Cache(usage = CacheConcurrencyStrategy.NONSTRICT_READ_WRITE)
@NamedQueries(
        @NamedQuery(name = Restaurant.DELETE, query = "DELETE FROM Restaurant r WHERE r.id=:id")
)
@Entity
@Table(name = "restaurants")
public class Restaurant extends AbstractNamedEntity {

    public static final String DELETE = "Restaurant.delete";

    @Cache(usage = CacheConcurrencyStrategy.NONSTRICT_READ_WRITE)
    @OneToMany(mappedBy = "restaurant", fetch = FetchType.LAZY)
    @BatchSize(size = 200)
    @JsonManagedReference
//    @JsonIgnore
    protected List<Item> items;

    @OneToMany(mappedBy = "restaurant", fetch = FetchType.LAZY)
    @JsonIgnore
    protected List<Vote> votes;

    public void setItems(List<Item> items) {
        this.items = items;
    }

    public List<Item> getItems() {
        return items;
    }

    public List<Vote> getVotes() {
        return votes;
    }

    public Restaurant() {
    }

    public Restaurant(Integer id, String name) {
        super(id, name);
    }

    public Restaurant(Restaurant restaurant) {
        super(restaurant.id, restaurant.name);
    }

    public Restaurant(Integer id, String name, List<Item> items) {
        super(id, name);
        this.items = items;
    }


}
