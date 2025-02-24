package com.bookspot.batch.data.file.csv;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public class ConvertedStockCsvData {
    private String isbn;
    private Integer subjectCodePrefix;
    private int numberOfBooks;
    private int loanCount;
}
