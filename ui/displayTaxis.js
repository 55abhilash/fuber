
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
		for(var i = 0; i < taxiData.length; i++) {
			/*Get Latitude and Longitude of country*/
			var pos = taxiData[i]["countryInfo"];
			var lat = taxiData["latitude"];
			var lng = taxiData["longitude"];

			/*Draw a car at that location*/
			var car = L.circleMarker([lat, lng], {
					color: 'red',
					fillColor: '#f03',
					fillOpacity: 0.85,
					radius: rad
				     }).addTo(myMap).bindPopup("<b>" + "Fuber Driver Details" + "</b>" +  	
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
