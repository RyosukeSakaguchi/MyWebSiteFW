/**
 *
 */
$(function() {
	$('#colour-variations ul').styleSwitcher({
		defaultThemeId : 'theme-switch',
		hasPreview : false,
		cookie : {
			expires : 30,
			isManagingLoad : true
		}
	});
	$('.option-toggle').click(function() {
		$('#colour-variations').toggleClass('sleep');
	});
});