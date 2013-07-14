var shiftTypeEntryTemplate = "<li class=\"sub-nav-entry\" id=\"{0}\"><div>{0}</div><a href=\"#\">-</a></li>";
var shiftTypes = [ {"name": "ShiftType 1", "from": {"hh": 7, "mm": 0}, "to": {"hh": 19, "mm": 0}}, 
                   {"name": "ShiftType 2", "from": {"hh": 7, "mm": 0}, "to": {"hh": 19, "mm": 0}}, 
                   {"name": "ShiftType 3", "from": {"hh": 7, "mm": 0}, "to": {"hh": 19, "mm": 0}} ]
var mode = "calendar";

function addShiftType(eventString, shiftType) {
	$("#shift-types").append(shiftTypeEntryTemplate.replace(/\{0\}/g, shiftType.name));
	shiftTypes.push(shiftType);
}

function editShiftType(eventString, oldShiftName, newShiftType) {
    $.each(shiftTypes, function(index, value) {
       if (shiftTypes[index].name == oldShiftName) {
           shiftTypes[index] = newShiftType;
       } 
    });
    if(oldShiftName !== newShiftType.name) {
        $("#shift-types > .sub-nav-entry > div:contains('" + oldShiftName + "')").html(newShiftType.name);
    }
}

function checkNameUniqueness(eventString, name) {
    var result = true;
    $.each(shiftTypes, function(index, value) {
        if (shiftTypes[index].name === name)
            result = false;
    });
    $("body").trigger("checkNameUniqunessResult", result);
}

function setUp() {
	$.each(shiftTypes, function(index, value) {
		$("#shift-types").append(shiftTypeEntryTemplate.replace(/\{0\}/g, shiftTypes[index].name));
	});
	
    $("#main-cont").load("templates/calendar.html");
	
	$("body").on("addShiftType", addShiftType);
	$("body").on("editShiftTypeComplete", editShiftType);
	$("body").on("checkNameUniquness", checkNameUniqueness);
}

function getShiftType(name) {
    var shiftType;
    $.each(shiftTypes, function(index, value) {
        if (shiftTypes[index].name == name) {
            shiftType = shiftTypes[index];
        }
    });
    return shiftType;
}

$(document).ready(function() {
	setUp();
	
	$(document).on("click", ".sub-nav-entry", function () {
	    if (!$(this).hasClass("sub-nav-entry-active")) {
    		$(this).addClass("sub-nav-entry-active");
    		$(this).siblings().removeClass("sub-nav-entry-active");
		} else {
    		$(this).removeClass("sub-nav-entry-active");
		}
		if (mode === "edit") {
            $("body").trigger("editShiftType", getShiftType($("#shift-types > .sub-nav-entry-active > div").html()));
        } else if (mode === "calendar") {
            $("body").trigger("changeShiftType", getShiftType($("#shift-types > .sub-nav-entry-active > div").html()));
        }
	});
	
	$("#btn-add-shift-type").click(function() {
		$("#main-cont").load("templates/editShiftType.html");
	});
	
	$("#btn-edit-shift-type").click(function() {
	    if (mode !== "edit") {
	        mode = "edit";
    	    $(".sub-nav-entry a").show();
    	    $(".sub-nav-entry a").click(function () {
    	        var currentAnchor = $(this);
    	        if (confirm("Are you sure you want to delete this category?")) {
    	            var indexToDelete = -1;
    	            $.each(shiftTypes, function (index, value) {
    	                if (shiftTypes[index].name === $(currentAnchor.siblings()[0]).html()) {
    	                    indexToDelete = index;
                            $("#shift-types li").remove(":contains('" + shiftTypes[index].name + "')");
	                    }
    	            });
    	            if (~indexToDelete) {
    	                shiftTypes.splice(indexToDelete, 1);
	                }
    	        }    
                return false;
    	    });
            $("#main-cont").load("templates/editShiftType.html", function() {
                if ($("#shift-types > .sub-nav-entry-active").length === 1) {
            	    $("body").trigger("editShiftType", getShiftType($("#shift-types > .sub-nav-entry-active > div").html()));                
                }
            });
        } else {
            mode = "calendar";
    	    $(".sub-nav-entry a").hide();
            $("#main-cont").load("templates/calendar.html");
        }
	});
	
	$("#login").click(function() {
	    chrome.identity.getAuthToken({ 'interactive': true }, function(token) {
        }); 
	});
});