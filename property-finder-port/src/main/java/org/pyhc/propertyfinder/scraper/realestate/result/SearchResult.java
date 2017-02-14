package org.pyhc.propertyfinder.scraper.realestate.result;

import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import org.pyhc.propertyfinder.scraper.realestate.query.Query;

import java.util.List;

@Getter
@Builder
@AllArgsConstructor(access = AccessLevel.PRIVATE)
public class SearchResult {

    private Query nextPageLink;
    private List<? extends Query> profileLinks;

    public boolean hasNextPageLink() {
        return nextPageLink != null;
    }
}
