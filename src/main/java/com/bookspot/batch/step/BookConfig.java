package com.bookspot.batch.step;

import com.bookspot.batch.data.file.csv.ConvertedStockCsvData;
import com.bookspot.batch.data.file.csv.StockCsvData;
import com.bookspot.batch.step.service.memory.book.BookMemoryData;
import com.bookspot.batch.step.service.memory.book.InMemoryJdkBookService;
import lombok.RequiredArgsConstructor;
import org.springframework.batch.core.Step;
import org.springframework.batch.core.repository.JobRepository;
import org.springframework.batch.core.step.builder.StepBuilder;
import org.springframework.batch.item.ItemWriter;
import org.springframework.batch.item.file.MultiResourceItemReader;
import org.springframework.batch.item.support.CompositeItemProcessor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.transaction.PlatformTransactionManager;

@Configuration
@RequiredArgsConstructor
public class BookConfig {
    private static final int BOOK_SYNC_CHUNK_SIZE = 5_000;

    private final JobRepository jobRepository;
    private final PlatformTransactionManager platformTransactionManager;

    private final MultiResourceItemReader<StockCsvData> multiBookStockCsvFileReader;
    private final CompositeItemProcessor<StockCsvData, ConvertedStockCsvData> stockCsvDataProcessor;

    private final InMemoryJdkBookService bookService;

    @Bean
    public Step loadBookToMemoryStep() {
        return new StepBuilder("loadBookToMemoryStep", jobRepository)
                .<StockCsvData, ConvertedStockCsvData>chunk(BOOK_SYNC_CHUNK_SIZE, platformTransactionManager)
                .reader(multiBookStockCsvFileReader)
                .processor(stockCsvDataProcessor)
                .writer(memoryIsbnWriter())
                .build();
    }

    // 새로 등록된 책을 메모리에 등록
    @Bean
    public ItemWriter<ConvertedStockCsvData> memoryIsbnWriter() {
        return chunk -> chunk.getItems().stream()
                .forEach(book -> {
                    int loanCount = book.getLoanCount();

                    if(bookService.contains(book.getIsbn()))
                        bookService.increase(book.getIsbn(), loanCount);
                    else
                        bookService.add(book.getIsbn(),
                                new BookMemoryData(book.getSubjectCodePrefix(), loanCount)
                        );
                });
    }
}
