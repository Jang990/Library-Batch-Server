package com.bookspot.batch.stock.file;

import java.time.LocalDate;
import java.util.Objects;

public record StockFileData(String libraryCode, String filePath, LocalDate stockUpdatedAt) {
    public StockFileData {
        Objects.requireNonNull(libraryCode);
        Objects.requireNonNull(filePath);
        Objects.requireNonNull(stockUpdatedAt);
    }
}
