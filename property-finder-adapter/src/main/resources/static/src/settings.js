$(document).ready(function () {
    getSearchableLocationsForAutocomplete();
});

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
    let savedSearchUuid = event.target.closest('a').getAttribute('pf-uuid');
    let index = event.target.closest('a').getAttribute('pf-index');
    $.ajax({
        url: "/settings/locations/" + savedSearchUuid,
        type: "DELETE",
        contentType: "application/json",
        success: function () {
            $("#pf-saved-searches-item-" + index).remove();
        },
        error: function (error) {
            console.log("Failed to delete saved search: " + error);
        }
    });
});

$("#pf-search-location-add").click(function (event) {
    event.preventDefault();
    let saveSearchInput = $("#pf-search-location-input").val();
    try {
        parseSearchLocationText(saveSearchInput);
        $("#pf-search-location-form").submit();
    } catch (err) {
        $("#pf-saved-searches-error span").text("Format should be 'Suburb State, PostCode' (ex. Sydney NSW, 2000)");
        $("#pf-saved-searches-error").show();
    }
});

let parseSearchLocationText = function (locationText) {
    let parsedText = locationText.match(/([A-Za-z ]+) (NSW|WA|NT|QLD|SA|TA|VIC), ([0-9]{4})/);
    if (parsedText.length <= 3) {
        throw "Unable to parse: " + locationText;
    }
    return {
        suburbName: parsedText[1],
        state: parsedText[2],
        postcode: parsedText[3]
    };
};