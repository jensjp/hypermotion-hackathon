

$(document).ready(function(event) {	
	
	$('.month-box').on('click', function(event){
	    $('div').find('.month-box').removeClass('month-checked');
	      $(this).addClass('month-checked');
	      var filterVal = $(this).data('val');
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
			
			
				var marker = new google.maps.Marker({
					position : value.coordinates,
					title : value.place,
					label : 'D',
					animation : google.maps.Animation.DROP,
					map : map
				});
				
				
				if(value.suppliers != null){
					$(value.suppliers).each(function(index, supplierData) {
						
						var marker = new google.maps.Marker({
							position : supplierData.coordinates,
							title : supplierData.place+index,
							label : 'S',
							animation : google.maps.Animation.DROP,
							strokeColor : '#7ee17e',
							map : map
						});
						
						var flightPlanCoordinates = [ value.coordinates, supplierData.coordinates ];
						var flightPath = new google.maps.Polyline({
							path : flightPlanCoordinates,
							geodesic : true,
							strokeColor : '#b50303',
							strokeOpacity : 1.0,
							strokeWeight : 1
						});
						flightPath.setMap(map);
					});
				}
			
			

			
		});
	}
}
