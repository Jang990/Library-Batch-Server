package com.bookspot.batch.step;

import com.bookspot.batch.stock.data.CurrentLibrary;
import com.bookspot.batch.stock.data.LibraryStock;
import com.bookspot.batch.stock.data.LibraryStockCsvData;
import com.bookspot.batch.stock.data.StockFileData;
import com.bookspot.batch.step.processor.csv.stock.LibraryStockProcessor;
import com.bookspot.batch.step.processor.crawler.stock.StockFileInfoParser;
import lombok.RequiredArgsConstructor;
import org.springframework.batch.core.Step;
import org.springframework.batch.core.repository.JobRepository;
import org.springframework.batch.core.step.builder.StepBuilder;
import org.springframework.batch.item.ItemWriter;
import org.springframework.batch.item.database.JdbcBatchItemWriter;
import org.springframework.batch.item.database.JdbcPagingItemReader;
import org.springframework.batch.item.file.MultiResourceItemReader;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.transaction.PlatformTransactionManager;

@Configuration
@RequiredArgsConstructor
public class StockStepConfig {
    private final JobRepository jobRepository;
    private final PlatformTransactionManager platformTransactionManager;

    private final MultiResourceItemReader<LibraryStockCsvData> multiBookStockCsvFileReader;
    private final LibraryStockProcessor LibraryStockProcessor;
    private final JdbcBatchItemWriter<LibraryStock> libraryStockWriter;

    private final JdbcPagingItemReader<CurrentLibrary> libraryStockDataReader;
    private final StockFileInfoParser stockFileInfoParser;
    private final ItemWriter<StockFileData> stockFileDownloaderWriter;

    @Bean
    public Step libraryStockStep() {
        return new StepBuilder(StockStepConst.STEP_NAME, jobRepository)
                .<LibraryStockCsvData, LibraryStock>chunk(StockStepConst.CHUNK_SIZE, platformTransactionManager)
                .reader(multiBookStockCsvFileReader)
                .processor(LibraryStockProcessor)
                .writer(libraryStockWriter)
                .build();
    }

    @Bean
    public Step stockFileDownloadStep() {
        return new StepBuilder(StockStepConst.DOWNLOAD_STEP_NAME, jobRepository)
                .<CurrentLibrary, StockFileData>chunk(StockStepConst.DOWNLOAD_CHUNK_SIZE, platformTransactionManager)
                .reader(libraryStockDataReader)
                .processor(stockFileInfoParser)
                .writer(stockFileDownloaderWriter)
                .build();
    }

}
