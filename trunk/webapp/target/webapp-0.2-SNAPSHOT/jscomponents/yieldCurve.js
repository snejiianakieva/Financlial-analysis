define(["jquery", "jqw/jqx-all", "jscomponents/SelectorEditor"], function ($, jqw, selector) {
	var that = this;
	selector.createEditor = function (response) {
		$("#nameInput").jqxInput({theme: 'darkblue', value: ''});
		$("#zeroYieldThreeMonthsInput").jqxInput({theme: 'darkblue', value: 0.0});
		$("#zeroYieldSixMonthsInput").jqxInput({theme: 'darkblue', value: 0.0});
		$("#zeroYieldOneYearInput").jqxInput({theme: 'darkblue', value: 0.0});
		$("#zeroYieldTwoYearsInput").jqxInput({theme: 'darkblue', value: 0.0});
		$("#zeroYieldFiveYearsInput").jqxInput({theme: 'darkblue', value: 0.0});
		$("#zeroYieldTenYearsInput").jqxInput({theme: 'darkblue', value: 0.0});
		$("#zeroYieldThirtyYearsInput").jqxInput({theme: 'darkblue', value: 0.0});
		$("#validFromInput").jqxDateTimeInput({ disabled: true,theme: 'darkblue', value: null});
		if (response) {
			$("#nameInput").jqxInput({value: response.id.currency});
			$("#zeroYieldThreeMonthsInput").jqxInput({value: response.zeroYieldThreeMonths});
			$("#zeroYieldSixMonthsInput").jqxInput({value: response.zeroYieldSixMonths});
			$("#zeroYieldOneYearInput").jqxInput({value: response.zeroYieldOneYear});
			$("#zeroYieldTwoYearsInput").jqxInput({value: response.zeroYieldTwoYears});
			$("#zeroYieldFiveYearsInput").jqxInput({value: response.zeroYieldFiveYears});
			$("#zeroYieldTenYearsInput").jqxInput({value: response.zeroYieldTenYears});
			$("#zeroYieldThirtyYearsInput").jqxInput({value: response.zeroYieldThirtyYears});
			$("#validFromInput").jqxDateTimeInput({ disabled: true,theme: 'darkblue', value: new Date(response.validFrom)});
		}
		else response={};
		$("#editor").css("visibility", "visible");
		$("#selector").jqxDropDownList('clearSelection');
		$("#saveButton").jqxButton({theme: 'darkblue', height: 27});
		$("#saveButton").on('click', function (event) {
			response.id.currency = $("#nameInput").val();
			response.zeroYieldThreeMonths = $("#zeroYieldThreeMonthsInput").val();
			response.zeroYieldSixMonths = $("#zeroYieldSixMonthsInput").val();
			response.zeroYieldOneYear = $("#zeroYieldOneYearInput").val();
			response.zeroYieldTwoYears = $("#zeroYieldTwoYearsInput").val();
			response.zeroYieldFiveYears = $("#zeroYieldFiveYearsInput").val();
			response.zeroYieldTenYears = $("#zeroYieldTenYearsInput").val();
			response.zeroYieldTirtyYears = $("#zeroYieldThhirtyYearsInput").val();
			jQuery.ajax({
				'type': 'POST',
				'url': 'yieldcurve/create',
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
