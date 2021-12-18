define(["jquery", "jqw/jqx-all", "jscomponents/SelectorEditor"], function ($, jqw, selector) {
	var that = this;
	selector.createEditor = function (response) {
		$("#nameInput").jqxInput({theme: 'darkblue', value: ''});
		$("#longSideInput").jqxInput({theme: 'darkblue', value: ''});
		$("#shortSideInput").jqxInput({theme: 'darkblue', value: ''});
		var url = "/portfolio/loadAll";
		// prepare the data
		var source =
				{
					datatype: "json",
					datafields: [
						{name: 'name'},
						{name: 'id'}
					],
					url: url,
					async: false
				};
		var dataAdapter = new $.jqx.dataAdapter(source);
		$("#portfolioInput").jqxDropDownList({
			source: dataAdapter, theme: 'darkblue', displayMember: "name", valueMember: "id", width: 200, height: 25
		});
		url = "/instrument/loadAll";
		// prepare the data
		source =
				{
					datatype: "json",
					datafields: [
						{name: 'isin'},
						{name: 'id'}
					],
					url: url,
					async: false
				};
		dataAdapter = new $.jqx.dataAdapter(source);
		$("#instrumentInput").jqxDropDownList({
			source: dataAdapter, theme: 'darkblue', displayMember: "isin", valueMember: "id", width: 200, height: 25
		});
		$("#validFromInput").jqxDateTimeInput({disabled: true, theme: 'darkblue', value: null});
		if (response) {
			$("#nameInput").jqxInput({value: response.name});
			$("#validFromInput").jqxDateTimeInput({disabled: true, theme: 'darkblue', value: new Date(response.validFrom)});
			$("#longSideInput").jqxInput({value: response.longSide});
			$("#shortSideInput").jqxInput({value: response.shortSide});
			$("#instrumentInput").jqxDropDownList('selectItem',response.instrument.id);
			$("#portfolioInput").jqxDropDownList('selectItem', response.portfolioId);
		}
		else
			response = {};
		$("#editor").css("visibility", "visible");
		$("#selector").jqxDropDownList('clearSelection');
		$("#saveButton").jqxButton({theme: 'darkblue', height: 27});
		$("#saveButton").on('click', function (event) {
			response.name = $("#nameInput").val();
			response.shortSide = $("#shortSideInput").val();
			response.longSide = $("#longSideInput").val();
			response.portfolioId = $("#portfolioInput").val();
			$.ajax({
				url: "/instrument/loadById/" + $("#instrumentInput").val() + "/" + (new Date()).getTime()
				, async: false
				, success: function (instrument) {
					response.instrument = instrument;
				}
			});
			if (!response.id)
				response.id = $("#nameInput").val();
			jQuery.ajax({
				'type': 'POST',
				'url': 'position/create',
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
