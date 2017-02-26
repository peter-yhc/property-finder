package org.pyhc.propertyfinder.controller.form;


import lombok.Getter;
import lombok.Setter;
import org.pyhc.propertyfinder.settings.SearchLocation;

@Getter
@Setter
public class SearchLocationForm {
    private String content;

    public SearchLocation parseData() {
        String[] contentSplit = content.split(" ");
        String suburb = contentSplit[0];
        String state = contentSplit[1].replace(",", "");
        String postcode = contentSplit[2];
        return SearchLocation.builder().suburb(suburb).state(state).postcode(Integer.parseInt(postcode)).build();
    }
}
