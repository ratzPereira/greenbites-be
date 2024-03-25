package com.ratz.greenbites.mapper;

import com.ratz.greenbites.DTO.collection.CollectionDTO;
import com.ratz.greenbites.DTO.collection.CreateCollectionDTO;
import com.ratz.greenbites.entity.Collection;
import org.mapstruct.Mapper;
import org.mapstruct.factory.Mappers;

@Mapper(componentModel = "spring")
public interface CollectionMapper {

    CollectionMapper INSTANCE = Mappers.getMapper(CollectionMapper.class);

    CollectionDTO toDto(Collection collection);

    Collection toEntity(CreateCollectionDTO createCollectionDTO);


}