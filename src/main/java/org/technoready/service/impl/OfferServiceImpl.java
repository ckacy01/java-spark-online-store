package org.technoready.service.impl;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.jdbi.v3.core.Jdbi;
import org.technoready.dao.ItemDao;
import org.technoready.dao.OfferDao;
import org.technoready.dao.UserDao;
import org.technoready.dto.request.CreateOfferRequest;
import org.technoready.entity.Item;
import org.technoready.entity.Offer;
import org.technoready.entity.OfferWithDetails;
import org.technoready.entity.User;
import org.technoready.exception.ConflictException;
import org.technoready.exception.NotFoundException;
import org.technoready.service.OfferService;
import org.technoready.util.OfferMapper;
import org.technoready.web.WebSocketHandler;

import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;

@Slf4j
@RequiredArgsConstructor
public class OfferServiceImpl implements OfferService {
    private final Jdbi jdbi;

    @Override
    public List<Offer> getAllOffers() {
        log.debug("Fetching all offers");
        return jdbi.withExtension(OfferDao.class, OfferDao::findAll);
    }

    @Override
    public List<OfferWithDetails> getAllOffersWithDetails() {
        log.debug("Fetching all offers with details");
        return jdbi.withExtension(OfferDao.class, OfferDao::findAllWithDetails);
    }

    @Override
    public Optional<Offer> getOfferById(Long id) {
        log.debug("Fetching offer with id: {}", id);
        return jdbi.withExtension(OfferDao.class, dao -> dao.findById(id));
    }

    @Override
    public Optional<OfferWithDetails> getOfferByIdWithDetails(Long id) {
        log.debug("Fetching offer with details for id: {}", id);
        return jdbi.withExtension(OfferDao.class, dao -> dao.findByIdWithDetails(id));
    }

    @Override
    public List<Offer> getOffersByItemId(Long itemId) {
        log.debug("Fetching offers for item: {}", itemId);
        return jdbi.withExtension(OfferDao.class, dao -> dao.findByItemId(itemId));
    }

    @Override
    public List<OfferWithDetails> getOffersByItemIdWithDetails(Long itemId) {
        log.debug("Fetching offers with details for item: {}", itemId);
        return jdbi.withExtension(OfferDao.class, dao -> dao.findByItemIdWithDetails(itemId));
    }

    @Override
    public List<Offer> getOffersByUserId(Long userId) {
        log.debug("Fetching offers for user: {}", userId);
        return jdbi.withExtension(OfferDao.class, dao -> dao.findByUserId(userId));
    }

    @Override
    public List<OfferWithDetails> getOffersByUserIdWithDetails(Long userId) {
        log.debug("Fetching offers with details for user: {}", userId);
        return jdbi.withExtension(OfferDao.class, dao -> dao.findByUserIdWithDetails(userId));
    }

    @Override
    public List<Offer> getOffersByItemIdAndStatus(Long itemId, String status) {
        log.debug("Fetching offers for item {} with status {}", itemId, status);
        return jdbi.withExtension(OfferDao.class, dao -> dao.findByItemIdAndStatus(itemId, status));
    }

    @Override
    public List<Offer> getOffersByUserIdAndStatus(Long userId, String status) {
        log.debug("Fetching offers for user {} with status {}", userId, status);
        return jdbi.withExtension(OfferDao.class, dao -> dao.findByUserIdAndStatus(userId, status));
    }

    @Override
    public Offer createOffer(CreateOfferRequest request) {
        log.info("Creating new offer for item {} by user {}", request.getItemId(), request.getUserId());

        request.validate();

        return jdbi.inTransaction(handle -> {
            ItemDao itemDao = handle.attach(ItemDao.class);
            UserDao userDao = handle.attach(UserDao.class);
            OfferDao offerDao = handle.attach(OfferDao.class);

            Item item = itemDao.findById(request.getItemId())
                    .orElseThrow(() -> new IllegalArgumentException("Item not found with id: " + request.getItemId()));

            if (!item.isAvailable()) {
                throw new IllegalArgumentException("Item is no longer available for offers");
            }

            User user = userDao.findById(request.getUserId())
                    .orElseThrow(() -> new IllegalArgumentException("User not found with id: " + request.getUserId()));

            // Create offer
            Offer offer = OfferMapper.toEntity(request);
            long generatedId = offerDao.insert(offer);
            offer.setId(generatedId);

            // Update item's current price if this offer is higher
            if (request.getOfferAmount().compareTo(item.getCurrentPrice()) > 0) {

                List<Offer> offersToOutbid = offerDao.findByItemIdAndStatus(item.getId(), "PENDING");

                itemDao.updateCurrentPrice(item.getId(), request.getOfferAmount());
                log.info("Updated item {} current price to {}", item.getId(), request.getOfferAmount());

                // Mark other pending offers for this item as OUTBID
                int outbidCount = offerDao.markOthersAsOutbid(item.getId(), generatedId, "OUTBID");
                log.info("Marked {} offers as OUTBID for item {}", outbidCount, item.getId());

                // ========== WEBSOCKET BROADCASTS ==========

                // 1. Broadcast new offer to all watchers
                Map<String, Object> offerData = new HashMap<>();
                offerData.put("id", generatedId);
                offerData.put("offerAmount", request.getOfferAmount().doubleValue());
                offerData.put("username", user.getUsername());
                offerData.put("userFullName", user.getFullName());
                offerData.put("message", request.getMessage());
                offerData.put("status", "PENDING");
                offerData.put("createdAt", LocalDateTime.now().toString());

                WebSocketHandler.broadcastNewOffer(item.getId(), offerData);

                // 2. Broadcast price update
                WebSocketHandler.broadcastPriceUpdate(
                        item.getId(),
                        request.getOfferAmount().doubleValue()
                );

                // 3. Broadcast OUTBID status para cada oferta que fue superada
                for (Offer outbidOffer : offersToOutbid) {
                    if (!outbidOffer.getId().equals(generatedId)) {
                        WebSocketHandler.broadcastOfferStatusChange(
                                item.getId(),
                                outbidOffer.getId(),
                                "OUTBID"
                        );
                        log.debug("Broadcasted OUTBID status for offer {}", outbidOffer.getId());
                    }
                }

                // 4. Notify users who were outbid (simplified - notifies all)
                if (outbidCount > 0) {
                    WebSocketHandler.notifyOutbid(
                            null, // In real app, get actual user IDs
                            item.getId(),
                            item.getCurrentPrice().doubleValue(),
                            request.getOfferAmount().doubleValue()
                    );
                }
            }

            log.info("Offer created successfully with id: {}", generatedId);
            return offer;
        });
    }

