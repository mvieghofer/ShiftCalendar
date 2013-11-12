var shiftTypeEntryTemplate = "<li class=\"sub-nav-entry\" id=\"{0}\"><div>{0}</div><a href=\"#\">-</a></li>";
var calendarEntryTemplate = "<li class=\"sub-nav-entry\" id=\"{0}\"><div>{0}</div></li>";

var shiftTypes = []// {"name": "ShiftType 1", "from": {"hh": 7, "mm": 0}, "to": {"hh": 19, "mm": 0}}, 
                   //{"name": "ShiftType 2", "from": {"hh": 7, "mm": 0}, "to": {"hh": 19, "mm": 0}}, 
                   //{"name": "ShiftType 3", "from": {"hh": 7, "mm": 0}, "to": {"hh": 19, "mm": 0}} ];
                   
var calendars = [];

var mode = "calendar";

var editDialog = null;

var token = null;

var apiKey = "AIzaSyDkhEAe8uYrRm1H2jMfCsI0yvHdBDX1GRc";
var clientId = "1078839201967-5q93saldmof06varcsmdmgmjmgq81m7m";
var scopes = ["https://www.googleapis.com/auth/calendar", "https://www.googleapis.com/auth/calendar.readonly", "https://www.googleapis.com/auth/userinfo.profile"];

var removedShiftTypes = [];

var SHIFT_TYPE_KEY = "shift-types";

function saveShiftTypes() {
    $.localStorage.set(SHIFT_TYPE_KEY, { "shiftTypes": shiftTypes });
}

function addShiftType(eventString, shiftType) {
	$("#shift-types").append(shiftTypeEntryTemplate.replace(/\{0\}/g, shiftType.name));
	shiftTypes.push(shiftType);
	saveShiftTypes();
	loadCalendarView();
}

function editShiftTypeComplete(eventString, oldShiftName, newShiftType) {
    $.each(shiftTypes, function(index, value) {
       if (shiftTypes[index].name == oldShiftName) {
           shiftTypes[index] = newShiftType;
       } 
    });
    if(oldShiftName !== newShiftType.name) {
        $("#shift-types > .sub-nav-entry > div:contains('" + oldShiftName + "')").html(newShiftType.name);
    }
    
	saveShiftTypes();
}

function saveRemovedShiftTypes() {
    $.localStorage.set("removed-shift-types", removedShiftTypes);
}

function removeShiftTypeCommit() {
    removedShiftTypes.shift();
    saveRemovedShiftTypes();
    if (removedShiftTypes.length > 0) {
        $("#undo-cont").html("You removed " + removedShiftTypes.length + " shift types. <a href=\"#\" id=\"undo\">Undo</a><a href=\"#\" id=\"undo-close\">x</a>");
    } else {
        $("#undo-cont").hide();
    }
}

function removeAllShiftTypeCommit() {
    removedShiftTypes = [];
    saveRemovedShiftTypes();
    $("#undo-cont").hide();
}

function removeShiftTypeUndo() {
    $.each(removedShiftTypes, function(index) {
        var shiftType = removedShiftTypes.shift();
        addShiftType("", shiftType);
    });
    $("#undo-cont").hide();
}

