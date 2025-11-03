package org.technoready.dao;

import org.jdbi.v3.sqlobject.config.RegisterBeanMapper;
import org.jdbi.v3.sqlobject.customizer.Bind;
import org.jdbi.v3.sqlobject.customizer.BindBean;
import org.jdbi.v3.sqlobject.statement.GetGeneratedKeys;
import org.jdbi.v3.sqlobject.statement.SqlQuery;
import org.jdbi.v3.sqlobject.statement.SqlUpdate;
import org.technoready.entity.Item;

import java.math.BigDecimal;
import java.util.List;
import java.util.Optional;

@RegisterBeanMapper(Item.class)
public interface ItemDao {

    @SqlUpdate("INSERT INTO items (name, description, price, current_price, original_price, is_available, created_at, updated_at) " +
            "VALUES (:name, :description, :price, :currentPrice, :originalPrice, :available, :createdAt, :updatedAt)")
    @GetGeneratedKeys("id")
    long insert(@BindBean Item item);

    @SqlQuery("SELECT * FROM Items ORDER BY id")
    List<Item> findAll();

    @SqlQuery("SELECT * FROM items WHERE is_available = true ORDER BY created_at DESC")
    List<Item> findAllAvailable();

    @SqlQuery("SELECT * FROM Items WHERE name = :name")
    Optional<Item> findByName(@Bind("name") String name);

    @SqlQuery("SELECT * FROM Items WHERE id = :id")
    Optional<Item> findById(@Bind("id") Long id);

    @SqlQuery("SELECT EXISTS(SELECT 1 FROM Items WHERE id = :id)")
    boolean exists(@Bind("id") Long id);

    @SqlUpdate("UPDATE items SET name = :name, description = :description, price = :price, " +
            "current_price = :currentPrice, original_price = :originalPrice, " +
            "is_available = :available, updated_at = :updatedAt " +
            "WHERE id = :id")
    int update(@BindBean Item item);

    @SqlUpdate("UPDATE items SET current_price = :currentPrice, updated_at = CURRENT_TIMESTAMP " +
            "WHERE id = :itemId")
    int updateCurrentPrice(@Bind("itemId") Long itemId, @Bind("currentPrice") BigDecimal currentPrice);

    @SqlUpdate("UPDATE items SET is_available = :available, updated_at = CURRENT_TIMESTAMP " +
            "WHERE id = :itemId")
    int updateAvailability(@Bind("itemId") Long itemId, @Bind("available") Boolean available);

    @SqlUpdate("DELETE FROM Items WHERE id = :id")
    int delete(@Bind("id") Long id);

    @SqlQuery("SELECT COUNT(*) FROM offers WHERE item_id = :itemId")
    int countOffersByItemId(@Bind("itemId") Long itemId);

    @SqlQuery("SELECT COALESCE(MAX(offer_amount), 0) FROM offers WHERE item_id = :itemId AND status = 'PENDING'")
    BigDecimal getHighestOfferByItemId(@Bind("itemId") Long itemId);


}


