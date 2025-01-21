package com.bookspot.batch.library.file;

import com.bookspot.batch.global.file.FileFormat;
import com.bookspot.batch.global.file.FileMetadata;

public class LibraryExcelConst {
    private static final String DIRECTORY_NAME = "bookSpotFiles/library";
    private static final String FILE_NAME = "library_list";

    protected static final FileMetadata metadata = new FileMetadata(FILE_NAME, DIRECTORY_NAME, FileFormat.EXCEL);
}
