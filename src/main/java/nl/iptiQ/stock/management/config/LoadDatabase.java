package nl.iptiQ.stock.management.config;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.RequiredArgsConstructor;
import lombok.extern.java.Log;
import nl.iptiQ.stock.management.entity.StockEntity;
import nl.iptiQ.stock.management.mapper.StockMapper;
import nl.iptiQ.stock.management.repository.StockRepository;
import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.io.IOException;
import java.io.InputStream;
import java.util.List;

@Log
@Configuration
@RequiredArgsConstructor
public class LoadDatabase {
    private final StockMapper stockMapper;

    @Bean
    public CommandLineRunner initDatabase(StockRepository repository) throws IOException {
        ObjectMapper mapper = new ObjectMapper();
        TypeReference<List<StockEntity>> typeReference = new TypeReference<>(){};
        InputStream inputStream = TypeReference.class.getResourceAsStream("/data/stocks.json");
        List<StockEntity> stockCreateDtoList = mapper.readValue(inputStream, typeReference);

        return args -> {
            stockCreateDtoList.forEach(
                    entity -> log.info("Preloading " + repository.save(entity))
            );
        };
    }
}
