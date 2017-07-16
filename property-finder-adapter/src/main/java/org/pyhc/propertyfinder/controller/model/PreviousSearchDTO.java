package org.pyhc.propertyfinder.controller.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import org.pyhc.propertyfinder.settings.SuburbDetails;

import java.util.List;

@Data
@AllArgsConstructor
public class PreviousSearchDTO {

    private List<SuburbDetails> suburbs;

}
