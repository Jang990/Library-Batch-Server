package com.bookspot.batch.step.service.memory.book;

import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.Map;

@Service
public class InMemoryJdkBookService implements InMemoryBookService {
    private static HashMap<Long, BookMemoryData> store = new HashMap<>();

    public void add(String isbn13, BookMemoryData data) {
        store.put(Long.parseLong(isbn13), data);
    }

    @Override
    public BookMemoryData get(String isbn13) {
        return store.get(Long.parseLong(isbn13));
    }

    public boolean contains(String isbn13) {
        return store.containsKey(Long.parseLong(isbn13));
    }

    public void clearAll() {
        store.clear();
    }

    public Map<Long, BookMemoryData> getData() {
        return store;
    }

    public void increase(String isbn, int loanCount) {
        BookMemoryData data = get(isbn);
        if(data == null)
            throw new IllegalArgumentException("찾을 수 없는 ISBN");
        data.increase(loanCount);
    }
}
