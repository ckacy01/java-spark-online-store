package org.technoready.util;



import org.technoready.dto.request.CreateOfferRequest;
import org.technoready.dto.response.OfferWithDetailsResponse;
import org.technoready.dto.response.OfferResponse;
import org.technoready.entity.Offer;
import org.technoready.entity.OfferWithDetails;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.stream.Collectors;


public class OfferMapper {

    private static final DateTimeFormatter FORMATTER = DateTimeFormatter.ISO_LOCAL_DATE_TIME;

    /**
     * Convert CreateOfferRequest to Offer entity
     */
    public static Offer toEntity(CreateOfferRequest request) {
        return Offer.builder()
                .itemId(request.getItemId())
                .userId(request.getUserId())
                .offerAmount(request.getOfferAmount())
                .status(Offer.OfferStatus.PENDING)
                .message(request.getMessage())
                .createdAt(LocalDateTime.now())
                .updatedAt(LocalDateTime.now())
                .build();
    }

    /**
     * Convert Offer entity to OfferResponse DTO
     */
    public static OfferResponse toResponse(Offer offer) {
        return OfferResponse.builder()
                .id(offer.getId())
                .itemId(offer.getItemId())
                .userId(offer.getUserId())
                .offerAmount(offer.getOfferAmount())
                .status(offer.getStatus().name())
                .message(offer.getMessage())
                .createdAt(offer.getCreatedAt() != null ? offer.getCreatedAt().format(FORMATTER) : null)
                .updatedAt(offer.getUpdatedAt() != null ? offer.getUpdatedAt().format(FORMATTER) : null)
                .build();
    }

    /**
     * Convert OfferWithDetails to OfferWithDetailsResponse
     */
    public static OfferWithDetailsResponse toDetailsResponse(OfferWithDetails offer) {
        return OfferWithDetailsResponse.builder()
                .id(offer.getId())
                .offerAmount(offer.getOfferAmount())
                .status(offer.getStatus().name())
                .message(offer.getMessage())
                .createdAt(offer.getCreatedAt() != null ? offer.getCreatedAt().format(FORMATTER) : null)
                .updatedAt(offer.getUpdatedAt() != null ? offer.getUpdatedAt().format(FORMATTER) : null)
                .user(OfferWithDetailsResponse.UserInfoResponse.builder()
                        .id(offer.getUserId())
                        .username(offer.getUsername())
                        .email(offer.getUserEmail())
                        .fullName(offer.getUserFullName())
                        .build())
                .item(OfferWithDetailsResponse.ItemInfoResponse.builder()
                        .id(offer.getItemId())
                        .name(offer.getItemName())
                        .description(offer.getItemDescription())
                        .currentPrice(offer.getItemCurrentPrice())
                        .originalPrice(offer.getItemOriginalPrice())
                        .isAvailable(offer.getItemIsAvailable())
                        .build())
                .build();
    }

    /**
     * Convert list of Offer entities to list of OfferResponse DTOs
     */
    public static List<OfferResponse> toResponseList(List<Offer> offers) {
        return offers.stream()
                .map(OfferMapper::toResponse)
                .collect(Collectors.toList());
    }

    /**
     * Convert list of OfferWithDetails to list of OfferWithDetailsResponse
     */
    public static List<OfferWithDetailsResponse> toDetailsResponseList(List<OfferWithDetails> offers) {
        return offers.stream()
                .map(OfferMapper::toDetailsResponse)
                .collect(Collectors.toList());
    }
}