function removeShiftType(shiftType) {
    if (shiftType !== undefined) {
        removedShiftTypes.push(shiftType);
        saveRemovedShiftTypes();
        setInterval(removeShiftTypeCommit, 30 * 1000);
        $("#undo-cont").html("You removed " + removedShiftTypes.length + " shift types. <a href=\"#\" id=\"undo\">Undo</a><a href=\"#\" id=\"undo-close\">x</a></div>");
        $("#undo-cont").show();
        var indexToDelete = -1;
        $.each(shiftTypes, function (index, value) {
            if (shiftTypes[index].name === shiftType.name) {
                indexToDelete = index;
                $("#shift-types li").remove(":contains('" + shiftTypes[index].name + "')");
            }
        });
        if (~indexToDelete) {
            shiftTypes.splice(indexToDelete, 1);
        }
        saveShiftTypes();
        $("#edit").dialog("close");   
    } 
    return false;
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
    if ($.localStorage.isSet(SHIFT_TYPE_KEY)) {
        shiftTypes = $.localStorage.get(SHIFT_TYPE_KEY).shiftTypes;
    }
	$.each(shiftTypes, function(index, value) {
		$("#shift-types").append(shiftTypeEntryTemplate.replace(/\{0\}/g, shiftTypes[index].name));
	});
	
    loadCalendarView();
	
	$(document).on("click", "#undo", removeShiftTypeUndo);
	$(document).on("click", "#undo-close", removeAllShiftTypeCommit);
	$("body").on("addShiftType", addShiftType);
	$("body").on("editShiftTypeComplete", editShiftTypeComplete);
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

function getCalendar(name) {
    var calendar;
    $.each(calendars, function(index) {
       if (calendars[index].summary == name || calendars[index].summaryOverride == name)
       calendar = calendars[index]; 
    });
    return calendar;
}

function getCalendarList() {
    gapi.client.load("calendar", "v3", function () {
        var request = gapi.client.calendar.calendarList.list();
        request.execute(function(resp) {
            calendars = resp.items;
            $.each(calendars, function(index, value) {
               var calendar = calendars[index]; 
               $("#calendars").append(calendarEntryTemplate.replace(/\{0\}/g, calendar.summaryOverride == undefined ? calendar.summary : calendar.summaryOverride));
            });
        });
    });
}

function handleClientLoad() {
    gapi.client.setApiKey(apiKey);
    window.setTimeout(checkAuth,1);
}

function checkAuth() {
    gapi.auth.authorize({client_id: clientId, scope: scopes, immediate: true}, handleAuthResult);
}

function handleAuthResult(authResult) {
    var authorizeButton = document.getElementById('login');
    if (authResult && !authResult.error) {
        authorizeButton.style.visibility = 'hidden';
        login();
    } else {
        authorizeButton.style.visibility = '';
        authorizeButton.onclick = handleAuthClick;
    }
}

function handleAuthClick(event) {
    gapi.auth.authorize({client_id: clientId, scope: scopes, immediate: false}, handleAuthResult);
    return false;
}

function login(callback) {
    gapi.client.load('oauth2', 'v2', function() {
        var request = gapi.client.oauth2.userinfo.get();
        request.execute(function(resp) {
            $("#login").hide();
            var elem = $("#user_info");
            if (!elem) return;
            elem.html("<b>Hello, " + resp.name + "</b>");
            
            getCalendarList();
            if (callback != null) {
                callback();
            }
        });
    });
}

function loadCalendarView() {
    $("#main-cont").load("templates/calendar.html");
}

function initEditShiftType(shiftType) {
    console.log("init " + shiftType);
    type = "editShiftTypeComplete";
    oldShiftName = shiftType.name;
    $("#shift-type-name").val(shiftType.name);
    $("#from-time").val((shiftType.from.hh < 10 ? "0" + shiftType.from.hh : shiftType.from.hh) + ":" + (shiftType.from.mm < 10 ? "0" + shiftType.from.mm : shiftType.from.mm));
    $("#from-time").trigger("change");
    $("#to-time").val((shiftType.to.hh < 10 ? "0" + shiftType.to.hh : shiftType.to.hh) + ":" + (shiftType.to.mm < 10 ? "0" + shiftType.to.mm : shiftType.to.mm));
    $("#to-time").trigger("change");
}

function clearShiftTypePopup() {
    $("#shift-type-name").val("");
    $("#from-time").val("");
    $("#to-time").val("");
    $("#duration").html("");
}

function loadEditShiftTypeView(option, shiftType) {
    mode = "edit";
    var title = "Edit Shift Type";
    if (option === "add") {
        title = "Add Shift Type";
        clearShiftTypePopup();
    } else if (option === "edit") {
        $("#btn-remove-shift-type").show(); 
        $("#btn-remove-shift-type").click(function() {
            removeShiftType(getShiftType($("#shift-type-name").val()));
        })
    }
    editDialog = $("#edit").dialog({
        width: "750px", 
        title: title,
        close: function() {
            mode = "calendar";
        }
    });
    if (option === "edit") {
	    initEditShiftType(getShiftType($(shiftType).children("div").html()));
    }
}

$(document).ready(function() {
	setUp();
	
	$(document).on("click", ".sub-nav-entry", function () {
	    if (!$(this).hasClass("sub-nav-entry-active")) {
    		$(this).addClass("sub-nav-entry-active");
    		$(this).siblings().removeClass("sub-nav-entry-active");
		} else {
		    if (editDialog !== null && editDialog.dialog("isOpen")) {
		        $("#edit").dialog("close");
		    }
    		$(this).removeClass("sub-nav-entry-active");
		}
		
		if ($(this).parent().attr("id") == "shift-types" && mode === "edit") {
		    initEditShiftType(getShiftType($("#shift-types > .sub-nav-entry-active > div").html()));
        } else if ($(this).parent().attr("id") == "shift-types" && mode === "calendar") {
            $("body").trigger("changeShiftType", getShiftType($("#shift-types > .sub-nav-entry-active > div").html()));
        } else if ($(this).parent().attr("id") == "calendars" && mode === "calendar") {    
            $("body").trigger("changeCalendar", getCalendar($("#calendars > .sub-nav-entry-active > div").html()));
        }
	});
	
	$(document).on("dblclick", ".sub-nav-entry", function() { 
	    $(this).addClass("sub-nav-entry-active");
	    loadEditShiftTypeView("edit", $(this));
	});
	
	$("#btn-add-shift-type").click(function() {
	    type = "addShiftType";
	    $(".sub-nav-entry-active").removeClass("sub-nav-entry-active"); 
	    loadEditShiftTypeView("add");
	});
});