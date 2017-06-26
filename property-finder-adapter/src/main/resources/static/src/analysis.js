$(document).ready(function () {
    getSearchableLocationsForAutocomplete();
    $(".button-collapse").sideNav();
});

function getSearchableLocationsForAutocomplete() {
    $.get("/analysis/locations",
        function (searchableLocations) {
            console.log(JSON.stringify(searchableLocations));
            let reformedData = {};
            searchableLocations.forEach(function (x) {
                reformedData[x] = null;
            });
            $("#pf-search-location-input").autocomplete({
                data: reformedData,
                limit: 20, // The max amount of results that can be shown at once. Default: Infinity.
                onAutocomplete: function (val) {
                    // Callback function when value is autcompleted.
                },
                minLength: 1, // The minimum length of the input for the autocomplete to start. Default: 1.
            });
        }
    );
}

$(".pf-saved-searches-delete-button").click(function (event) {
    let savedSearchUuid = event.target.closest('a').getAttribute('pf-uuid');
    let index = event.target.closest('a').getAttribute('pf-index');
    $.ajax({
        url: "/analysis/locations/" + savedSearchUuid,
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
        $("#pf-saved-searches-error span").text("Format should be 'Suburb, State PostCode' (ex. Sydney, NSW 2000)");
        $("#pf-saved-searches-error").show();
    }
});

let parseSearchLocationText = function (locationText) {
    let parsedText = locationText.match(/([A-Za-z ]+), (NSW|WA|NT|QLD|SA|TA|VIC) ([0-9]{4})/);
    if (parsedText.length <= 3) {
        throw "Unable to parse: " + locationText;
    }
    return {
        suburbName: parsedText[1],
        state: parsedText[2],
        postcode: parsedText[3]
    };
};