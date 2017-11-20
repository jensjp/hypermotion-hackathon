var optiData = null;
var categories = [];

$(document).ready(function(event) {

	$('.month-box').on('click', function(event) {
		$('#filter_container').html("");
		optiData = null;
		categories = [];
		$('div').find('.month-box').removeClass('month-checked');
		$(this).addClass('month-checked');
		var filterVal = $(this).data('val');
		var url = "search/" + filterVal;
		$.ajax({
			url : url,
			success : function(data) {
				optiData = data;
				filterCategories();
				createMap(data, false);
			},
			error : function(data) {
				alert('error');
			}
		});
	});

	$('#supply-btn').on('click', function(event) {
		tryFilter(true);
	});

	$('#filter_container').hide();

	$('#filter_container').on('change', '.catg-input', function(event) {
		tryFilter(false);
	});

});

function tryFilter(includeSupply) {
	var filterVals = [];
	$('#filter_container').find('.catg-input').each(function(index, val) {
		if ($(val).is(':checked')) {
			filterVals.push($(val).data('val'));
		}
	});
	var filteredData = [];
	if (filterVals.length == 0) {
		filteredData = optiData;
	} else {
		$(optiData).each(function(index, value) {
			if (filterVals.indexOf(value.klass) >= 0) {
				filteredData.push(value);
			}
		});
	}
	createMap(filteredData, includeSupply);
}

function filterCategories() {
	$(optiData).each(
			function(index, value) {
				if (categories.indexOf(value.klass) < 0) {
					categories.push(value.klass);
					$('#filter_container').append(
							'<input type="checkbox" class="catg-input" data-val='
									+ value.klass
									+ '> <label style="margin-right:15px;">'
									+ value.klass + '</label></input>');
				}
			});

}

function createMap(data, showSupply) {
	if (data != null && data.length > 0) {
		$('#filter_container').show();
		var mapOptions = {
			center : data[0].coordinates,
			zoom : 3,
			mapTypeId : google.maps.MapTypeId.ROADMAP
		};

		var map = new google.maps.Map(document.getElementById('map_container'),
				mapOptions);

		$(data)
				.each(
						function(index, value) {

							var circle = new google.maps.Circle({
								strokeColor : "#FF0000",
								strokeOpacity : 0.8,
								strokeWeight : 2,
								fillColor : '#FF0000',
								fillOpacity : 0.35,
								map : map,
								center : value.coordinates,
								radius : 75000
							});

							var infowindow = new google.maps.InfoWindow({
								content : '<strong><div>' + value.desc
										+ '</div><div>' + value.klass
										+ '</div><div>' + value.weight
										+ ' KG</div><div>' + value.volume
										+ ' CBM</div></strong>',
								position : value.coordinates
							});

							google.maps.event.addListener(circle, 'mouseover',
									function(ev) {
										infowindow.open(map);
									});

							google.maps.event.addListener(circle, 'mouseout',
									function() {
										infowindow.close();
									});

							if (showSupply && value.suppliers != null) {
								$(value.suppliers)
										.each(
												function(index, supplierData) {

													var supplyCircle = new google.maps.Circle(
															{
																strokeColor : "#4BA350",
																strokeOpacity : 0.8,
																strokeWeight : 2,
																fillColor : '#4BA350',
																fillOpacity : 0.35,
																map : map,
																center : supplierData.coordinates,
																radius : 75000
															});

													var supplyInfoWindow = new google.maps.InfoWindow(
															{
																content : '<strong><div>'
																		+ value.place
																		+ '</div><div>'
																		+ value.weight
																		+ ' KG</div><div>'
																		+ value.volume
																		+ ' CBM</div></strong>',
																position : supplierData.coordinates
															});

													google.maps.event
															.addListener(
																	supplyCircle,
																	'mouseover',
																	function(ev) {
																		supplyInfoWindow
																				.open(map);
																	});

													google.maps.event
															.addListener(
																	supplyCircle,
																	'mouseout',
																	function() {
																		supplyInfoWindow
																				.close();
																	});

													if (supplierData.connection == null) {
														var flightPlanCoordinates = [
																supplierData.coordinates,
																value.coordinates ];
														var flightPath = new google.maps.Polyline(
																{
																	path : flightPlanCoordinates,
																	geodesic : true,
																	strokeColor : '#FAFE09',
																	strokeOpacity : 1.0,
																	strokeWeight : 1
																});
														flightPath.setMap(map);
													} else {
														var _oneColorCode = supplierData.connection.isLH ? '#FAFE09'
																: '#F02906';
														var _twoColorCode = supplierData.connection.isLH ? '#F02906'
																: '#FAFE09';

														var inmarker = new google.maps.Marker(
																{
																	position : supplierData.connection.coordinates,
																	label : 'I',
																	animation : google.maps.Animation.DROP,
																	map : map
																});

														var inInfowindow = new google.maps.InfoWindow(
																{
																	content : '<strong>'
																			+ supplierData.connection.place
																			+ '</strong>',
																	position : supplierData.connection.coordinates
																});

														google.maps.event
																.addListener(
																		inmarker,
																		'mouseover',
																		function() {
																			inInfowindow
																					.open(
																							map,
																							inmarker);
																		});

														google.maps.event
																.addListener(
																		inmarker,
																		'mouseout',
																		function() {
																			inInfowindow
																					.close();
																		});

														var oneflightPlanCoordinates = [
																supplierData.coordinates,
																supplierData.connection.coordinates ];
														var flightPath = new google.maps.Polyline(
																{
																	path : oneflightPlanCoordinates,
																	geodesic : true,
																	strokeColor : _oneColorCode,
																	strokeOpacity : 1.0,
																	strokeWeight : 1
																});
														flightPath.setMap(map);

														var twoflightPlanCoordinates = [
																supplierData.connection.coordinates,
																value.coordinates ];
														var flightPath = new google.maps.Polyline(
																{
																	path : twoflightPlanCoordinates,
																	geodesic : true,
																	strokeColor : _twoColorCode,
																	strokeOpacity : 1.0,
																	strokeWeight : 1
																});
														flightPath.setMap(map);

													}

												});
							}

						});
	} else {
		$('#map_container').html("");
		$('#filter_container').hide();
	}
}
