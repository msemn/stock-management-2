package nl.iptiQ.stock.management.service;

import nl.iptiQ.stock.management.dto.StockCreateDto;
import nl.iptiQ.stock.management.dto.StockReadDto;

import java.util.List;

public interface StockService {
    List<StockReadDto> findAll();
    StockReadDto findById(Long stockId);
    StockReadDto createStock(StockCreateDto stock);
    StockReadDto updateStock(Long stockId, StockCreateDto stock);
    void deleteStock(Long stockId);
}
