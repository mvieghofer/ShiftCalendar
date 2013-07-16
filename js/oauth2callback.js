$(document).ready(function() {
    var location = window.location.toString();
    var paramsString = location.split("#")[1]; 
    var paramsArr = paramsString.split("&");
    var params = []
    $.each(paramsArr, function(index, value) {
       var keyValue = paramsArr[index].split("=");
        params.push({"key": keyValue[0], "value": keyValue[1]});
        
        $.localStorage.set(keyValue[0], keyValue[1]);
    });
    console.log(params);
    window.location = "http://localhost:8001/";
});