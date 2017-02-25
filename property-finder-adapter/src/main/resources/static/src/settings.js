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
    var locationTextSplit = locationText.split(" ");
    var suburb = locationTextSplit[0];
    var state = locationTextSplit[1].slice(0, -1);
    var postcode = locationTextSplit[2];
    $.ajax({
        url: "/settings/locations",
        type: "DELETE",
        data: JSON.stringify({
            "suburb": suburb,
            "state": state,
            "postcode": postcode
        }),
        contentType: "application/json",
        success: function () {
            $("#pf-saved-searches-item-" + index).remove();
        },
        error: function(error) {
            console.log("Failed to delete saved search: " + error);
        }
    });
});
