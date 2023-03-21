package nl.iptiQ.stock.management.service;

import nl.iptiQ.stock.management.dto.StockCreateDto;
import nl.iptiQ.stock.management.dto.StockReadDto;
import nl.iptiQ.stock.management.entity.StockEntity;
import nl.iptiQ.stock.management.exception.StockNotFoundException;
import nl.iptiQ.stock.management.mapper.StockMapper;
import nl.iptiQ.stock.management.repository.StockRepository;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class StockServiceImpl implements StockService{

    private final StockRepository stockRepository;
    private final StockMapper stockMapper;

    public final static String errMsg = "Stock not found for this id: ";

    public StockServiceImpl(StockRepository stockRepository, StockMapper stockMapper) {
        this.stockRepository = stockRepository;
        this.stockMapper = stockMapper;
    }

    public List<StockReadDto> findAll() {
        return stockRepository
                .findAll()
                .stream()
                .map(stockMapper::fromEntity)
                .collect(Collectors.toList());
    }

    public StockReadDto findById(Long stockId) {
        return stockMapper.fromEntity(stockRepository
                .findById(stockId)
                .orElseThrow(() -> new StockNotFoundException(errMsg + stockId))
        );
    }

    public StockReadDto createStock(StockCreateDto stock) {
        return stockMapper.fromEntity(stockRepository.save(stockMapper.fromModel(stock)));
    }

    public StockReadDto updateStock(Long stockId, StockCreateDto stock) {
        StockEntity entity = stockRepository
                        .findById(stockId)
                        .orElseThrow(() -> new StockNotFoundException(errMsg + stockId));
        entity.setName(stock.getName());
        entity.setPrice(stock.getPrice());
        return stockMapper.fromEntity(stockRepository.save(entity));
    }

    public void deleteStock(Long stockId) {
        StockEntity entity = stockRepository
                        .findById(stockId)
                        .orElseThrow(() -> new StockNotFoundException(errMsg + stockId));
        stockRepository.delete(entity);
    }
}
