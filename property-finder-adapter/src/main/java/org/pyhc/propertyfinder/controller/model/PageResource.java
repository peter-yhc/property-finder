package org.pyhc.propertyfinder.controller.model;

import org.springframework.hateoas.ResourceSupport;

import java.util.Collection;

public abstract class PageResource extends ResourceSupport {

    private static final int PAGE_SIZE = 20;

    private Page page = new Page();

    public void setPagination(Collection<?> collection) {
        this.setTotalElements(collection.size());
        this.setPageSize(PAGE_SIZE);
        this.setTotalPages((int) Math.floor(collection.size() / PAGE_SIZE) + 1);
    }

    public Integer getTotalElements() {
        return page.totalElements;
    }

    public Integer getTotalPages() {
        return page.totalPages;
    }

    public Integer getPageSize() {
        return page.pageSize;
    }

    public String getNext() {
        return page.next;
    }

    public String getPrevious() {
        return page.previous;
    }

    public void setTotalElements(Integer totalElements) {
        this.page.totalElements = totalElements;
    }

    public void setTotalPages(Integer totalPages) {
        this.page.totalPages = totalPages;
    }

    public void setPageSize(Integer pageSize) {
        this.page.pageSize = pageSize;
    }

    public void setNext(String next) {
        this.page.next = next;
    }

    public void setPrevious(String previous) {
        this.page.previous = previous;
    }

    static class Page {
        Integer totalElements;
        Integer totalPages;
        Integer pageSize;
        String next;
        String previous;
    }
}
