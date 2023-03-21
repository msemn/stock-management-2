package nl.iptiQ.stock.management.service;

import nl.iptiQ.stock.management.config.LoadDatabase;
import nl.iptiQ.stock.management.dto.StockCreateDto;
import nl.iptiQ.stock.management.dto.StockReadDto;
import nl.iptiQ.stock.management.repository.StockRepository;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.annotation.DirtiesContext;

import java.io.IOException;
import java.util.List;

@SpringBootTest
@DirtiesContext(classMode = DirtiesContext.ClassMode.AFTER_EACH_TEST_METHOD)
public class StockServiceTest {

    @Autowired
    StockService stockService;

    @Autowired
    StockRepository stockRepository;

    @Autowired
    LoadDatabase loadDatabase;

    @BeforeEach
    public void setUp() throws IOException {
        loadDatabase.initDatabase(stockRepository);
    }

    @Test
    public void findAll_ReturnsStockList() {
        List<StockReadDto> all = stockService.findAll();
        Assertions.assertEquals(2, all.size());
    }

    @Test
    public void findById_WithExistingStock_ReturnsStock() {
        StockReadDto byId = stockService.findById(1L);
        Assertions.assertNotNull(byId);
        Assertions.assertEquals(byId.getName(), "Bilbo Test");
        Assertions.assertEquals(byId.getPrice(), 12.7);
    }

    @Test
    public void createStock_ReturnsStock() {
        stockService.createStock(new StockCreateDto("Gandorf", 1.75));

        List<StockReadDto> all = stockService.findAll();
        Assertions.assertEquals(3, all.size());
    }

    @Test
    public void updateStock_ReturnsStock() {
        StockReadDto frodo_test_new = stockService.updateStock(
                2L, new StockCreateDto("Frodo Test New", 111d));
        StockReadDto byId = stockService.findById(2L);
        Assertions.assertEquals(byId, frodo_test_new);
    }

    @Test
    public void deleteStock_ReturnsAccepted() {
        stockService.deleteStock(1L);
        List<StockReadDto> all = stockService.findAll();
        Assertions.assertEquals(1, all.size());
        Assertions.assertEquals(all.get(0).getId(), 2L);
    }

}
