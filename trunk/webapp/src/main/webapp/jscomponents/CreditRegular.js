define(["jquery", "jqw/jqx-all", "jscomponents/SelectorEditor"], function ($, jqw, selector) {
	var that = this;
	selector.createEditor = function (response) {
		$("#nameInput").jqxInput({theme: 'darkblue', value: ''});
		$("#tenorMonthsInput").jqxInput({theme: 'darkblue', value: 0});
		$("#interestRateInput").jqxInput({theme: 'darkblue', value: 0});
		$("#currencyInput").jqxInput({theme: 'darkblue', value: ''});
		$("#marketInput").jqxInput({theme: 'darkblue', value: ''});
		$("#issueInput").jqxDateTimeInput({ theme: 'darkblue', value: null});
		$("#maturityInput").jqxDateTimeInput({ theme: 'darkblue', value: null});
		$("#validFromInput").jqxDateTimeInput({disabled: true, theme: 'darkblue', value: null});
		if (response) {
			$("#nameInput").jqxInput({value: response.isin});
			$("#validFromInput").jqxDateTimeInput({disabled: true, theme: 'darkblue', value: new Date(response.validFrom)});
			$("#tenorMonthsInput").jqxInput({value: response.tenorMonths});
			$("#interestRateInput").jqxInput({value: response.interestRate});
			$("#currencyInput").jqxInput({value: response.currency});
			$("#marketInput").jqxInput({value: response.market});
			$("#issueInput").jqxDateTimeInput({ theme: 'darkblue', value: new Date(response.issue)});
			$("#maturityInput").jqxDateTimeInput({ theme: 'darkblue', value: new Date(response.maturity)});
		}
		else
			response = {};
		$("#editor").css("visibility", "visible");
		$("#selector").jqxDropDownList('clearSelection');
		$("#saveButton").jqxButton({theme: 'darkblue', height: 27});
		$("#saveButton").on('click', function (event) {
			response.name = $("#nameInput").val();
			response.tenorMonths = $("#tenorMonthsInput").val();
			response.interestRate = $("#interestRateInput").val();
			response.currency = $("#currencyInput").val();
			response.market = $("#marketInput").val();
			response.issue = new Date($("#issueInput").val()).getTime();
			response.maturity = new Date($("#maturityInput").val()).getTime();
		
			if (!response.id)
				response.id = $("#nameInput").val();
			jQuery.ajax({
				'type': 'POST',
				'url': 'instrument/create',
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
