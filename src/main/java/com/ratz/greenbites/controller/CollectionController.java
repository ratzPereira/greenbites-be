package com.ratz.greenbites.controller;

import com.ratz.greenbites.DTO.collection.CollectionDTO;
import com.ratz.greenbites.DTO.collection.CreateCollectionDTO;
import com.ratz.greenbites.entity.User;
import com.ratz.greenbites.response.HttpResponse;
import com.ratz.greenbites.services.CollectionService;
import com.ratz.greenbites.services.UserService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.Map;

@RequiredArgsConstructor
@RestController
@RequestMapping("/api/v1/collections")
@Slf4j
public class CollectionController {

    private final CollectionService collectionService;
    private final UserService userService;

    @GetMapping
    public ResponseEntity<HttpResponse> getAllCollections(Pageable pageable) {
        User user = getAuthenticatedUser();
        Page<CollectionDTO> collections = collectionService.getAllCollectionsForUser(user.getId(), pageable);
        log.info("User {} fetching all collections", user.getId());
        return buildPageResponse(collections, "Collections fetched successfully.", "collections");
    }

    @GetMapping("/{collectionId}")
    public ResponseEntity<HttpResponse> getCollectionById(@PathVariable Long collectionId) {
        CollectionDTO collectionDTO = collectionService.getCollectionById(collectionId);
        log.info("Fetching collection with ID {}", collectionId);
        return buildHttpResponse(HttpStatus.OK, "Collection fetched successfully", Map.of("collection", collectionDTO));
    }

    @PostMapping
    public ResponseEntity<HttpResponse> createCollection(@RequestBody CreateCollectionDTO createCollectionDTO) {
        User user = getAuthenticatedUser();
        CollectionDTO collectionDTO = collectionService.createCollection(user.getId(), createCollectionDTO.getName());
        log.info("User {} creating a new collection named {}", user.getId(), createCollectionDTO.getName());
        return buildHttpResponse(HttpStatus.CREATED, "Collection created successfully", Map.of("collection", collectionDTO));
    }

    @PutMapping("/{collectionId}")
    public ResponseEntity<HttpResponse> updateCollection(@PathVariable Long collectionId, @RequestBody CreateCollectionDTO collectionDTO) {
        collectionService.updateCollectionName(collectionId, collectionDTO.getName());
        log.info("Updating collection ID {} with new name {}", collectionId, collectionDTO.getName());
        return buildSimpleResponse("Collection updated successfully");
    }

    @DeleteMapping("/{collectionId}")
    public ResponseEntity<HttpResponse> deleteCollection(@PathVariable Long collectionId) {
        collectionService.deleteCollection(collectionId);
        log.info("Deleting collection with ID {}", collectionId);
        return buildSimpleResponse("Collection deleted successfully");
    }

    private User getAuthenticatedUser() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        String currentUsername = authentication.getName();
        return userService.getUserByEmail(currentUsername);
    }

    private ResponseEntity<HttpResponse> buildSimpleResponse(String message) {
        HttpResponse response = HttpResponse.ok(message, null).build();
        return ResponseEntity.ok(response);
    }

    private ResponseEntity<HttpResponse> buildHttpResponse(HttpStatus status, String message, Map<String, Object> data) {
        HttpResponse response = HttpResponse.builder()
                .timeStamp(LocalDateTime.now().toString())
                .statusCode(status.value())
                .httpStatus(status)
                .message(message)
                .data(data)
                .build();
        return new ResponseEntity<>(response, status);
    }

    private ResponseEntity<HttpResponse> buildPageResponse(Page<?> page, String message, String dataType) {
        Map<String, Object> responseData = new HashMap<>();
        responseData.put(dataType, page.getContent());
        responseData.put("totalItems", page.getTotalElements());
        responseData.put("totalPages", page.getTotalPages());

        HttpResponse response = HttpResponse.builder()
                .timeStamp(LocalDateTime.now().toString())
                .statusCode(HttpStatus.OK.value())
                .httpStatus(HttpStatus.OK)
                .message(message)
                .data(responseData)
                .build();
        return ResponseEntity.ok(response);
    }
}
