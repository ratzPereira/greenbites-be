package com.ratz.greenbites.mapper;

import com.ratz.greenbites.DTO.collection.CollectionDTO;
import com.ratz.greenbites.DTO.collection.CreateCollectionDTO;
import com.ratz.greenbites.entity.Collection;
import javax.annotation.processing.Generated;
import org.springframework.stereotype.Component;

@Generated(
    value = "org.mapstruct.ap.MappingProcessor",
    date = "2024-03-25T22:04:09-0100",
    comments = "version: 1.4.2.Final, compiler: javac, environment: Java 18.0.2 (Amazon.com Inc.)"
)
@Component
public class CollectionMapperImpl implements CollectionMapper {

    @Override
    public CollectionDTO toDto(Collection collection) {
        if ( collection == null ) {
            return null;
        }

        CollectionDTO collectionDTO = new CollectionDTO();

        collectionDTO.setId( collection.getId() );
        collectionDTO.setName( collection.getName() );

        return collectionDTO;
    }

    @Override
    public Collection toEntity(CreateCollectionDTO createCollectionDTO) {
        if ( createCollectionDTO == null ) {
            return null;
        }

        Collection collection = new Collection();

        collection.setName( createCollectionDTO.getName() );

        return collection;
    }
}
