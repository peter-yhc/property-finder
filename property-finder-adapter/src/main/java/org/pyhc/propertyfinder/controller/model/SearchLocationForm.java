package org.pyhc.propertyfinder.controller.model;


import lombok.Getter;
import lombok.Setter;
import org.pyhc.propertyfinder.settings.SuburbDetails;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

@Getter
@Setter
public class SearchLocationForm {
    private String content;

    public SuburbDetails parseData() {
        Pattern pattern = Pattern.compile("([A-Za-z ]+), (NSW|WA|NT|QLD|SA|TA|VIC) ([0-9]{4})");
        Matcher matcher = pattern.matcher(content);
        if (!matcher.matches()) {
            return null;
        }
        String suburb = matcher.group(1);
        String state = matcher.group(2);
        String postcode = matcher.group(3);
        return SuburbDetails.builder().suburbName(suburb).state(state).postcode(Integer.parseInt(postcode)).build();
    }
}
