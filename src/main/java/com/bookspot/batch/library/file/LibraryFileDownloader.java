package com.bookspot.batch.library.file;

import com.bookspot.batch.global.crawler.naru.NaruRequestCreator;
import com.bookspot.batch.global.file.NaruFileDownloader;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.io.File;

@Slf4j
@Service
@RequiredArgsConstructor
public class LibraryFileDownloader {
    private final NaruRequestCreator requestCreator;
    private final NaruFileDownloader naruFileDownloader;

    private static final String URL = "https://www.data4library.kr/libDataDownload";

    public void download() {
        naruFileDownloader.downloadSync(
                URL,
                requestCreator.create(),
                LibraryExcelConst.metadata);
    }

    public void delete() {
        File file = new File(LibraryExcelConst.metadata.absolutePath());

        boolean isDeleted = false;
        if(file.exists())
            isDeleted = file.delete();

        if(isDeleted)
            log.info("도서관 CSV 파일 삭제 완료");
        else
            log.warn("도서관 CSV 파일 삭제 실패");
    }
}
