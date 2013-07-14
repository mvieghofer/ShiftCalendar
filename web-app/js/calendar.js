var currentShiftType = null;
var dates = [];

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

function addDate(date) {
    var tmp = new Date(date.getTime())
    console.log(tmp);
    dates[dates.length] = tmp;
    $("#selected-dates-cont").append("<li>" + getDateString(tmp, "dd.mm.yyyy") + "</li>");
}

function removeDate(date, dateIndex) {
    dates.splice(dateIndex, 1);
    $("#selected-dates-cont li").remove(":contains(" + getDateString(date, "dd.mm.yyyy") + ")");
}

function compareDates(date1, date2) {
    return (date1.getDate() === date2.getDate()) &&
           (date1.getMonth() === date2.getMonth()) &&
           (date1.getFullYear() === date2.getFullYear());
}

function dateSelected(date) {
    var dateIndex = -1;
    $.each(dates, function(index, value) {
        console.log(dates[index] + " " + date);
        if (compareDates(dates[index], date)) {
            dateIndex = index;
        }
    });
    return dateIndex;
}

$(document).ready(function() {   
    
    $("body").on("changeShiftType", changeShiftType);
    
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
   
   $(window).trigger("resize");
});