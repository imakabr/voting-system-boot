package ru.imakabr.votingsystemboot.model;

import org.hibernate.Hibernate;
import org.hibernate.annotations.BatchSize;

import javax.persistence.*;
import javax.validation.constraints.NotNull;
import java.time.LocalDate;

@Entity
@Table(name = "votes")
@Access(AccessType.FIELD)
public class Vote {
    public static final int START_SEQ = 100000;

    @Id
    @SequenceGenerator(name = "VOTE_SEQ", sequenceName = "VOTE_SEQ", allocationSize = 1, initialValue = START_SEQ)
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "VOTE_SEQ")
    protected Integer id;

    @Column(name = "date_time", nullable = false)
    @NotNull
    private LocalDate date;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(
            name = "user_id")
    @BatchSize(size = 200)
    protected User user;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(
            name = "rest_id")
    @BatchSize(size = 200)
    protected Restaurant restaurant;

    public Vote() {
    }

    public Vote(Integer id, User user, Restaurant restaurant, LocalDate date) {
        this.id = id;
        this.user = user;
        this.restaurant = restaurant;
        this.date = date;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public Integer getId() {
        return id;
    }

    public boolean isNew() {
        return this.id == null;
    }

    public LocalDate getDate() {
        return date;
    }

    public void setDate(LocalDate date) {
        this.date = date;
    }

    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }

    public Restaurant getRestaurant() {
        return restaurant;
    }

    public void setRestaurant(Restaurant restaurant) {
        this.restaurant = restaurant;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || !getClass().equals(Hibernate.getClass(o))) {
            return false;
        }
        Vote that = (Vote) o;
        return id != null && id.equals(that.id);
    }

    @Override
    public int hashCode() {
        return id == null ? 0 : id;
    }

    @Override
    public String toString() {
        return "Vote{" +
                "id=" + getId() +
                ", date=" + getDate() +
                '}';
    }
}
