define(["jquery", "jqw/jqx-all"], function ($, jqw) {
	var that = this;
	that.createSelector = function (entityType) {
		$("#createButton").jqxButton({theme: 'darkblue', height: 27});
		$("#createButton").css('visibility', 'visible');
		var url = "/" + entityType + "/loadAll";
		var name = 'name';
		var id = 'id';
		if (entityType === "yieldcurve") {
			name = 'currency';
			id = 'date';
		}
		;
		
		if (entityType === "instrument") {
			name = 'isin';
		}
		;

		if (entityType === "instrumentPriceQuote") {
			name = 'instrumentId';
			id = 'date';
		}
		;
		// prepare the data
		var source =
				{
					datatype: "json",
					datafields: [
						{name: name},
						{name: id}
					],
					url: url,
					async: false
				};
		var dataAdapter = new $.jqx.dataAdapter(source);
		// Create a jqxDropDownList
		$("#selector").jqxDropDownList({
			source: dataAdapter, theme: 'darkblue', displayMember: name, valueMember: id, width: 200, height: 25
		});
		// subscribe to the select event.
		$("#selector").on('select', function (event) {
			if (event.args) {
				var item = event.args.item;
				var loadByIdValue = item.value.toString();
				if (entityType === "yieldcurve") {
					loadByIdValue = item.label.toString() +"/"+item.value.toString();
				}
				;

				if (entityType === "instrumentPriceQuote") {
					loadByIdValue = item.label.toString() +"/"+item.value.toString();
				}
				;
				$.ajax({
					url: "/" + entityType + "/loadById/" + loadByIdValue + "/" + (new Date()).getTime()
					, success: function (response) {
						createEditor(response);
					}
				});
			}
		});
		$("#createButton").on('click', function (event) {
			that.createEditor();
		});
	};
	return that;
});