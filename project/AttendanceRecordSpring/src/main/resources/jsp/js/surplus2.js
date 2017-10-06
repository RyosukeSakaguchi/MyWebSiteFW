/**
 *
 */

$(function() {

	if ($.cookie('layoutCookie') != '') {
		$('body').addClass($.cookie('layoutCookie'));
	}

	$('a[data-layout="boxed"]').click(function(event) {
		event.preventDefault();
		$.cookie('layoutCookie', 'boxed', {
			expires : 7,
			path : '/'
		});
		$('body').addClass($.cookie('layoutCookie')); // the value is boxed.
	});

	$('a[data-layout="wide"]').click(function(event) {
		event.preventDefault();
		$('body').removeClass($.cookie('layoutCookie')); // the value is
															// boxed.
		$.removeCookie('layoutCookie', {
			path : '/'
		}); // remove the value of our cookie 'layoutCookie'
	});
});