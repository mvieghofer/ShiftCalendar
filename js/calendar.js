var currentShiftType = null;
var dates = [];
var calendar = null;

var addCalendarURLTemplate = "https://www.googleapis.com/calendar/v3/calendars/{calendarId}/events?access_token={access_token}";

function leadingZeroNumber(number) {
    return number < 10 ? "0" + number : number;
}

function getDateString(date, format) {
    var dateString = "";
    if (format === "dd.mm.yyyy")
        dateString = leadingZeroNumber(date.getDate()) + "." + leadingZeroNumber(date.getMonth() + 1) + "." + date.getFullYear();
    else if (format === "yyyy-mm-dd")
        dateString = date.getFullYear() + "-" + leadingZeroNumber(date.getMonth() + 1) + "-" + leadingZeroNumber(date.getDate());
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
}

function submitDates() {
    if (validate()) {
        var url = addCalendarURLTemplate.replace(/\{calendarId\}/g, calendar.id);
        url = url.replace(/\{access_token\}/g, $.localStorage.get("access_token"));
        console.log(url);
        $.each(dates, function(index) {
            var date = dates[index];
            var fromDate = new Date(date.getTime());
            fromDate.setHours(currentShiftType.from.hh);
            fromDate.setMinutes(currentShiftType.from.mm);
            var toDate = new Date(date.getTime());
            toDate.setHours(currentShiftType.to.hh);
            toDate.setMinutes(currentShiftType.to.mm);
            if (toDate < fromDate) {
                toDate.setDate(toDate.getDate() + 1);
            }
            
            var data = { 
                         "summary": currentShiftType.name,
                         "end":   { "dateTime": toDate.toISOString() },
                         "start": { "dateTime": fromDate.toISOString() }
                       };
           console.log(data);
             gapi.client.load("calendar", "v3", function () {
                var request = gapi.client.calendar.events.insert({"calendarId": calendar.id, "resource": data});
                request.execute(function(response) {
                    console.log(response);
                })
             });    
        });
    }
}

function validate() {
    var errorText = "";
    if (calendar == null) {
        errorText += "Please select a calendar\n";
    }
    if (dates.length == 0) {
        errorText += "No Dates selected\n";
    }
    if (currentShiftType == null) {
        errorText += "No Shifttype selected\n";
    }
    if (errorText == "") {
        return true;
    } else {
        alert(errorText);
        return false;
    }
}

$(document).ready(function() {   
    
    $("body").on("changeShiftType", changeShiftType);
    $("body").on("changeCalendar", changeCalendar);
    
    var selectionManager = (function(){

        //define a "select" method for switching 'selected' state
        return {
            select: function(elementToSelect) {
                if (elementToSelect.hasClass("selectedDay")) {
                    elementToSelect.removeClass("selectedDay");
                } else {
                    curSelectedDay = elementToSelect.addClass("selectedDay");
                }
            }       
        };
    })();
    
   $("#calendar").fullCalendar({
       firstDay: 1,
       selectable: true,
       selectHelper: true,
       select: function(start, end, allDay, jsEvent, view) {
            var currDate = start;
            while (currDate <= end) {
                var clickedEvent = $("td.fc-day[data-date=\"" + getDateString(currDate, "yyyy-mm-dd") + "\"]");
                selectionManager.select(clickedEvent);
                var dateIndex = dateSelected(currDate);
                if (dateIndex === -1){
                    addDate(currDate);
                } else { 
                    removeDate(currDate, dateIndex);
                }
                currDate.setDate(currDate.getDate() + 1);
            }
       }
   });
   
   $("#submit-overview").click(function() {
       submitDates();
   });
   
   $(window).trigger("resize");
});