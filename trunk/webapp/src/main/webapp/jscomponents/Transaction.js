define(["jquery", "jqw/jqx-all", "jscomponents/SelectorEditor"], function ($, jqw, selector) {
	var that = this;
	selector.createEditor = function (response) {
		$("#nameInput").jqxInput({theme: 'darkblue', value: ''});
		$("#volumeInput").jqxInput({theme: 'darkblue', value: 0});
		$("#paidAmountInput").jqxInput({theme: 'darkblue', value: 0});
		$("#currencyInput").jqxInput({theme: 'darkblue', value: ''});
		$("#senderInput").jqxInput({theme: 'darkblue', value: ''});
		$("#receiverInput").jqxInput({theme: 'darkblue', value: ''});
		var url = "/position/loadAll";
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
		$("#positionInput").jqxDropDownList({
			source: dataAdapter, theme: 'darkblue', displayMember: "name", valueMember: "id", width: 200, height: 25
		});
		$("#validFromInput").jqxDateTimeInput({disabled: true, theme: 'darkblue', value: null});
		if (response) {
			$("#volumeInput").jqxInput({theme: 'darkblue', value: 0});
		$("#paidAmountInput").jqxInput({theme: 'darkblue', value: 0});
		$("#currencyInput").jqxInput({theme: 'darkblue', value: ''});
		$("#senderInput").jqxInput({theme: 'darkblue', value: ''});
		$("#receiverInput").jqxInput({theme: 'darkblue', value: ''});
			$("#nameInput").jqxInput({value: response.name});
			$("#validFromInput").jqxDateTimeInput({disabled: true, theme: 'darkblue', value: new Date(response.validFrom)});
			$("#volumeInput").jqxInput({value: response.volume});
			$("#paidAmountInput").jqxInput({value: response.paidAmount});
			$("#currencyInput").jqxInput({value: response.currency});
			$("#senderInput").jqxInput({value: response.sender});
			$("#receiverInput").jqxInput({value: response.receiver});
			$("#positionInput").jqxDropDownList('selectItem', response.positionId);
		}
		else
			response = {};
		$("#editor").css("visibility", "visible");
		$("#selector").jqxDropDownList('clearSelection');
		$("#saveButton").jqxButton({theme: 'darkblue', height: 27});
		$("#saveButton").on('click', function (event) {
			response.name = $("#nameInput").val();
			response.volume = $("#volumeInput").val();
			response.paidAmount = $("#paidAmountInput").val();
			response.currency = $("#currencyInput").val();
			response.sender = $("#senderInput").val();
			response.receiver = $("#receiverInput").val();
			response.positionId = $("#positionInput").val();
		
			if (!response.id)
				response.id = $("#nameInput").val();
			jQuery.ajax({
				'type': 'POST',
				'url': 'transaction/create',
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
