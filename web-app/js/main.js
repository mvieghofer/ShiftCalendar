var shiftTypeEntryTemplate = "<li class=\"sub-nav-entry\" id=\"{0}\"><div>{0}</div><a href=\"#\">-</a></li>";
var calendarEntryTemplate = "<li class=\"sub-nav-entry\" id=\"{0}\"><div>{0}</div><a href=\"#\">-</a></li>";

var shiftTypes = [ {"name": "ShiftType 1", "from": {"hh": 7, "mm": 0}, "to": {"hh": 19, "mm": 0}}, 
                   {"name": "ShiftType 2", "from": {"hh": 7, "mm": 0}, "to": {"hh": 19, "mm": 0}}, 
                   {"name": "ShiftType 3", "from": {"hh": 7, "mm": 0}, "to": {"hh": 19, "mm": 0}} ];
                   
var calendars = [];

var mode = "calendar";

var token = null;

var apiKey = "AIzaSyBlqvZFDaLy3hXvtElkemMFlZK_NTtput0";
var clientId = "1078839201967-5q93saldmof06varcsmdmgmjmgq81m7m";
var scopes = ["https://www.googleapis.com/auth/calendar", "https://www.googleapis.com/auth/calendar.readonly", "https://www.googleapis.com/auth/userinfo.profile"];

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
	
    $("#main-cont").load("templates/calendar.html", function () {});
	
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


function onUserInfoFetched(e) {
  if (this.status != 200) return;
  var user_info = JSON.parse(this.response);
  populateUserInfo(user_info);
}

function populateUserInfo(user_info) {
    $("#login").hide();
    var elem = $("#user_info");
    if (!elem) return;
    elem.html("<b>Hello " + user_info.name + "</b>");
}

function getCalendarList() {
    gapi.client.load("calendar", "v3", function () {
        var request = gapi.client.calendar.calendarList.list();
        request.execute(function(resp) {
            console.log(resp.items);
            calendars = resp.items;
            $.each(calendars, function(index, value) {
               var calendar = calendars[index]; 
               $("#calendars").append(calendarEntryTemplate.replace(/\{0\}/g, calendar.summaryOverride == undefined ? calendar.summary : calendar.summaryOverride));
            });
        });
    });
}

function handleClientLoad() {
    // Step 2: Reference the API key
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
    // Step 3: get authorization to use private data
    gapi.auth.authorize({client_id: clientId, scope: scopes, immediate: false}, handleAuthResult);
    return false;
}
// Loadthe API and make an API call.  Display the results on the screen.
function login() {
    // Step 4: Load the Google+ API
    gapi.client.load('oauth2', 'v2', function() {
        // Step 5: Assemble the API request
        var request = gapi.client.oauth2.userinfo.get();
        // Step 6: Execute the API request
        request.execute(function(resp) {
            $("#login").hide();
            var elem = $("#user_info");
            if (!elem) return;
            elem.html("<b>Hello " + resp.name + "</b>");
            
            getCalendarList();
        });
    });
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
	    window.location = "https://accounts.google.com/o/oauth2/auth?" +
	                "scope=https%3A%2F%2Fwww.googleapis.com%2Fauth%2Fcalendar+https%3A%2F%2Fwww.googleapis.com%2Fauth%2Fcalendar.readonly+https%3A%2F%2Fwww.googleapis.com%2Fauth%2Fuserinfo.profile&" +
	                "state=%2Fprofile&" +
	                "response_type=token&" +
	                "redirect_uri=http%3A%2F%2Flocalhost:8001%2Foauth2callback.html&" +
                    "client_id=1078839201967-5q93saldmof06varcsmdmgmjmgq81m7m.apps.googleusercontent.com";
	});
	
	/*if ($.localStorage.isSet("access_token")) {
	    $.getJSON(
            'https://www.googleapis.com/oauth2/v2/userinfo?alt=json&access_token=' + $.localStorage.get("access_token"),
            function(data, textStatus, jqXHR) {
                $("#login").hide();
                $("#user_info").show();
                $("#user_info").html("Hello, " + data.name);
                getCalendarList();
            }
        );
	}*/
});