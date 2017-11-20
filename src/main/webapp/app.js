var optiData = null;
var categories = [];
$(document).ready(function(event) {

	$('.month-box').on('click', function(event) {
		$('#category-dropdown li').remove();
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
				createMap(data);
			},
			error : function(data) {
				alert('error');
			}
		});
	});

	$('#filter_container').hide();

	$('#filter_container').on('click', '.catg-selector', function(event) {
		var filterVal = $(this).data('val');
		var filteredData = [];
		$('#dropdownMenu1').html(filterVal + '<span class="caret"></span>');
		if ("ALL" == filterVal) {
			filteredData = optiData;
		} else {
			$(optiData).each(function(index, value) {
				if (value.klass == filterVal) {
					filteredData.push(value);
				}
			});
		}
		createMap(filteredData);
	});

});

function filterCategories() {
	var list = document.getElementById("category-dropdown");
	var li = document.createElement("li");
	var link = document.createElement("a");
	var text = document.createTextNode("ALL");
	link.appendChild(text);
	link.href = "#!";
	link.setAttribute('class', "catg-selector");
	link.setAttribute('data-val', "ALL");
	li.appendChild(link);
	list.appendChild(li);
	$(optiData).each(function(index, value) {

		if (categories.indexOf(value.klass) < 0) {
			var li = document.createElement("li");
			var link = document.createElement("a");
			var text = document.createTextNode(value.klass);
			link.appendChild(text);
			link.href = "#!";
			link.setAttribute('class', "catg-selector");
			link.setAttribute('data-val', value.klass);
			li.appendChild(link);
			list.appendChild(li);
			categories.push(value.klass);
		}
	});
}

function createMap(data) {
	if (data != null && data.length > 0) {
		$('#filter_container').show();
		var mapOptions = {
			center : data[0].coordinates,
			zoom : 3,
			mapTypeId : google.maps.MapTypeId.ROADMAP
		};

		var map = new google.maps.Map(document.getElementById('map_container'),
				mapOptions);
		var infowindow = new google.maps.InfoWindow();

		$(data).each(
				function(index, value) {

					var marker = new google.maps.Marker({
						position : value.coordinates,
						label : 'D',
						animation : google.maps.Animation.DROP,
						map : map
					});

					google.maps.event.addListener(marker, 'mouseover',
							function() {
								infowindow.setContent('<strong>' + value.desc
										+ '</strong>');
								infowindow.open(map, marker);
							});

					google.maps.event.addListener(marker, 'mouseout',
							function() {
								infowindow.close();
							});

					if (value.suppliers != null) {
						console.log('supplue');
						$(value.suppliers).each(
								function(index, supplierData) {

									var marker = new google.maps.Marker({
										position : supplierData.coordinates,
										title : supplierData.place + index,
										label : 'S',
										animation : google.maps.Animation.DROP,
										strokeColor : '#7ee17e',
										map : map
									});

									var flightPlanCoordinates = [
											value.coordinates,
											supplierData.coordinates ];
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
	} else {
		$('#map_container').html("");
		$('#filter_container').hide();
	}
}
