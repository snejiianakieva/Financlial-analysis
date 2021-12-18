define(["jquery", "jqw/jqx-all", "jscomponents/SelectorEditor"], function ($, jqw, selector) {
	var that = this;
	selector.createEditor = function (response) {
		var url = "position/buildCashFlow/" + response.id + "";
		var posCashflow = [];
		var createTable = function () {
			var source =
					{
						localData: posCashflow,
						dataType: "array",
						dataFields:
								[
									{name: 'date', type: 'date'},
									{name: 'value', type: 'double'}
								],
						sortcolumn: 'date',
						sortdirection: 'asc'
					};
			var dataAdapter = new $.jqx.dataAdapter(source);
			$("#cashflowtable").jqxDataTable(
					{
						sortable: true,
						pageable: true,
						source: dataAdapter,
						columnsResize: true,
						theme: 'darkblue',
						columns: [
							{text: 'Date', dataField: 'date', type: 'date', cellsFormat: 'dd-MMM-yyyy'},
							{text: 'Value', dataField: 'value', type: 'float', cellsFormat: 'f2'}
						]

					});
		}

		$.ajax({
			url: url
			, async: false
			, success: function (cashflow) {
				posCashflow = [];
				var keys = Object.keys(cashflow);
				for (var i = 0; i < keys.length; i++) {
					var val = cashflow[keys[i]];
					posCashflow[i] = {};
					posCashflow[i]['date'] = new Date(keys[i]);
					posCashflow[i]['value'] = val;
					createTable();
				}
			}
		});
		var principalCF = [];
		var url = "position/buildPrincipalCashFlow/" + response.id + "";
		$.ajax({
			url: url
			, async: false
			, success: function (cashflow) {
				principalCF = [];
				var keys = Object.keys(cashflow);
				for (var i = 0; i < keys.length; i++) {
					var val = cashflow[keys[i]];
					principalCF.push({
						'date': new Date(keys[i]),
						'principal': val
					}
					);
				}
			}
		});
		var url = "position/buildInterestCashFlow/" + response.id + "";
		$.ajax({
			url: url
			, async: false
			, success: function (cashflow) {
				var keys = Object.keys(cashflow);
				for (var i = 0; i < keys.length; i++) {
					var val = cashflow[keys[i]];
					principalCF.push({
						'date': new Date(keys[i]),
						'interest': val
					}
					);
				}
			}
		});
		function compare(a, b) {
			return new Date(b.date) - new Date(a.date);
		}
		;

		principalCF.sort(compare);
		var source =
				{
					datatype: "array",
					datafields: [
						{name: 'date'},
						{name: 'principal'},
						{name: 'interest'}
					],
					localdata: principalCF
				};
		var dataAdapter = new $.jqx.dataAdapter(source);
		// prepare jqxChart settings
		var settings = {
			title: response.name + " Cashflow",
			description: "Principal and interest cashflow",
			showLegend: true,
			padding: {left: 10, top: 5, right: 10, bottom: 5},
			titlePadding: {left: 50, top: 0, right: 0, bottom: 10},
			source: dataAdapter,
			xAxis:
					{
						dataField: 'date',
						type: 'date',
					},
			colorScheme: 'scheme04',
			seriesGroups:
					[
						{
							type: 'column',
							series: [
								{dataField: 'principal', displayText: 'Principal', emptyPointsDisplay: 'connect'},
								{dataField: 'interest', displayText: 'Interest', emptyPointsDisplay: 'connect'}
							]
						}
					]
		};
		$('#cashflowchart').jqxChart(settings);
		$("#editor").css("visibility", "visible");
	};
	return that;
});
