package com.ratz.greenbites.services;

import com.ratz.greenbites.DTO.collection.CollectionDTO;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

public interface CollectionService {

    CollectionDTO createCollection(Long userId, String collectionName);

    CollectionDTO updateCollectionName(Long collectionId, String newName);

    void deleteCollection(Long collectionId);

    Page<CollectionDTO> getAllCollectionsForUser(Long userId, Pageable pageable);

    CollectionDTO getCollectionById(Long collectionId);
}
