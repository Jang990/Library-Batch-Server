package com.bookspot.batch.step;

import com.bookspot.batch.step.processor.csv.book.YearParser;
import com.bookspot.batch.step.reader.StockCsvFileReader;
import lombok.RequiredArgsConstructor;
import org.springframework.batch.core.configuration.annotation.StepScope;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import javax.sql.DataSource;

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
}
