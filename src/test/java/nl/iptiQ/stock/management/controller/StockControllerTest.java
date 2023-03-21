package nl.iptiQ.stock.management.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.ObjectWriter;
import nl.iptiQ.stock.management.dto.StockCreateDto;
import nl.iptiQ.stock.management.dto.StockReadDto;
import nl.iptiQ.stock.management.exception.StockNotFoundException;
import nl.iptiQ.stock.management.service.StockService;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;

import java.util.List;
import java.util.Objects;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(StockController.class)
public class StockControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private StockService service;

    private final long stockId = 1;
    private final String expectedErrMsg = "Stock not found for this id: " + stockId;
    private final String baseUri = "/stocks";
    private final ObjectWriter ow = new ObjectMapper().writer();

    @Test
    public void findAll_ReturnsStockList() throws Exception {
        Mockito.when(service.findAll()).thenReturn(
                List.of(new StockReadDto(1L, "Bilbo Baggins", 12.7d, "2021-04-19 10:10:10"),
                        new StockReadDto(2L, "Frodo Baggins", 11d, "2021-04-19 11:11:11")
                ));
        this.mockMvc
                .perform(MockMvcRequestBuilders.get(baseUri))
                .andExpect(status().isOk())
                .andExpect(MockMvcResultMatchers.jsonPath("$.size()").value(2))
                .andExpect(MockMvcResultMatchers.jsonPath("$[0].id").value(1))
                .andExpect(MockMvcResultMatchers.jsonPath("$[0].name").value("Bilbo Baggins"))
                .andExpect(MockMvcResultMatchers.jsonPath("$[0].price").value(12.7d))
                .andExpect(MockMvcResultMatchers.jsonPath("$[0].lastUpdate")
                        .value("2021-04-19 10:10:10"))
                .andExpect(MockMvcResultMatchers.jsonPath("$[1].id").value(2))
                .andExpect(MockMvcResultMatchers.jsonPath("$[1].name").value("Frodo Baggins"))
                .andExpect(MockMvcResultMatchers.jsonPath("$[1].price").value(11d))
                .andExpect(MockMvcResultMatchers.jsonPath("$[1].lastUpdate")
                        .value("2021-04-19 11:11:11"));
    }

    @Test
    public void findById_WithExistingStock_ReturnsStock() throws Exception {
        Mockito.when(service.findById(stockId)).thenReturn(
                new StockReadDto(stockId, "Bilbo Baggins", 12.7d, "2021-04-19 10:10:10")
        );
        this.mockMvc
                .perform(MockMvcRequestBuilders.get(baseUri + "/" + stockId))
                .andExpect(status().isOk())
                .andExpect(MockMvcResultMatchers.jsonPath("$.id").value(stockId))
                .andExpect(MockMvcResultMatchers.jsonPath("$.name").value("Bilbo Baggins"))
                .andExpect(MockMvcResultMatchers.jsonPath("$.price").value(12.7d))
                .andExpect(MockMvcResultMatchers.jsonPath("$.lastUpdate")
                        .value("2021-04-19 10:10:10"));
    }

    @Test
    public void findById_WithWrongId_ReturnsStockNotFoundException() throws Exception {
        Mockito.when(service.findById(stockId)).thenThrow(
                new StockNotFoundException(expectedErrMsg)
        );
        this.mockMvc
                .perform(MockMvcRequestBuilders.get(baseUri + "/" +  stockId))
                .andExpect(result -> assertTrue(result.getResolvedException() instanceof StockNotFoundException))
                .andExpect(result -> assertEquals(expectedErrMsg,
                        Objects.requireNonNull(result.getResolvedException()).getMessage()));
    }

    @Test
    public void createStock_ReturnsStock() throws Exception {
        StockReadDto expected = new StockReadDto(stockId, "Bilbo Baggins", 12.7d, "2021-04-19 10:10:10");
        StockCreateDto inputParam = new StockCreateDto("Bilbo Baggins", 12.7d);
        Mockito.when(service.createStock(inputParam)).thenReturn(expected);
        String jsonStr = ow.writeValueAsString(inputParam);

        this.mockMvc
                .perform(MockMvcRequestBuilders
                        .post(baseUri).content(jsonStr).contentType(MediaType.APPLICATION_JSON)
                )
                .andExpect(status().isOk())
                .andExpect(MockMvcResultMatchers.jsonPath("$.id").value(stockId))
                .andExpect(MockMvcResultMatchers.jsonPath("$.name").value("Bilbo Baggins"))
                .andExpect(MockMvcResultMatchers.jsonPath("$.price").value(12.7d))
                .andExpect(MockMvcResultMatchers.jsonPath("$.lastUpdate")
                        .value("2021-04-19 10:10:10"));
    }

    @Test
    public void deleteStock_ReturnsAccepted() throws Exception
    {
        this.mockMvc.perform( MockMvcRequestBuilders.delete(baseUri + "/{id}", stockId) )
                .andExpect(status().isNoContent());
    }

    @Test
    public void deleteStock_WithWrongId_ReturnsStockNotFoundException() throws Exception {
        Mockito.doThrow(new StockNotFoundException(expectedErrMsg))
                .when(service).deleteStock(stockId);

        this.mockMvc
                .perform(MockMvcRequestBuilders.delete(baseUri + "/{id}", stockId))
                .andExpect(result -> assertTrue(result.getResolvedException() instanceof StockNotFoundException))
                .andExpect(result -> assertEquals(expectedErrMsg,
                        Objects.requireNonNull(result.getResolvedException()).getMessage()));
    }

}
