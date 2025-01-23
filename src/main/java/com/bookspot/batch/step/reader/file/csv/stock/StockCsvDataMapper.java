package com.bookspot.batch.step.reader.file.csv.stock;

import com.bookspot.batch.step.processor.csv.book.YearParser;
import com.bookspot.batch.data.file.csv.LibraryStockCsvData;
import lombok.RequiredArgsConstructor;
import org.springframework.batch.item.file.mapping.FieldSetMapper;
import org.springframework.batch.item.file.transform.FieldSet;
import org.springframework.validation.BindException;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;

@RequiredArgsConstructor
public class StockCsvDataMapper implements FieldSetMapper<LibraryStockCsvData> {

    private static final DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");
    private final YearParser yearParser;

    @Override
    public LibraryStockCsvData mapFieldSet(FieldSet fieldSet) throws BindException {
        LibraryStockCsvData result = new LibraryStockCsvData();

        result.setId(toInt(read(fieldSet, LibraryStockCsvSpec.ID)));
        result.setTitle(read(fieldSet, LibraryStockCsvSpec.TITLE));
        result.setAuthor(read(fieldSet, LibraryStockCsvSpec.AUTHOR));
        result.setPublisher(read(fieldSet, LibraryStockCsvSpec.PUBLISHER));
        result.setPublicationYear(yearParser.parse(read(fieldSet, LibraryStockCsvSpec.PUBLICATION_YEAR)));
        result.setIsbn(read(fieldSet, LibraryStockCsvSpec.ISBN));
        result.setSetIsbn(read(fieldSet, LibraryStockCsvSpec.SET_ISBN));
        result.setAdditionalCode(read(fieldSet, LibraryStockCsvSpec.ADDITIONAL_CODE));
        result.setVolume(read(fieldSet, LibraryStockCsvSpec.VOLUME));
        result.setSubjectCode(read(fieldSet, LibraryStockCsvSpec.SUBJECT_CODE));
        result.setNumberOfBooks(toInt(read(fieldSet, LibraryStockCsvSpec.NUMBER_OF_BOOKS)));
        result.setLoanCount(toInt(read(fieldSet, LibraryStockCsvSpec.LOAN_COUNT)));
        result.setRegistrationDate(LocalDate.parse(read(fieldSet, LibraryStockCsvSpec.REGISTRATION_DATE), formatter));

        return result;
    }

    private String read(FieldSet fieldSet, LibraryStockCsvSpec spec) {
        return fieldSet.readString(spec.value());
    }

    private long toLong(String value) {
        return value.isBlank() ? 0 : Long.parseLong(value);
    }

    private int toInt(String value) {
        return value.isBlank() ? 0 : Integer.parseInt(value);
    }
}
