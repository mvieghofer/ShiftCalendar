var currentShiftType = null;
var dates = [];
var calendar = null;

var addCalendarURLTemplate = "https://www.googleapis.com/calendar/v3/calendars/{calendarId}/events?access_token={access_token}";

function leadingZeroNumber(number) {
    return number < 10 ? "0" + number : number;
}

function getDateString(date, format) {
    var dateString = "";
    if (format === "dd.mm.yyyy") {
        dateString = leadingZeroNumber(date.getDate()) + "." + leadingZeroNumber(date.getMonth() + 1) + "." + date.getFullYear();
    } else if (format === "yyyy-mm-dd") {
        dateString = date.getFullYear() + "-" + leadingZeroNumber(date.getMonth() + 1) + "-" + leadingZeroNumber(date.getDate());
    }
    return dateString;
}

function changeShiftType(event, shiftType) {
    currentShiftType = shiftType;
    $("#selected-shift-type-cont").html("<div>" + shiftType.name + "</div>");
}

function addDatesToView() {
    dates.sort(compareDates);
    $("#selected-dates-cont").html("");
    $.each(dates, function(index) {
        $("#selected-dates-cont").append("<li>" + getDateString(dates[index], "dd.mm.yyyy") + "</li>");
    });
}

function addDate(date) {
    var tmp = new Date(date.getTime());
    dates[dates.length] = tmp;
    addDatesToView();
}

function removeDate(date, dateIndex) {
    dates.splice(dateIndex, 1);    
    addDatesToView();
}

function compareDates(date1, date2) {
    return (date1.getTime() - date2.getTime());
}

function dateSelected(date) {
    var dateIndex = -1;
    $.each(dates, function(index, value) {
        if (compareDates(dates[index], date) === 0) {
            dateIndex = index;
        }
    });
    return dateIndex;
}

function getCalendarSummary(calendar) {
    return calendar.summaryOverride == undefined ? calendar.summary : calendar.summaryOverride;
}

function changeCalendar(event, data) {
    calendar = data;
    $("#selected-calendar-cont").html(getCalendarSummary(calendar));
    getCalendarEvents();
}

function createEvent(data) {
    var calEvent = {
           "id": data.id,
           "title": data.summary,
           "start": new Date(data.start.dateTime),
           "end": new Date(data.end.dateTime),
           "editable": true
       };
   return calEvent;
}

function getCalendarEvents() {
    $("#calendar").fullCalendar("removeEvents");
    var calendarView = $("#calendar").fullCalendar("getView");
    var request = gapi.client.calendar.events.list({"calendarId": calendar.id, "timeMax": calendarView.visEnd.toISOString(), "timeMin": calendarView.visStart.toISOString()});
    request.execute(function(response) {
        var events = response.items;
        var calEvents = [];
        $.each(events, function(index) {
           var tmpEvent = events[index];
           var calEvent = createEvent(tmpEvent);
           calEvents.push(calEvent);
        });
       $("#calendar").fullCalendar("addEventSource", calEvents);
    });
}

function getFromDate(date) {
    var fromDate = new Date(date.getTime());
    fromDate.setHours(currentShiftType.from.hh);
    fromDate.setMinutes(currentShiftType.from.mm);
    return fromDate;
}

function getToDate(date) {
    var toDate = new Date(date.getTime());
    toDate.setHours(currentShiftType.to.hh);
    toDate.setMinutes(currentShiftType.to.mm);
    return toDate;
}

function eventAddedSuccessfully(response) {
    console.log(response);
    $("#calendar").fullCalendar("addEventSource", [ createEvent(response) ]);
    
    $("#selected-dates-cont li:contains("+getDateString(new Date(response.start.dateTime), "dd.mm.yyyy")+")").addClass("successfullyAdded");
    
    if ($("#selected-dates-cont li").length === $("#selected-dates-cont li.successfullyAdded").length) {
        $("#selected-dates-cont").empty();
        $("#overview-dates-success").show();
        $(".selectedDay").removeClass("selectedDay");
        dates = [];
    }
}

