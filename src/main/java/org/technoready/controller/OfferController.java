package org.technoready.controller;

import com.google.gson.Gson;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.technoready.dto.request.CreateOfferRequest;
import org.technoready.dto.response.OfferWithDetailsResponse;
import org.technoready.dto.response.ApiResponse;
import org.technoready.dto.response.OfferResponse;
import org.technoready.entity.Offer;
import org.technoready.entity.OfferWithDetails;
import org.technoready.service.OfferService;
import org.technoready.util.OfferMapper;
import spark.Request;
import spark.Response;

import java.util.List;
import java.util.Optional;

@Slf4j
@RequiredArgsConstructor
public class OfferController {

    private final OfferService offerService;
    private final Gson gson;

    /**
     * GET /offers - Retrieve all offers
     */
    public String getAllOffers(Request request, Response response) {
        log.info("GET /offers - Fetching all offers");
        String withDetails = request.queryParams("details");
        if ("true".equalsIgnoreCase(withDetails)) {
            List<OfferWithDetails> offers = offerService.getAllOffersWithDetails();
            List<OfferWithDetailsResponse> offerResponses = OfferMapper.toDetailsResponseList(offers);
            response.status(200);
            return gson.toJson(ApiResponse.success(offerResponses));
        }else {
            List<Offer> offers = offerService.getAllOffers();
            List<OfferResponse> offerResponses = OfferMapper.toResponseList(offers);
            response.status(200);
            return gson.toJson(ApiResponse.success(offerResponses));
        }
    }

    /**
     * GET /offers/:id - Retrieve offer by ID
     */
    public String getOfferById(Request request, Response response) {
        String idParam = request.params(":id");
        log.info("GET /offers/{} - Fetching offer by id", idParam);

        Long id = Long.parseLong(idParam);
        String withDetails = request.queryParams("details");

        if ("true".equalsIgnoreCase(withDetails)) {
            Optional<OfferWithDetails> offer = offerService.getOfferByIdWithDetails(id);
            if (offer.isPresent()) {
                OfferWithDetailsResponse offerResponse = OfferMapper.toDetailsResponse(offer.get());
                response.status(200);
                return gson.toJson(ApiResponse.success(offerResponse));
            }
        } else {
            Optional<Offer> offer = offerService.getOfferById(id);
            if (offer.isPresent()) {
                OfferResponse offerResponse = OfferMapper.toResponse(offer.get());
                response.status(200);
                return gson.toJson(ApiResponse.success(offerResponse));
            }
        }

        response.status(404);
        return gson.toJson(ApiResponse.error("Offer not found with id: " + id));


    }

    /**
     * GET /offers/item/:itemId - Get all offers for a specific item
     */
    public String getOffersByItemId(Request request, Response response) {
        String itemIdParam = request.params(":itemId");
        log.info("GET /offers/item/{} - Fetching offers for item", itemIdParam);
        Long itemId = Long.parseLong(itemIdParam);
        String withDetails = request.queryParams("details");
        String status = request.queryParams("status");

        if ("true".equalsIgnoreCase(withDetails)) {
            List<OfferWithDetails> offers = offerService.getOffersByItemIdWithDetails(itemId);
            List<OfferWithDetailsResponse> offerResponses = OfferMapper.toDetailsResponseList(offers);
            response.status(200);
            return gson.toJson(ApiResponse.success(offerResponses));
        } else if (status != null && !status.trim().isEmpty()) {
            List<Offer> offers = offerService.getOffersByItemIdAndStatus(itemId, status.toUpperCase());
            List<OfferResponse> offerResponses = OfferMapper.toResponseList(offers);
            response.status(200);
            return gson.toJson(ApiResponse.success(offerResponses));
        } else {
            List<Offer> offers = offerService.getOffersByItemId(itemId);
            List<OfferResponse> offerResponses = OfferMapper.toResponseList(offers);
            response.status(200);
            return gson.toJson(ApiResponse.success(offerResponses));
        }

    }

    /**
     * GET /offers/user/:userId - Get all offers by a specific user
     */
    public String getOffersByUserId(Request request, Response response) {
        String userIdParam = request.params(":userId");
        log.info("GET /offers/user/{} - Fetching offers for user", userIdParam);
        Long userId = Long.parseLong(userIdParam);
        String withDetails = request.queryParams("details");
        String status = request.queryParams("status");

        if ("true".equalsIgnoreCase(withDetails)) {
            List<OfferWithDetails> offers = offerService.getOffersByUserIdWithDetails(userId);
            List<OfferWithDetailsResponse> offerResponses = OfferMapper.toDetailsResponseList(offers);
            response.status(200);
            return gson.toJson(ApiResponse.success(offerResponses));
        } else if (status != null && !status.trim().isEmpty()) {
            List<Offer> offers = offerService.getOffersByUserIdAndStatus(userId, status.toUpperCase());
            List<OfferResponse> offerResponses = OfferMapper.toResponseList(offers);
            response.status(200);
            return gson.toJson(ApiResponse.success(offerResponses));
        } else {
            List<Offer> offers = offerService.getOffersByUserId(userId);
            List<OfferResponse> offerResponses = OfferMapper.toResponseList(offers);
            response.status(200);
            return gson.toJson(ApiResponse.success(offerResponses));
        }
    }

    /**
     * POST /offers - Create new offer
     */
    public String createOffer(Request request, Response response) {
        log.info("POST /offers - Creating new offer");
        CreateOfferRequest createRequest = gson.fromJson(request.body(), CreateOfferRequest.class);
        if (createRequest == null) {
            response.status(400);
            return gson.toJson(ApiResponse.error("Request body is required"));
        }

        Offer offer = offerService.createOffer(createRequest);
        OfferResponse offerResponse = OfferMapper.toResponse(offer);

        response.status(201);
        return gson.toJson(ApiResponse.success("Offer created successfully", offerResponse));
    }

    /**
     * PUT /offers/:id/accept - Accept an offer
     */
    public String acceptOffer(Request request, Response response) {
        String idParam = request.params(":id");
        log.info("PUT /offers/{}/accept - Accepting offer", idParam);

        Long id = Long.parseLong(idParam);

        Offer offer = offerService.acceptOffer(id);
        OfferResponse offerResponse = OfferMapper.toResponse(offer);

        response.status(200);
        return gson.toJson(ApiResponse.success("Offer accepted successfully", offerResponse));
    }

    /**
     * PUT /offers/:id/reject - Reject an offer
     */
    public String rejectOffer(Request request, Response response) {
        String idParam = request.params(":id");
        log.info("PUT /offers/{}/reject - Rejecting offer", idParam);

        Long id = Long.parseLong(idParam);

        Offer offer = offerService.rejectOffer(id);
        OfferResponse offerResponse = OfferMapper.toResponse(offer);

        response.status(200);
        return gson.toJson(ApiResponse.success("Offer rejected successfully", offerResponse));
    }

    /**
     * DELETE /offers/:id - Delete offer
     */
    public String deleteOffer(Request request, Response response) {
        String idParam = request.params(":id");
        log.info("DELETE /offers/{} - Deleting offer", idParam);

        Long id = Long.parseLong(idParam);
        boolean deleted = offerService.deleteOffer(id);

        if (deleted) {
            response.status(200);
            return gson.toJson(ApiResponse.success("Offer deleted successfully", null));
        } else {
            response.status(404);
            return gson.toJson(ApiResponse.error("Offer not found with id: " + id));
        }
    }
}
