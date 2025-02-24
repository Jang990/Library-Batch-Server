package com.bookspot.batch.step.service.memory.book;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public class BookMemoryData {
    private Integer subjectCode;
    private int loanCount;

    public void increase(int loanCount) {
        this.loanCount += loanCount;
    }
}
