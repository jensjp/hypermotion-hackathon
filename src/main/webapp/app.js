

$(document).ready(function(event) {

	$('.month-selector').on('click', function(event) {
		var filterVal = $(this).data('val');
		alert(filterVal);
		var url = "search/" + filterVal;
		$.ajax({
			url : url,
			success : function(data) {
				createMap(data);
			},
			error : function(data) {
				alert('error');
			}
		});
	});

});

function createMap(optiData) {
	if (optiData != null) {
		
		var mapOptions = {
				center : optiData[0].coordinates,
				zoom : 3,
				mapTypeId : google.maps.MapTypeId.ROADMAP
			};

		var map = new google.maps.Map(document.getElementById('map_container'),
				mapOptions);

		$(optiData).each(function(index, value) {
			
			console.log(value.coordinates); 

			var marker = new google.maps.Marker({
				position : value.coordinates,
				// title : value,
				animation : google.maps.Animation.DROP,
				map : map
			});
		});

		/*var flightPlanCoordinates = [ {
			lat : 40.741895,
			lng : -73.989308
		}, {
			lat : 50.11092209999999,
			lng : 8.682126700000026
		} ];

		var flightPath = new google.maps.Polyline({
			path : flightPlanCoordinates,
			geodesic : true,
			strokeColor : '#b50303',
			strokeOpacity : 1.0,
			strokeWeight : 1
		});
		flightPath.setMap(map);*/

	}
}
