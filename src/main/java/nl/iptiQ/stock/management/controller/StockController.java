package nl.iptiQ.stock.management.controller;

import lombok.RequiredArgsConstructor;
import lombok.extern.java.Log;
import nl.iptiQ.stock.management.dto.StockCreateDto;
import nl.iptiQ.stock.management.dto.StockReadDto;
import nl.iptiQ.stock.management.service.StockService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.validation.Valid;
import java.util.List;

@Log
@RestController
@RequestMapping("stocks")
@RequiredArgsConstructor
public class StockController {

    private final StockService stockService;

    @GetMapping
    public ResponseEntity<List<StockReadDto>> findAll() {
        return ResponseEntity.ok().body(stockService.findAll());
    }

    @GetMapping("/{id}")
    public ResponseEntity<StockReadDto> findById(@PathVariable(value = "id") Long stockId) {
        StockReadDto found = stockService.findById(stockId);
        log.info("Stock with Id={" + found.getId() + "} was found successfully.");
        return ResponseEntity.ok().body(found);
    }

    @PostMapping
    public ResponseEntity<StockReadDto> createStock(@Valid @RequestBody StockCreateDto stock) {
        StockReadDto created = stockService.createStock(stock);
        log.info("Stock with Id={" + created.getId() + "} was created successfully.");
        return ResponseEntity.ok().body(created);
    }

    @PatchMapping("/{id}")
    public ResponseEntity<StockReadDto> updateStock(@PathVariable(value = "id") Long stockId,
                                                   @Valid @RequestBody StockCreateDto stock) {
        StockReadDto updated = stockService.updateStock(stockId, stock);
        log.info("Stock with Id={" + stockId + "} was updated successfully.");
        return ResponseEntity.ok().body(updated);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<?> deleteStock(@PathVariable(value = "id") Long stockId) {
        stockService.deleteStock(stockId);
        log.info("Stock with Id={" + stockId + "} was deleted successfully.");
        return ResponseEntity.noContent().build();
    }
}
