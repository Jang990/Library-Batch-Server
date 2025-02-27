package com.bookspot.batch.step;

import com.bookspot.batch.step.processor.csv.book.YearParser;
import com.bookspot.batch.step.reader.*;
import lombok.RequiredArgsConstructor;
import org.springframework.batch.core.configuration.annotation.StepScope;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import javax.sql.DataSource;
import java.io.IOException;

@Configuration
@RequiredArgsConstructor
public class ReaderConfig {

    @Bean
    @StepScope
    public StockCsvFileReader stockCsvFileReader(
            @Value("#{jobParameters['filePath']}") String filePath,
            YearParser yearParser) throws Exception {
        return new StockCsvFileReader(filePath, yearParser);
    }

    @Bean
    public MultiStockCsvFileReader multiStockCsvFileReader(YearParser yearParser) throws IOException {
        return new MultiStockCsvFileReader(yearParser);
    }

    @Bean
    public IsbnReader isbnReader(
            DataSource dataSource,
            IsbnIdPagingQueryProviderFactory isbnIdPagingQueryProviderFactory) throws Exception {
        return new IsbnReader(
                dataSource,
                isbnIdPagingQueryProviderFactory.getObject(),
                InMemoryIsbnStepConfig.CHUNK_SIZE
        );
    }

    @Bean
    public IsbnIdReader isbnIdReader(
            DataSource dataSource,
            IsbnIdPagingQueryProviderFactory isbnIdPagingQueryProviderFactory) throws Exception {
        return new IsbnIdReader(
                dataSource,
                isbnIdPagingQueryProviderFactory.getObject(),
                InMemoryIsbnIdStepConfig.WARM_UP_CHUNK_SIZE
        );
    }

    @Bean
    public IsbnIdPagingQueryProviderFactory isbnIdPagingQueryProviderFactory(DataSource dataSource) {
        return new IsbnIdPagingQueryProviderFactory(dataSource);
    }
}
