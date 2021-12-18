define(["jquery", "jqw/jqx-all"], function ($, jqw) {
	var createJqwMenu = function (menu) {
		menu.jqxMenu({width: '100%', height: '40px', autoOpen: false, theme: 'darkblue'});
		menu.css('visibility', 'visible');
		menu.jqxMenu({showTopLevelArrows: true});
	};

	$('a').click(function (event) {
		 event.preventDefault(); 
		var src = $(this).attr('href');
		if(src==="#Home"){
			$('.eventWindow').jqxWindow('closeAll');
			return false;
		}
		var title = $(this).attr('title');
		var jqxWidget = $('.eventWindow').last();
		if (jqxWidget.length == 0)
			jqxWidget = $('#jqxWidget');
		var offset = jqxWidget.offset();
		$('body').append($("<div class='eventWindow'  style='visibility: hidden;'><iframe src=" + src + " style='width:100%; height: 100%'></iframe></div>"));

		var eventWindow = $('.eventWindow').last()
		eventWindow.jqxWindow({
			theme: 'darkblue',
			title: title,
			height: 800,
			width: 600,
			position: {x: offset.left + 50, y: offset.top + 50},
			showCollapseButton: true,
					initContent: function () {
						eventWindow.css('visibility', 'visible');
					}
		});
		return false;
	});
	return createJqwMenu;
});