    @Override
    public Offer acceptOffer(Long offerId) {
        log.info("Accepting offer: {}", offerId);

        return jdbi.inTransaction(handle -> {
            OfferDao offerDao = handle.attach(OfferDao.class);
            ItemDao itemDao = handle.attach(ItemDao.class);

            Offer offer = offerDao.findById(offerId)
                    .orElseThrow(() -> new IllegalArgumentException("Offer not found with id: " + offerId));

            if (offer.getStatus() != Offer.OfferStatus.PENDING) {
                throw new IllegalArgumentException("Only pending offers can be accepted");
            }

            List<Offer> offersToReject = offerDao.findByItemIdAndStatus(offer.getItemId(), "PENDING");

            // Update offer status to ACCEPTED
            offerDao.updateStatus(offerId, "ACCEPTED", LocalDateTime.now());
            offer.setStatus(Offer.OfferStatus.ACCEPTED);
            offer.setUpdatedAt(LocalDateTime.now());

            // Mark item as unavailable
            itemDao.updateAvailability(offer.getItemId(), false);

            // Mark all other pending offers for this item as REJECTED
            int rejectedCount = offerDao.markOthersAsOutbid(offer.getItemId(), offerId, "REJECTED");
            log.info("Marked {} other offers as REJECTED for item {}", rejectedCount, offer.getItemId());

            // ========== WEBSOCKET BROADCASTS ==========

            // 1. Broadcast item sold
            WebSocketHandler.broadcastItemSold(offer.getItemId(), offer.getUserId());

            WebSocketHandler.broadcastOfferStatusChange(
                    offer.getItemId(),
                    offerId,
                    "ACCEPTED"
            );

            for (Offer rejectedOffer : offersToReject) {
                if (!rejectedOffer.getId().equals(offerId)) {
                    WebSocketHandler.broadcastOfferStatusChange(
                            offer.getItemId(),
                            rejectedOffer.getId(),
                            "REJECTED"
                    );
                    log.debug("Broadcasted REJECTED status for offer {}", rejectedOffer.getId());
                }
            }

            log.info("Offer {} accepted successfully", offerId);
            return offer;
        });
    }


    @Override
    public Offer rejectOffer(Long offerId) {
        log.info("Rejecting offer: {}", offerId);

        return jdbi.inTransaction(handle -> {
            OfferDao offerDao = handle.attach(OfferDao.class);

            // Get offer
            Offer offer = offerDao.findById(offerId)
                    .orElseThrow(() -> new NotFoundException("Offer not found with id: " + offerId));

            if (offer.getStatus() != Offer.OfferStatus.PENDING) {
                throw new ConflictException("Only pending offers can be rejected");
            }

            // Update offer status to REJECTED
            offerDao.updateStatus(offerId, "REJECTED", LocalDateTime.now());
            offer.setStatus(Offer.OfferStatus.REJECTED);
            offer.setUpdatedAt(LocalDateTime.now());

            // ========== WEBSOCKET BROADCAST ==========
            WebSocketHandler.broadcastOfferStatusChange(
                    offer.getItemId(),
                    offerId,
                    "REJECTED"
            );

            log.info("Offer {} rejected successfully", offerId);
            return offer;
        });
    }

    @Override
    public boolean deleteOffer(Long id) {
        log.info("Deleting offer with id: {}", id);

        int rowsAffected = jdbi.withExtension(OfferDao.class, dao -> dao.delete(id));

        if (rowsAffected > 0) {
            log.info("Offer deleted successfully with id: {}", id);
            return true;
        }

        log.warn("No offer found to delete with id: {}", id);
        throw new NotFoundException("Offer not found with id: " + id);
    }

    @Override
    public boolean offerExists(Long id) {
        log.debug("Checking if offer exists with id: {}", id);
        return jdbi.withExtension(OfferDao.class, dao -> dao.exists(id));
    }

}