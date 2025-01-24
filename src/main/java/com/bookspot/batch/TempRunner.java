package com.bookspot.batch;

import com.bookspot.batch.step.writer.file.stock.StockCsvMetadataCreator;
import com.bookspot.batch.step.writer.file.stock.StockFilenameElement;
import com.bookspot.batch.step.writer.file.stock.StockFilenameUtil;
import lombok.RequiredArgsConstructor;
import org.springframework.batch.core.Job;
import org.springframework.batch.core.JobParameters;
import org.springframework.batch.core.JobParametersBuilder;
import org.springframework.batch.core.JobParametersInvalidException;
import org.springframework.batch.core.launch.JobLauncher;
import org.springframework.batch.core.repository.JobExecutionAlreadyRunningException;
import org.springframework.batch.core.repository.JobInstanceAlreadyCompleteException;
import org.springframework.batch.core.repository.JobRestartException;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Service;

import java.io.File;
import java.time.LocalDateTime;

@Service
@RequiredArgsConstructor
public class TempRunner implements CommandLineRunner {
    // 임시 코드
    private final JobLauncher jobLauncher;
    private final Job librarySyncJob;
    private final Job stockSyncJob;

    @Override
    public void run(String... args) throws Exception {
        jobLauncher.run(librarySyncJob, new JobParametersBuilder()
                .addLocalDateTime("tempParam", LocalDateTime.now())
                .toJobParameters());

        File directory = new File(StockCsvMetadataCreator.DIRECTORY_NAME);
        for (String fileName : directory.list()) {
            runTempJob(fileName);
        }
    }

    private void runTempJob(String fileName) throws JobExecutionAlreadyRunningException, JobRestartException, JobInstanceAlreadyCompleteException, JobParametersInvalidException {
        String filePath = StockCsvMetadataCreator.DIRECTORY_NAME.concat("/").concat(fileName);
        StockFilenameElement metadata = StockFilenameUtil.parse(fileName);
        System.out.println("파일 경로 => "+ filePath);

        JobParameters jobParameters = new JobParametersBuilder()
                .addLocalDateTime("tempParam", LocalDateTime.now())

                .addString("filePath", filePath)
                .addLong("libraryId", metadata.libraryId())
                .addLocalDate("referenceDate", metadata.referenceDate())
                .toJobParameters();

        jobLauncher.run(stockSyncJob, jobParameters);
    }
}
