
$.ajax({
	url: "http://localhost:3000/getAllTaxis",
	beforeSend: function() {
		$("#mapid").css('opacity', '0.4');
		$("#loading").show();
	},
	complete: function() {
		$("#loading").hide();	
		$("#mapid").css('opacity', '1');
	},
	success: function (resp) {
		var taxiData = resp;
		var taxiIcon = L.icon({
    			iconUrl: './img/taxi.png',

    			iconSize:     [38, 95], // size of the icon
    			iconAnchor:   [22, 94], // point of the icon which will correspond to marker's location
    			popupAnchor:  [-3, -76] // point from which the popup should open relative to the iconAnchor
		});
		for(var i = 0; i < taxiData.length; i++) {
			/*Get Latitude and Longitude of country*/
			var lat = taxiData[i]["latitude"];
			var lng = taxiData[i]["longitude"];

			/*Draw a car at that location*/
			var car = L.marker([lat, lng], 
				{ icon: taxiIcon,}).addTo(myMap).bindPopup("<b>" + "Fuber Driver Details" + "</b>" +  	
							  "<hr>" + 
							  "Driver Name: " + taxiData[i]["driver_name"] +  
							  "<br> Driver ID: " + taxiData[i]["driver_id"] +
							  "<br> Contact No.: " + taxiData[i]["driver_no"] + 
							  "<br> Taxi No.: " + taxiData[i]["taxi_id"]);
			
		}
	},
	error: function() {
	}
});
