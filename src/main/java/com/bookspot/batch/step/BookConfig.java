package com.bookspot.batch.step;

import com.bookspot.batch.data.file.csv.LibraryStockCsvData;
import com.bookspot.batch.step.processor.csv.stock.IsbnValidationProcessor;
import com.bookspot.batch.step.processor.csv.stock.repository.BookRepository;
import com.bookspot.batch.step.service.memory.book.BookMemoryData;
import com.bookspot.batch.step.service.memory.book.InMemoryBookService;
import com.bookspot.batch.step.service.memory.book.InMemoryJdkBookService;
import com.bookspot.batch.step.service.memory.bookid.Isbn13MemoryData;
import com.bookspot.batch.step.service.memory.bookid.IsbnMemoryRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.batch.core.Step;
import org.springframework.batch.core.repository.JobRepository;
import org.springframework.batch.core.step.builder.StepBuilder;
import org.springframework.batch.item.ItemWriter;
import org.springframework.batch.item.database.JdbcBatchItemWriter;
import org.springframework.batch.item.database.builder.JdbcBatchItemWriterBuilder;
import org.springframework.batch.item.file.FlatFileItemReader;
import org.springframework.batch.item.file.MultiResourceItemReader;
import org.springframework.batch.item.support.CompositeItemWriter;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.transaction.PlatformTransactionManager;

import javax.sql.DataSource;
import java.util.List;

@Configuration
@RequiredArgsConstructor
public class BookConfig {
    private static final int BOOK_SYNC_CHUNK_SIZE = 5_000;

    private final JobRepository jobRepository;
    private final PlatformTransactionManager platformTransactionManager;

    private final MultiResourceItemReader<LibraryStockCsvData> multiBookStockCsvFileReader;
    private final IsbnValidationProcessor isbnValidationProcessor;

    private final InMemoryJdkBookService bookService;

    @Bean
    public Step libraryBookSyncStep() {
        return new StepBuilder("libraryBookSyncStep", jobRepository)
                .<LibraryStockCsvData, LibraryStockCsvData>chunk(BOOK_SYNC_CHUNK_SIZE, platformTransactionManager)
                .reader(multiBookStockCsvFileReader)
                .processor(isbnValidationProcessor)
                .writer(memoryIsbnWriter())
                .build();
    }

    /*// 도서관 재고 csv -> book 테이블 저장
    @Bean
    public JdbcBatchItemWriter<LibraryStockCsvData> stockBookWriter() {
        JdbcBatchItemWriter<LibraryStockCsvData> writer = new JdbcBatchItemWriterBuilder<LibraryStockCsvData>()
                .dataSource(dataSource)
                .sql("""
                        INSERT IGNORE INTO book
                        (isbn13, classification)
                        VALUES(?, ?);
                        """)
                .itemPreparedStatementSetter(
                        (book, ps) -> {
                            ps.setString(1, book.getIsbn());
                            ps.setString(2, book.getSubjectCode());
                        })
                .assertUpdates(false)
                .build();
        return writer;
    }*/

    // 새로 등록된 책을 메모리에 등록
    @Bean
    public ItemWriter<LibraryStockCsvData> memoryIsbnWriter() {
        return chunk -> chunk.getItems().stream()
                .forEach(book -> {
                    int loanCount = book.getLoanCount() == null ? 0 : book.getLoanCount();

                    if(bookService.contains(book.getIsbn()))
                        bookService.increase(book.getIsbn(), loanCount);
                    else
                        bookService.add(book.getIsbn(), new BookMemoryData(book.getSubjectCode(), loanCount));
                });
    }
}
