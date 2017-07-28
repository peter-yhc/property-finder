package org.pyhc.propertyfinder.controller.model;

import lombok.Data;

import java.io.Serializable;

@Data
public class Page implements Serializable {

    long totalElements;
    int totalPages;
    int pageSize;
    int number;
    boolean first;
    boolean last;

}
