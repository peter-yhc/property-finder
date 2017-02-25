$(document).ready(
    getSearchableLocationsForAutocomplete()
);

function getSearchableLocationsForAutocomplete() {
    $.get("/settings/locations",
        function (searchableLocations) {
            $("#pf-search-location-input").autocomplete({
                source: searchableLocations
            });
        }
    );
}
