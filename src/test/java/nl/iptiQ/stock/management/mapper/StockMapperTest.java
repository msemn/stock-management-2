package nl.iptiQ.stock.management.mapper;

import nl.iptiQ.stock.management.dto.StockCreateDto;
import nl.iptiQ.stock.management.dto.StockReadDto;
import nl.iptiQ.stock.management.entity.StockEntity;
import org.junit.jupiter.api.Test;
import org.mapstruct.factory.Mappers;

import java.time.LocalDateTime;

import static org.junit.jupiter.api.Assertions.assertEquals;

public class StockMapperTest {

    private final StockMapper stockMapper = Mappers.getMapper(StockMapper.class);

    @Test
    public void testFromModel(){
        StockCreateDto stockCreateDto = new StockCreateDto("Frodo", 12.5D);
        StockEntity stockEntity = stockMapper.fromModel(stockCreateDto);
        assertEquals(stockCreateDto.getName(), stockEntity.getName());
        assertEquals(stockCreateDto.getPrice(), stockEntity.getPrice());
    }

    @Test
    public void fromEntity(){
        String strDateTime = "2021-04-19T10:10:10";
        LocalDateTime aLDT = LocalDateTime.parse(strDateTime);

        StockEntity stockEntity = new StockEntity(1L, "Frodo", 12.5D, aLDT);
        StockReadDto stockReadDto = stockMapper.fromEntity(stockEntity);
        assertEquals(stockEntity.getId(), stockReadDto.getId());
        assertEquals(stockEntity.getName(), stockReadDto.getName());
        assertEquals(stockEntity.getPrice(), stockReadDto.getPrice());
        assertEquals(stockEntity.getLastUpdate().toString(), stockReadDto.getLastUpdate());
    }

}
