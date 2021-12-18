define(["jquery", "jqw/jqx-all", "js/SelectorEditor"], function ($, jqw, selector) {
	var that = this;
	selector.createEditor = function (response) {
		$("#instrumentPriceInput").jqxInput({theme: 'darkblue', value: 0.0});
		$("#currencyInput").jqxInput({theme: 'darkblue', value: ''});
		$("#validFromInput").jqxDateTimeInput({ disabled: true,theme: 'darkblue', value: null});
		if (response) {
			$("#instrumentPriceInput").jqxInput({value: response.instrumentPrice});
			$("#currencyInput").jqxInput({value: response.currency});
			$("#validFromInput").jqxDateTimeInput({ disabled: true,theme: 'darkblue', value: new Date(response.validFrom)});
		}
		else
			response = {};
		$("#editor").css("visibility", "visible");
		$("#selector").jqxDropDownList('clearSelection');
		$("#saveButton").jqxButton({theme: 'darkblue', height: 27});
		$("#saveButton").on('click', function (event) {
			response.instrumentPrice = $("#instrumentPriceInput").val();
			response.currency = $("#currencyInput").val();
			
			if (!response.id)
				response.id =response.instrumentPrice.toString() + response.currency;
			jQuery.ajax({
				'type': 'POST',
				'url': 'instrumentPriceQuote/create',
				'contentType': 'application/json',
				'data': JSON.stringify(response),
				'dataType': 'json'
			});
		});
		$("#deleteButton").jqxButton({theme: 'darkblue', height: 27});
	};
	that.createSelector = selector.createSelector;
	return that;
});
