package com.bookspot.batch.step.processor.crawler.book;

import com.bookspot.batch.data.Book_;
import com.bookspot.batch.global.crawler.aladdin.AladdinCrawler;
import com.bookspot.batch.step.service.Isbn13MemoryData;
import lombok.RequiredArgsConstructor;
import org.springframework.batch.item.ItemProcessor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class BookInfoParser implements ItemProcessor<Isbn13MemoryData, Book_> {
    private final AladdinCrawler aladdinCrawler;
    private final BookDataMapper dataMapper;

    @Override
    public Book_ process(Isbn13MemoryData item) throws Exception {
        return dataMapper.transform(
                item.bookId(),
                aladdinCrawler.findBookDetail(item.isbn13()));
    }
}
