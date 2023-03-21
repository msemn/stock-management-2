package nl.iptiQ.stock.management;

import nl.iptiQ.stock.management.dto.StockCreateDto;
import nl.iptiQ.stock.management.dto.StockReadDto;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.test.annotation.DirtiesContext;

import java.util.HashMap;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertNull;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@DirtiesContext(classMode = DirtiesContext.ClassMode.AFTER_EACH_TEST_METHOD)
class StockIT {

    @Value(value="${local.server.port}")
    private int port;

    @Autowired
    private TestRestTemplate restTemplate;

    private String baseUrl;
    private StockReadDto bilbo;
    private StockReadDto frodo;

    @BeforeEach
    public void setUp(){
        baseUrl = "http://localhost:" + port + "/stocks";
        bilbo = new StockReadDto(1L, "Bilbo Test", 12.7, "20/12/2022, 12:30");
        frodo = new StockReadDto(2L, "Frodo Test", 11D, "20/12/2022, 12:30");
    }

    @Test
    public void getAllStocksShouldReturnAllStocks() {
        ResponseEntity<StockReadDto[]> response = restTemplate.getForEntity(baseUrl, StockReadDto[].class);
        StockReadDto[] stocks = response.getBody();
        assertNotNull(stocks);
        Map<Long, StockReadDto> responseMap = new HashMap<>();

        for (StockReadDto stock : stocks) {
            responseMap.put(stock.getId(), stock);
        }

        assertNotNull(stocks);
        assertEquals(stocks.length, 2);

        assertEquals(responseMap.get(bilbo.getId()).getName(), bilbo.getName());
        assertEquals(responseMap.get(bilbo.getId()).getPrice(), bilbo.getPrice());
        assertEquals(responseMap.get(frodo.getId()).getName(), frodo.getName());
        assertEquals(responseMap.get(frodo.getId()).getPrice(), frodo.getPrice());
    }

    @Test
    public void getOneStockShouldReturnIt(){
        StockReadDto response = restTemplate.getForObject(baseUrl + "/1", StockReadDto.class);
        Assertions.assertEquals(bilbo.getId(), response.getId());
        Assertions.assertEquals(bilbo.getName(), response.getName());
        Assertions.assertEquals(bilbo.getPrice(), response.getPrice());
    }

    @Test
    public void CreateStockReturnSuccessful(){
        StockCreateDto inputStock = new StockCreateDto("Lord", 10D);
        HttpEntity<StockCreateDto> request = new HttpEntity<>(inputStock);
        StockReadDto response = restTemplate.postForObject(baseUrl, request, StockReadDto.class);
        Assertions.assertNotNull(response);
        Assertions.assertEquals(inputStock.getName(), response.getName());
        Assertions.assertEquals(inputStock.getPrice(), response.getPrice());
    }

//    @Test
//    public void CreateStockReturnSuccessful() {
//        StockCreateDto inputStock = new StockCreateDto("Lord", 0.01);
//        HttpEntity<StockCreateDto> request = new HttpEntity<>(inputStock);
//        ResponseEntity<StockReadDto> response = restTemplate.exchange(baseUrl, HttpMethod.PATCH, request, StockReadDto.class);
//        StockReadDto responseStock = response.getBody();
//
//        assertEquals(HttpStatus.OK, response.getStatusCode());
//        assertNotNull(responseStock);
//        assertEquals(inputStock.getName(), responseStock.getName());
//        assertEquals(inputStock.getPrice(), responseStock.getPrice());
//    }

    @Test
    public void DeleteStockReturnSuccessful() {
        ResponseEntity<Void> response =
                restTemplate.exchange(baseUrl + "/1", HttpMethod.DELETE, null, Void.class);
        assertEquals(HttpStatus.NO_CONTENT, response.getStatusCode());
        assertNull(response.getBody());
    }
}


