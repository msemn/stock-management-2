package nl.iptiQ.stock.management.mapper;

import nl.iptiQ.stock.management.dto.StockCreateDto;
import nl.iptiQ.stock.management.dto.StockReadDto;
import nl.iptiQ.stock.management.entity.StockEntity;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(componentModel = "spring")
public interface StockMapper {

    StockEntity fromModel(StockCreateDto stock);

//    @Mapping(target="name", source="id")
    StockReadDto fromEntity(StockEntity entity);
}
