package org.technoready.dao;


import org.jdbi.v3.sqlobject.config.RegisterBeanMapper;
import org.jdbi.v3.sqlobject.customizer.Bind;
import org.jdbi.v3.sqlobject.customizer.BindBean;
import org.jdbi.v3.sqlobject.statement.GetGeneratedKeys;
import org.jdbi.v3.sqlobject.statement.SqlQuery;
import org.jdbi.v3.sqlobject.statement.SqlUpdate;
import org.technoready.entity.Offer;
import org.technoready.entity.OfferWithDetails;

import java.util.List;
import java.util.Optional;

/**
 * Offer Data Access Object Interface
 * @version 1.2.0
 */
@RegisterBeanMapper(Offer.class)
public interface OfferDao {

    @SqlQuery("SELECT * FROM offers ORDER BY created_at DESC")
    List<Offer> findAll();

    @SqlQuery("SELECT * FROM offers WHERE id = :id")
    Optional<Offer> findById(@Bind("id") Long id);

    @SqlQuery("SELECT * FROM offers WHERE item_id = :itemId ORDER BY created_at DESC")
    List<Offer> findByItemId(@Bind("itemId") Long itemId);

    @SqlQuery("SELECT * FROM offers WHERE user_id = :userId ORDER BY created_at DESC")
    List<Offer> findByUserId(@Bind("userId") Long userId);

    @SqlQuery("SELECT * FROM offers WHERE item_id = :itemId AND status = :status ORDER BY created_at DESC")
    List<Offer> findByItemIdAndStatus(@Bind("itemId") Long itemId, @Bind("status") String status);

    @SqlQuery("SELECT * FROM offers WHERE user_id = :userId AND status = :status ORDER BY created_at DESC")
    List<Offer> findByUserIdAndStatus(@Bind("userId") Long userId, @Bind("status") String status);

    // JOIN query to get offer with user and item details
    @SqlQuery("SELECT " +
            "o.id, o.item_id, o.user_id, o.offer_amount, o.status, o.message, o.created_at, o.updated_at, " +
            "u.username, u.email as user_email, u.full_name as user_full_name, " +
            "i.name as item_name, i.description as item_description, " +
            "i.current_price as item_current_price, i.original_price as item_original_price, " +
            "i.available as item_available " +
            "FROM offers o " +
            "INNER JOIN users u ON o.user_id = u.id " +
            "INNER JOIN items i ON o.item_id = i.id " +
            "ORDER BY o.created_at DESC")
    @RegisterBeanMapper(OfferWithDetails.class)
    List<OfferWithDetails> findAllWithDetails();

    @SqlQuery("SELECT " +
            "o.id, o.item_id, o.user_id, o.offer_amount, o.status, o.message, o.created_at, o.updated_at, " +
            "u.username, u.email as user_email, u.full_name as user_full_name, " +
            "i.name as item_name, i.description as item_description, " +
            "i.current_price as item_current_price, i.original_price as item_original_price, " +
            "i.available as item_available " +
            "FROM offers o " +
            "INNER JOIN users u ON o.user_id = u.id " +
            "INNER JOIN items i ON o.item_id = i.id " +
            "WHERE o.id = :id")
    @RegisterBeanMapper(OfferWithDetails.class)
    Optional<OfferWithDetails> findByIdWithDetails(@Bind("id") Long id);

    @SqlQuery("SELECT " +
            "o.id, o.item_id, o.user_id, o.offer_amount, o.status, o.message, o.created_at, o.updated_at, " +
            "u.username, u.email as user_email, u.full_name as user_full_name, " +
            "i.name as item_name, i.description as item_description, " +
            "i.current_price as item_current_price, i.original_price as item_original_price, " +
            "i.available as item_available " +
            "FROM offers o " +
            "INNER JOIN users u ON o.user_id = u.id " +
            "INNER JOIN items i ON o.item_id = i.id " +
            "WHERE o.item_id = :itemId " +
            "ORDER BY o.created_at DESC")
    @RegisterBeanMapper(OfferWithDetails.class)
    List<OfferWithDetails> findByItemIdWithDetails(@Bind("itemId") Long itemId);

    @SqlQuery("SELECT " +
            "o.id, o.item_id, o.user_id, o.offer_amount, o.status, o.message, o.created_at, o.updated_at, " +
            "u.username, u.email as user_email, u.full_name as user_full_name, " +
            "i.name as item_name, i.description as item_description, " +
            "i.current_price as item_current_price, i.original_price as item_original_price, " +
            "i.available as item_available " +
            "FROM offers o " +
            "INNER JOIN users u ON o.user_id = u.id " +
            "INNER JOIN items i ON o.item_id = i.id " +
            "WHERE o.user_id = :userId " +
            "ORDER BY o.created_at DESC")
    @RegisterBeanMapper(OfferWithDetails.class)
    List<OfferWithDetails> findByUserIdWithDetails(@Bind("userId") Long userId);

    @SqlUpdate("INSERT INTO offers (item_id, user_id, offer_amount, status, message, created_at, updated_at) " +
            "VALUES (:itemId, :userId, :offerAmount, :status, :message, :createdAt, :updatedAt)")
    @GetGeneratedKeys("id")
    long insert(@BindBean Offer offer);

    @SqlUpdate("UPDATE offers SET status = :status, updated_at = :updatedAt WHERE id = :id")
    int updateStatus(@Bind("id") Long id, @Bind("status") String status, @Bind("updatedAt") java.time.LocalDateTime updatedAt);

    @SqlUpdate("UPDATE offers SET status = :status, updated_at = CURRENT_TIMESTAMP " +
            "WHERE item_id = :itemId AND status = 'PENDING' AND id != :excludeOfferId")
    int markOthersAsOutbid(@Bind("itemId") Long itemId, @Bind("excludeOfferId") Long excludeOfferId, @Bind("status") String status);

    @SqlUpdate("DELETE FROM offers WHERE id = :id")
    int delete(@Bind("id") Long id);

    @SqlQuery("SELECT EXISTS(SELECT 1 FROM offers WHERE id = :id)")
    boolean exists(@Bind("id") Long id);
}