function handleCalendarResponse(response) {
    if (response.code == 404) {
        $("#error").html("Could not save all events.");
        $("#error").dialog();
    } else if (response.code == 401) {
        login(submitDates);
    } else if (response.status == "confirmed") {
        eventAddedSuccessfully(response);
    } else {
        $("#error").html("Could not save events.");
        $("#error").dialog();
    }
}

function submitDates() {
    if (validate()) {
        var url = addCalendarURLTemplate.replace(/\{calendarId\}/g, calendar.id);
        url = url.replace(/\{access_token\}/g, $.localStorage.get("access_token"));
        $.each(dates, function(index) {
            var date = dates[index];
            var fromDate = getFromDate(date);
            var toDate = getToDate(date);
            
            if (toDate < fromDate) {
                toDate.setDate(toDate.getDate() + 1);
            }
            
            var data = { 
                         "summary": currentShiftType.name,
                         "end":   { "dateTime": toDate.toISOString() },
                         "start": { "dateTime": fromDate.toISOString() }
                       };
            
            gapi.client.load("calendar", "v3", function () {
                var request = gapi.client.calendar.events.insert({"calendarId": calendar.id, "resource": data});
                request.execute(function(response) {
                    handleCalendarResponse(response);
                });
            });    
        });
    }
}

function validate() {
    var errorText = "";
    if (calendar == null) {
        errorText += "Please select a calendar<br />";
    }
    if (dates.length == 0) {
        errorText += "No Dates selected<br />";
    }
    if (currentShiftType == null) {
        errorText += "No Shifttype selected<br />";
    }
    if (errorText == "") {
        return true;
    } else {
        $("#error").html(errorText);
        $("#error").dialog();
        return false;
    }
}

function changeDateCssClass(clickedDate) {
    if (clickedDate.hasClass("selectedDay")) {
        clickedDate.removeClass("selectedDay");
    } else {
        clickedDate = clickedDate.addClass("selectedDay");
    }
}

function selectDate(start, end, allDay, jsEvent, view) {
    var currDate = start;
    while (currDate <= end) {
        var selectedDate = $("td.fc-day[data-date=\"" + getDateString(currDate, "yyyy-mm-dd") + "\"]");
        console.log(selectedDate);
        changeDateCssClass(selectedDate);
        var dateIndex = dateSelected(currDate);
        if (dateIndex === -1){
            addDate(currDate);
        } else { 
            removeDate(currDate, dateIndex);
        }
        currDate.setDate(currDate.getDate() + 1);
    }
}

function calendarTimeRangeChanged(view) {
    $.each(dates, function(index) {
        var date = dates[index];
        if ($("td.fc-day[data-date=\"" + getDateString(date, "yyyy-mm-dd") + "\"]").length === 1) {
            $("td.fc-day[data-date=\"" + getDateString(date, "yyyy-mm-dd") + "\"]").addClass("selectedDay");
        }
    });
}

function eventClicked(calEvent, jsEvent, view) {
    console.log($(this));
}

$(document).ready(function() {   
    
    $("body").on("changeShiftType", changeShiftType);
    $("body").on("changeCalendar", changeCalendar);
    
   $("#calendar").fullCalendar({
       firstDay: 1,
       selectable: true,
       selectHelper: true,
       select: function(start, end, allDay, jsEvent, view) {
            selectDate(start, end, allDay, jsEvent, view);
       },
       viewDisplay: function(view) {
           calendarTimeRangeChanged(view);
       },
       eventClick: function(calEvent, jsEvent, view) {
           eventClicked(calEvent, jsEvent, view);
       }
   });
   
   $("#submit-overview").click(function() {
       submitDates();
   });
   
   $(window).trigger("resize");
});