package com.ratz.greenbites.services.Impl;

import com.ratz.greenbites.DTO.collection.CollectionDTO;
import com.ratz.greenbites.entity.Collection;
import com.ratz.greenbites.entity.User;
import com.ratz.greenbites.mapper.CollectionMapper;
import com.ratz.greenbites.repository.CollectionRepository;
import com.ratz.greenbites.repository.UserRepository;
import com.ratz.greenbites.services.CollectionService;
import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class CollectionServiceImpl implements CollectionService {

    private final CollectionRepository collectionRepository;
    private final UserRepository userRepository;

    @Override
    public CollectionDTO createCollection(Long userId, String collectionName) {
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new EntityNotFoundException("User not found with ID: " + userId));

        Collection collection = new Collection();
        collection.setName(collectionName);
        collection.setUser(user);
        Collection savedCollection = collectionRepository.save(collection);
        return CollectionMapper.INSTANCE.toDto(savedCollection);
    }

    @Override
    public CollectionDTO updateCollectionName(Long collectionId, String newName) {
        Collection collection = collectionRepository.findById(collectionId)
                .orElseThrow(() -> new EntityNotFoundException("Collection not found with ID: " + collectionId));
        collection.setName(newName);
        Collection updatedCollection = collectionRepository.save(collection);
        return CollectionMapper.INSTANCE.toDto(updatedCollection);
    }

    @Override
    public Page<CollectionDTO> getAllCollectionsForUser(Long userId, Pageable pageable) {
        return collectionRepository.findByUserId(userId, pageable)
                .map(CollectionMapper.INSTANCE::toDto);
    }

    @Override
    public CollectionDTO getCollectionById(Long collectionId) {
        Collection collection = collectionRepository.findById(collectionId)
                .orElseThrow(() -> new EntityNotFoundException("Collection not found with ID: " + collectionId));
        return CollectionMapper.INSTANCE.toDto(collection);
    }

    @Override
    public void deleteCollection(Long collectionId) {
        Collection collection = collectionRepository.findById(collectionId)
                .orElseThrow(() -> new EntityNotFoundException("Collection not found with ID: " + collectionId));
        collectionRepository.delete(collection);
    }
}
