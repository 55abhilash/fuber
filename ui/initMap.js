
var myMap = L.map('mapid').setView([24, 16], 3);

L.tileLayer('https://api.mapbox.com/styles/v1/{id}/tiles/{z}/{x}/{y}?access_token=pk.eyJ1IjoiNTVhYmhpbGFzaCIsImEiOiJjazg0a2t2bW8xMnE0M25vd3FwNXQzZ3BwIn0.3dbqJ8xSkjN9lrstWlPM5g', 
{
	attribution: 'Map data &copy; <a href="https://www.openstreetmap.org/">OpenStreetMap</a> contributors, <a href="https://creativecommons.org/licenses/by-sa/2.0/">CC-BY-SA</a>, Imagery © <a href="https://www.mapbox.com/">Mapbox</a>',
	maxZoom: 18,
	id: 'mapbox/streets-v11',
	tileSize: 512,
	zoomOffset: -1,
	accessToken: 'pk.eyJ1IjoiNTVhYmhpbGFzaCIsImEiOiJjazg0a2t2bW8xMnE0M25vd3FwNXQzZ3BwIn0.3dbqJ8xSkjN9lrstWlPM5g'
}).addTo(myMap);

