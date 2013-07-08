function setUp() {
	var shiftTypes = [ {"name": "ShiftType 1"}, {"name": "ShiftType 2"}, {"name": "ShiftType 3"} ]
	$.each(shiftTypes, function(index, value) {
		$('#shift-types').append("<li class=\"sub-nav-entry\"><div>" + shiftTypes[index].name + "</div></li>");
	});
}

$(function() {
	setUp();
	
	$('.sub-nav-entry').click(function () {
		$(this).addClass("sub-nav-entry-active");
		$(this).siblings().removeClass("sub-nav-entry-active");
	});
});