package org.pyhc.propertyfinder.controller.model;

import lombok.Data;
import lombok.EqualsAndHashCode;
import org.pyhc.propertyfinder.suburb.SuburbDetails;

import java.util.List;

@Data
@EqualsAndHashCode(callSuper = false)
public class PreviousSearchDTO extends PageResource {

    private List<SuburbDetails> suburbs;

    public PreviousSearchDTO(List<SuburbDetails> suburbs) {
        this.suburbs = suburbs;
//        setPagination(suburbs);
    }
}
