define(["jquery", "jqw/jqx-all", "jscomponents/SelectorEditor"], function ($, jqw, selector) {
	var that = this;
	selector.createEditor = function (response) {
		$("#nameInput").jqxInput({theme: 'darkblue', value: ''});
		$("#currencyInput").jqxInput({theme: 'darkblue', value: ''});
		$("#validFromInput").jqxDateTimeInput({ disabled: true,theme: 'darkblue', value: null});
		if (response) {
			$("#nameInput").jqxInput({value: response.name});
			$("#currencyInput").jqxInput({value: response.currency});
			$("#validFromInput").jqxDateTimeInput({ disabled: true,theme: 'darkblue', value: new Date(response.validFrom)});
		}
		else response={};
		$("#editor").css("visibility", "visible");
		$("#selector").jqxDropDownList('clearSelection');
		$("#saveButton").jqxButton({theme: 'darkblue', height: 27});
		$("#saveButton").on('click', function (event) {
			response.name = $("#nameInput").val();
			if(!response.id)response.id = $("#nameInput").val();
			response.currency = $("#currencyInput").val();
			jQuery.ajax({
				'type': 'POST',
				'url': 'portfolio/create',
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
