package org.technoready.service;

import org.technoready.dto.request.CreateOfferRequest;
import org.technoready.entity.Offer;
import org.technoready.entity.OfferWithDetails;

import java.util.List;
import java.util.Optional;

/**
 * Offer Service Interface
 * @version 1.2.0
 */
public interface OfferService {

    /**
     * Retrieve all offers
     */
    List<Offer> getAllOffers();

    /**
     * Retrieve all offers with details (JOIN)
     */
    List<OfferWithDetails> getAllOffersWithDetails();

    /**
     * Find offer by ID
     */
    Optional<Offer> getOfferById(Long id);

    /**
     * Find offer by ID with details (JOIN)
     */
    Optional<OfferWithDetails> getOfferByIdWithDetails(Long id);

    /**
     * Get all offers for a specific item
     */
    List<Offer> getOffersByItemId(Long itemId);

    /**
     * Get all offers for a specific item with details (JOIN)
     */
    List<OfferWithDetails> getOffersByItemIdWithDetails(Long itemId);

    /**
     * Get all offers by a specific user
     */
    List<Offer> getOffersByUserId(Long userId);

    /**
     * Get all offers by a specific user with details (JOIN)
     */
    List<OfferWithDetails> getOffersByUserIdWithDetails(Long userId);

    /**
     * Get offers by item and status
     */
    List<Offer> getOffersByItemIdAndStatus(Long itemId, String status);

    /**
     * Get offers by user and status
     */
    List<Offer> getOffersByUserIdAndStatus(Long userId, String status);

    /**
     * Create a new offer
     * Automatically updates item's current price if offer is higher
     * Marks other pending offers as OUTBID
     */
    Offer createOffer(CreateOfferRequest request);

    /**
     * Accept an offer
     * Marks item as unavailable
     * Marks other pending offers as REJECTED
     */
    Offer acceptOffer(Long offerId);

    /**
     * Reject an offer
     */
    Offer rejectOffer(Long offerId);

    /**
     * Delete an offer
     */
    boolean deleteOffer(Long id);

    /**
     * Check if offer exists
     */
    boolean offerExists(Long id);
}
