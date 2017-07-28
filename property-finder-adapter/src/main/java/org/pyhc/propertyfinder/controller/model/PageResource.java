package org.pyhc.propertyfinder.controller.model;

import lombok.Getter;
import org.springframework.hateoas.ResourceSupport;

@Getter
abstract class PageResource extends ResourceSupport {

    private Page page = new Page();

    void setPagination(org.springframework.data.domain.Page page) {
        this.page.totalElements = page.getTotalElements();
        this.page.totalPages = page.getTotalPages();
        this.page.pageSize = page.getSize();
        this.page.first = page.isFirst();
        this.page.last = page.isLast();
        this.page.number = page.getNumber();
    }

    public Page getPage() {
        return page;
    }
}
