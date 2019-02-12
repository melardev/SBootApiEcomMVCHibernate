package com.melardev.spring.shoppingcartweb.dtos.response.shared;


import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

public class PageMeta {
    boolean hasNextPage;
    boolean hasPrevPage;

    int requestedPageSize; // max cartItems per page

    int currentPageNumber;
    int totalPagesCount; // number of pages

    int currentItemsCount; // cartItems in this page
    long totalItemsCount; // total cartItems in total
    long offset;

    int nextPageNumber;
    int prevPageNumber;

    String nextPageUrl;
    String prevPageUrl;

    public PageMeta() {
    }


    public static PageMeta build(Page resourcePage, String basePath) {
        PageMeta pageMeta = new PageMeta();
        Pageable pageable = resourcePage.getPageable();

        pageMeta.setTotalItemsCount(resourcePage.getTotalElements());
        pageMeta.setOffset(pageable.getOffset());
        pageMeta.setRequestedPageSize(pageable.getPageSize());
        pageMeta.setCurrentItemsCount(resourcePage.getContent().size());
        pageMeta.setTotalPagesCount(resourcePage.getTotalPages());

        pageMeta.setCurrentPageNumber(resourcePage.getNumber() + 1);

        pageMeta.setHasNextPage(resourcePage.hasNext());
        pageMeta.setHasPrevPage(resourcePage.hasPrevious());

        if (resourcePage.hasNext()) {
            pageMeta.setNextPageNumber(resourcePage.getPageable().next().getPageNumber() + 1);
            pageMeta.setNextPageUrl(String.format("%s?page_size=%d&page=%d",
                    basePath, pageMeta.getRequestedPageSize(), pageMeta.getNextPageNumber()));
        } else {
            pageMeta.setNextPageNumber(pageMeta.getTotalPagesCount());
            pageMeta.setNextPageUrl(String.format("%s?page_size=%d&page=%d",
                    basePath, pageMeta.getRequestedPageSize(), pageMeta.getNextPageNumber()));
        }
        if (resourcePage.hasPrevious()) {
            pageMeta.setPrevPageNumber(resourcePage.getPageable().previousOrFirst().getPageNumber() + 1);

            pageMeta.setPrevPageUrl(String.format("%s?page_size=%d&page=%d",
                    basePath, pageMeta.getRequestedPageSize(),
                    pageMeta.getPrevPageNumber()));
        } else {
            pageMeta.setPrevPageNumber(1);
            pageMeta.setPrevPageUrl(String.format("%s?page_size=%d&page=%d",
                    basePath, pageMeta.getRequestedPageSize(), pageMeta.getPrevPageNumber()));
        }

        return pageMeta;
    }

    public void setPrevPageUrl(String prevPageUrl) {
        this.prevPageUrl = prevPageUrl;
    }

    public int getPrevPageNumber() {
        return prevPageNumber;
    }

    private void setPrevPageNumber(int prevPageNumber) {
        this.prevPageNumber = prevPageNumber;
    }

    private void setHasNextPage(boolean hasNext) {
        this.hasNextPage = hasNext;
    }

    public void setOffset(long offset) {
        this.offset = offset;
    }

    public long getOffset() {
        return offset;
    }

    public void setRequestedPageSize(int requestedPageSize) {
        this.requestedPageSize = requestedPageSize;
    }

    public int getRequestedPageSize() {
        return requestedPageSize;
    }

    public void setCurrentItemsCount(int currentItemsCount) {
        this.currentItemsCount = currentItemsCount;
    }

    public int getCurrentItemsCount() {
        return currentItemsCount;
    }

    public void setTotalPagesCount(int totalPagesCount) {
        this.totalPagesCount = totalPagesCount;
    }

    public int getTotalPagesCount() {
        return totalPagesCount;
    }

    public void setNextPageNumber(int nextPageNumber) {
        this.nextPageNumber = nextPageNumber;
    }

    public int getNextPageNumber() {
        return nextPageNumber;
    }

    public void setNextPageUrl(String nextPageUrl) {
        this.nextPageUrl = nextPageUrl;
    }

    public String getNextPageUrl() {
        return nextPageUrl;
    }

    public void setHasPrevPage(boolean hasPrevPage) {
        this.hasPrevPage = hasPrevPage;
    }

    public boolean getHasPrevPage() {
        return hasPrevPage;
    }

    public void setTotalItemsCount(long totalItemsCount) {
        this.totalItemsCount = totalItemsCount;
    }

    public long getTotalItemsCount() {
        return totalItemsCount;
    }

    public void setCurrentPageNumber(int currentPageNumber) {
        this.currentPageNumber = currentPageNumber;
    }

    public int getCurrentPageNumber() {
        return currentPageNumber;
    }
}
