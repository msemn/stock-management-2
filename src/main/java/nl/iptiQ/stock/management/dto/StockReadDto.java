package nl.iptiQ.stock.management.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class StockReadDto {
    private Long id;
    private String name;
    private Double price;
    private String lastUpdate;

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        StockReadDto that = (StockReadDto) o;
        return id.equals(that.id);
    }
}
