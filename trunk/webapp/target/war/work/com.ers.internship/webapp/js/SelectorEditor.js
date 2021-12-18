define(["jquery", "jqw/jqx-all"], function ($, jqw) {
	var that = this;
	that.createSelector = function (entityType) {
		$("#createButton").jqxButton({theme: 'darkblue', height: 27});
		$("#createButton").css('visibility', 'visible');
		var url = "/"+entityType+"/loadAll";
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
		// Create a jqxDropDownList
		$("#selector").jqxDropDownList({
			source: dataAdapter, theme: 'darkblue', displayMember: "name", valueMember: "id", width: 200, height: 25
		});
		// subscribe to the select event.
		$("#selector").on('select', function (event) {
			if (event.args) {
				var item = event.args.item;
				$.ajax({
					url: "/"+entityType+"/loadById/" + item.value.toString() + "/" + (new Date()).getTime()
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