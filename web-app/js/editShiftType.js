var type = "addShiftType";
var oldShiftName = "";

function parseTime(s) {
    var part = s.match(/(\d+):(\d+)(?: )?(am|pm)?/i),
        hh = parseInt(part[1], 10),
        mm = parseInt(part[2], 10),
        ap = part[3] ? part[3].toUpperCase() : null;
        
    if (ap == "AM") {
        if (hh == 12) {
            hh = 0;
        }
    }    
    if (ap == "PM") {
        if (hh != 12) {
            hh += 12;
        }
    }
    return { "hh": hh, "mm": mm };
}

function calcDiff(time1, time2) {
    var date1 = new Date(2014, 1, 1, time1.hh, time1.mm),
        date2 = new Date(2014, 1, 1, time2.hh, time2.mm);
    if (date2 < date1) {
        date2.setDate(date2.getDate() + 1);
    }
    var diff = date2 - date1,
        msec = diff,
        hh = Math.floor(msec / 1000 / 60 / 60);
    msec -= hh * 1000 * 60 * 60;
    var mm = Math.floor(msec / 1000 / 60);
    msec -= mm * 1000 * 60;
    var ss = Math.floor(msec / 1000);
    msec -= ss * 1000;
    return { "hh": hh, "mm": mm };
}

function submitShiftType(eventString, fromTime, toTime) {
    console.log(eventString);
    var shiftType = {"name": $("#shift-type-name").val(), 
                     "from": fromTime,
                     "to": toTime};
    if (eventString == "editShiftTypeComplete")
        $("body").trigger(eventString, [oldShiftName, shiftType]);
    else {
        $("body").trigger(eventString, shiftType);
    }    
    $("#edit").dialog("close");
}

function checkNameUniqunessResult(event, uniqueness) {
    if (!uniqueness) {
        $("#error").append("<li>Name not unique</li>");
    } else {
        $("#error").html($("#error").html().replace("<li>Name not unique</li>", ""));
    }
}

$(document).ready(function() {
    var fromTime = {"hh": 0, "mm": 0},
        toTime = {"hh": 0, "mm": 0},
        duration = {"hh": 0, "mm": 0};
        
    $("body").on("checkNameUniqunessResult", checkNameUniqunessResult);
    
    $("#from-time").change(function() {
        fromTime = parseTime($(this).val());
        $("#to-time").timepicker("option", "minTime", $(this).val());
        if ($("#to-time").val().length > 0) {
            var date1 = new Date(2014, 1, 1, fromTime.hh, fromTime.mm),
                date2 = new Date(2014, 1, 1, duration.hh, duration.mm);
            date1.setHours(date1.getHours() + date2.getHours());
            date1.setMinutes(date1.getMinutes() + date2.getMinutes());
            var hours = date1.getHours(),
                minutes = date1.getMinutes();
            var time = (hours < 10 ? "0" + hours : hours) + ":" + (minutes < 10 ? "0" + minutes : minutes);
            $("#to-time").val(time);
        }
    });
    
    $("#to-time").change(function() {
        toTime = parseTime($(this).val());
        duration = calcDiff(fromTime, toTime);
        $("#duration").html((duration.hh < 10 ? "0" + duration.hh : duration.hh) + ":" + (duration.mm < 10 ? "0" + duration.mm : duration.mm));
    });    
    
    $("#shift-type-name").change(function() {
       $("body").trigger("checkNameUniquness", $(this).val()); 
    });
    
    $("#btn-add-shift-type-submit").click(function() {
            var errorText = "";
            if($("#shift-type-name").val() === "") {
                errorText = "<li>no name</li>";
            } 
            if ($("#from-time").val() === "") {
                errorText += "<li>no start time</li>";
            } 
            if ($("#to-time").val() === "") {
                errorText += "<li>no end time</li>";
            }
            if (errorText === "")
                submitShiftType(type, fromTime, toTime);
            else
                $("#error").html("<div class=\"row\"><div class=\"cell\">" + errorText + "</div></div>");
        });
    
    $("#from-time").timepicker({
        "timeFormat": "H:i",
        "scrollDefaultNow": true
    });
    
    $("#to-time").timepicker({
        "timeFormat": "H:i",
        "scrollDefaultNow": true,
        "minTime": "00:00",
    	"showDuration": true
    });

    
});