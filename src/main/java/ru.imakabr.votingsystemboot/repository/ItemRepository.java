package ru.imakabr.votingsystemboot.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.transaction.annotation.Transactional;
import ru.imakabr.votingsystemboot.model.Item;

@Transactional(readOnly = true)
public interface ItemRepository extends JpaRepository<Item, Integer> {

    @Transactional
    @Modifying
    @Query("DELETE FROM Item i WHERE i.id=:id")
    int delete(@Param("id") int id);

}
