var shiftTypeEntryTemplate = "<li class=\"sub-nav-entry\" id=\"{0}\"><div>{0}</div><a href=\"#\">-</a></li>";
var calendarEntryTemplate = "<li class=\"sub-nav-entry\" id=\"{0}\"><div>{0}</div></li>";

var shiftTypes = []// {"name": "ShiftType 1", "from": {"hh": 7, "mm": 0}, "to": {"hh": 19, "mm": 0}}, 
                   //{"name": "ShiftType 2", "from": {"hh": 7, "mm": 0}, "to": {"hh": 19, "mm": 0}}, 
                   //{"name": "ShiftType 3", "from": {"hh": 7, "mm": 0}, "to": {"hh": 19, "mm": 0}} ];
                   
var calendars = [];

var mode = "calendar";

var token = null;

var apiKey = "AIzaSyDkhEAe8uYrRm1H2jMfCsI0yvHdBDX1GRc";
var clientId = "1078839201967-5q93saldmof06varcsmdmgmjmgq81m7m";
var scopes = ["https://www.googleapis.com/auth/calendar", "https://www.googleapis.com/auth/calendar.readonly", "https://www.googleapis.com/auth/userinfo.profile"];


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

function editShiftType(eventString, oldShiftName, newShiftType) {
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

function removeShiftType(shiftType) {
    if (confirm("Are you sure you want to delete this category?")) {
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

function editShiftType(shiftType) {
    type = "editShiftTypeComplete";
    oldShiftName = shiftType.name;
    $("#shift-type-name").val(shiftType.name);
    $("#from-time").val((shiftType.from.hh < 10 ? "0" + shiftType.from.hh : shiftType.from.hh) + ":" + (shiftType.from.hh < 10 ? "0" + shiftType.from.mm : shiftType.from.mm));
    $("#from-time").trigger("change");
    $("#to-time").val((shiftType.to.hh < 10 ? "0" + shiftType.to.hh : shiftType.to.hh) + ":" + (shiftType.to.mm < 10 ? "0" + shiftType.to.mm : shiftType.to.mm));
    $("#to-time").trigger("change");
}

function loadEditShiftTypeView(option, shiftType) {
    var title = "Edit Shift Type";
    if (option === "add") {
        title = "Add Shift Type";
    } else if (option === "edit") {
        $("#btn-remove-shift-type").show(); 
        $("#btn-remove-shift-type").click(function() {
            removeShiftType(getShiftType($(shiftType).children("div").html()));
        })
    }
    $("#edit").dialog({width: "750px", title: title});
    if (option === "edit") {
	    editShiftType(getShiftType($(shiftType).children("div").html()));
    }
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
		
		if ($(this).parent().attr("id") == "shift-types" && mode === "edit") {
            $("body").trigger("editShiftType", getShiftType($("#shift-types > .sub-nav-entry-active > div").html()));
        } else if ($(this).parent().attr("id") == "shift-types" && mode === "calendar") {
            $("body").trigger("changeShiftType", getShiftType($("#shift-types > .sub-nav-entry-active > div").html()));
        } else if ($(this).parent().attr("id") == "calendars" && mode === "calendar") {    
            $("body").trigger("changeCalendar", getCalendar($("#calendars > .sub-nav-entry-active > div").html()));
        }
	});
	
	$(document).on("dblclick", ".sub-nav-entry", function() { 
	    loadEditShiftTypeView("edit", $(this));
	});
	
	$("#btn-add-shift-type").click(function() {
	    loadEditShiftTypeView("add");
	});
});