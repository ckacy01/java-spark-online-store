package org.technoready.web;

import com.google.gson.Gson;
import lombok.extern.slf4j.Slf4j;
import org.eclipse.jetty.websocket.api.Session;
import org.eclipse.jetty.websocket.api.annotations.*;
import org.technoready.dto.response.WebSocketMessage;
import org.technoready.entity.Item;

import java.io.IOException;
import java.util.*;
import java.util.concurrent.ConcurrentHashMap;

/**
 * WebSocket Handler for Real-Time Auction Updates
 * Handles WebSocket connections and broadcasts updates to clients
 * @version 1.2.0
 */
@Slf4j
@WebSocket
public class WebSocketHandler {

    // Store sessions by item ID they're watching
    private static final Map<Long, Set<Session>> itemWatchers = new ConcurrentHashMap<>();

    // Store all active sessions
    private static final Set<Session> allSessions = Collections.synchronizedSet(new HashSet<>());

    private static final Gson gson = new Gson();

    @OnWebSocketConnect
    public void onConnect(Session session) {
        log.info("WebSocket connected: {}", session.getRemoteAddress());
        allSessions.add(session);

        // Send welcome message
        sendToSession(session, new WebSocketMessage(
                "CONNECTED",
                "Connection established",
                null
        ));
    }

    @OnWebSocketClose
    public void onClose(Session session, int statusCode, String reason) {
        log.info("WebSocket closed: {} - Status: {} - Reason: {}",
                session.getRemoteAddress(), statusCode, reason);

        // Remove from all watchers
        itemWatchers.values().forEach(set -> set.remove(session));
        allSessions.remove(session);
    }

    @OnWebSocketError
    public void onError(Session session, Throwable error) {
        log.error("WebSocket error for session {}: {}",
                session.getRemoteAddress(), error.getMessage());
    }

    @OnWebSocketMessage
    public void onMessage(Session session, String message) {
        log.debug("Received message: {}", message);

        try {
            // Parse incoming message
            Map<String, Object> data = gson.fromJson(message, Map.class);
            String action = (String) data.get("action");

            if ("WATCH_ITEM".equals(action)) {
                // Client wants to watch an item
                Double itemIdDouble = (Double) data.get("itemId");
                Long itemId = itemIdDouble.longValue();
                watchItem(session, itemId);

                sendToSession(session, new WebSocketMessage(
                        "WATCHING",
                        "Now watching item " + itemId,
                        Map.of("itemId", itemId)
                ));

            } else if ("UNWATCH_ITEM".equals(action)) {
                // Client stops watching an item
                Double itemIdDouble = (Double) data.get("itemId");
                Long itemId = itemIdDouble.longValue();
                unwatchItem(session, itemId);

                sendToSession(session, new WebSocketMessage(
                        "UNWATCHED",
                        "Stopped watching item " + itemId,
                        Map.of("itemId", itemId)
                ));
            }

        } catch (Exception e) {
            log.error("Error processing WebSocket message", e);
            sendToSession(session, new WebSocketMessage(
                    "ERROR",
                    "Invalid message format",
                    null
            ));
        }
    }

    // ========== Public Methods for Broadcasting ==========

    public static void brodcastNewItem(Long itemId, Map<String, Object> data) {
        log.info("Brodcasting new item: {}", itemId);
        WebSocketMessage message = new WebSocketMessage(
                "NEW_ITEMADDED",
                "New item was appered",
                Map.of("itemId", itemId,
                        "item", data
                )
        );
        broadcastToAll(message);
    }

    public static void broadcastItemUpdated(Long itemId, Map<String, Object> data) {
        log.info("Broadcasting item updated: {}", itemId);
        WebSocketMessage message = new WebSocketMessage(
                "ITEM_UPDATED",
                "A Item was updated by the seller",
                Map.of("itemId", itemId,
                        "item", data
                )
        );
        broadcastToAll(message);
    }

