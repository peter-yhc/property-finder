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

$(".pf-saved-searches-delete-button").click(function (event) {
    var deleteButtonIdSplit = event.target.id.split("-");
    var index = deleteButtonIdSplit[deleteButtonIdSplit.length - 1];

    var locationText = $("#pf-saved-searches-item-" + index).text().trim();
    var searchLocationData = parseSearchLocationText(locationText);
    $.ajax({
        url: "/settings/locations",
        type: "DELETE",
        data: JSON.stringify(searchLocationData),
        contentType: "application/json",
        success: function () {
            $("#pf-saved-searches-item-" + index).remove();
        },
        error: function (error) {
            console.log("Failed to delete saved search: " + error);
        }
    });
});

var parseSearchLocationText = function (locationText) {
    var parsedText = locationText.match(/([A-Za-z ]+) (NSW|WA|NT|QLD|SA|TA|VIC), ([0-9]{4})/);
    return {
        suburbName: parsedText[1],
        state: parsedText[2],
        postcode: parsedText[3]
    };
};