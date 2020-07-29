package com.dusan.taxiservice.web.api.rest.model.response;

import lombok.AllArgsConstructor;
import lombok.Getter;

import java.util.List;

@Getter
@AllArgsConstructor
public class PageResponseWrapper<T> {

    private List<T> content;
    private PageMetadata metadata;


    @Getter
    public static class PageMetadata {

        private int pageNumber;
        private int pageSize;
        private long totalNumberOfElements;
        private int totalNumberOfPages;

        private PageMetadata(int pageNumber, int pageSize, long totalNumberOfElements, int totalNumberOfPages) {
            this.pageNumber = pageNumber;
            this.pageSize = pageSize;
            this.totalNumberOfElements = totalNumberOfElements;
            this.totalNumberOfPages = totalNumberOfPages;
        }

        public static PageMetadata of(int number, int size, long totalNumberOfElements, int totalNumberOfPages) {
            return new PageMetadata(number, size, totalNumberOfElements, totalNumberOfPages);
        }
    }
}