    /**
     * Broadcast new offer to all watching the item
     */
    public static void broadcastNewOffer(Long itemId, Map<String, Object> offerData) {
        log.info("Broadcasting new offer for item {}", itemId);

        WebSocketMessage message = new WebSocketMessage(
                "NEW_OFFER",
                "New offer placed on item",
                Map.of(
                        "itemId", itemId,
                        "offer", offerData
                )
        );
        broadcastToAll(message);
    }

    /**
     * Broadcast price update
     */
    public static void broadcastPriceUpdate(Long itemId, Double newPrice) {
        log.info("Broadcasting price update for item {}: ${}", itemId, newPrice);

        WebSocketMessage message = new WebSocketMessage(
                "PRICE_UPDATE",
                "Item price updated",
                Map.of(
                        "itemId", itemId,
                        "newPrice", newPrice
                )
        );

        broadcastToItemWatchers(itemId, message);
    }

    /**
     * Notify user they were outbid
     */
    public static void notifyOutbid(Long userId, Long itemId, Double yourOffer, Double newHighestOffer) {
        log.info("Notifying user {} they were outbid on item {}", userId, itemId);

        WebSocketMessage message = new WebSocketMessage(
                "OUTBID",
                "Your offer was outbid!",
                Map.of(
                        "itemId", itemId,
                        "yourOffer", yourOffer,
                        "newHighestOffer", newHighestOffer
                )
        );

        // Send to all sessions (in a real app, you'd filter by userId)
        broadcastToAll(message);
    }

    /**
     * Broadcast item sold
     */
    public static void broadcastItemSold(Long itemId, Long winnerId) {
        log.info("Broadcasting item {} sold to user {}", itemId, winnerId);

        WebSocketMessage message = new WebSocketMessage(
                "ITEM_SOLD",
                "Item has been sold",
                Map.of(
                        "itemId", itemId,
                        "winnerId", winnerId
                )
        );

        broadcastToItemWatchers(itemId, message);
    }

    /**
     * Broadcast offer status change (accepted/rejected)
     */
    public static void broadcastOfferStatusChange(Long itemId, Long offerId, String status) {
        log.info("Broadcasting offer {} status change: {}", offerId, status);

        WebSocketMessage message = new WebSocketMessage(
                "OFFER_STATUS_CHANGE",
                "Offer status updated",
                Map.of(
                        "itemId", itemId,
                        "offerId", offerId,
                        "status", status
                )
        );

        broadcastToItemWatchers(itemId, message);
    }

    // ========== Private Helper Methods ==========

    private void watchItem(Session session, Long itemId) {
        itemWatchers.computeIfAbsent(itemId, k -> Collections.synchronizedSet(new HashSet<>()))
                .add(session);
        log.debug("Session {} now watching item {}", session.getRemoteAddress(), itemId);
    }

    private void unwatchItem(Session session, Long itemId) {
        Set<Session> watchers = itemWatchers.get(itemId);
        if (watchers != null) {
            watchers.remove(session);
            if (watchers.isEmpty()) {
                itemWatchers.remove(itemId);
            }
        }
        log.debug("Session {} stopped watching item {}", session.getRemoteAddress(), itemId);
    }

    private static void broadcastToItemWatchers(Long itemId, WebSocketMessage message) {
        Set<Session> watchers = itemWatchers.get(itemId);
        if (watchers != null && !watchers.isEmpty()) {
            String json = gson.toJson(message);
            synchronized (watchers) {
                watchers.forEach(session -> sendToSession(session, json));
            }
            log.debug("Broadcasted to {} watchers of item {}", watchers.size(), itemId);
        }
    }

    private static void broadcastToAll(WebSocketMessage message) {
        String json = gson.toJson(message);
        synchronized (allSessions) {
            allSessions.forEach(session -> sendToSession(session, json));
        }
        log.debug("Broadcasted to {} total sessions", allSessions.size());
    }

    private static void sendToSession(Session session, WebSocketMessage message) {
        sendToSession(session, gson.toJson(message));
    }

    private static void sendToSession(Session session, String message) {
        if (session != null && session.isOpen()) {
            try {
                session.getRemote().sendString(message);
            } catch (IOException e) {
                log.error("Error sending message to session", e);
            }
        }
    }
}
