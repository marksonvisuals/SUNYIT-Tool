// JavaScript Document

// the height of the div tags that contain the content
// of each menu tab
var height = 400;


///
/// add click functionality to each menu tab
///

$.fn.tabs = function () {

  var curr = "none";

  return this.each(function () {

    var $tabwrapper = $(this);

    var $panels = $tabwrapper.find ('> div.menu_info');
    var $tabs = $tabwrapper.find ('> ul li a');

    $tabs.click (function () {

      // temp is the tab where the user clicked
      // curr is the tab that is currently open

			var temp = $(this).parent().attr ('id');

			if (curr === "none") { // there are no open menus at the time of the click
				curr = temp;
				$panels
				.hide () // hide ALL the panels
				.filter (this.hash) // filter down to 'this.hash'
				.slideDown (750); // show only this one
				//return false;

			} else if (curr !== temp) { // the click was on a tab other than the currently open tab

				curr = temp;
				$panels
				//.hide () // hide ALL the panels
        .slideUp (750)
        .delay (800)
				.filter (this.hash) // filter down to 'this.hash'
        //.show (); // show only this one
        .slideDown (750);

				//return false;

			} else { // the click was on the currently open tab

				curr = "none";
				$panels.slideUp (750);

				//return false;
			}

      return false;
    });

		$('.hide_menu').click (function () {
			curr = "none";
			$panels.slideUp (750);

			return false;
		});
  });
};


///
/// set the heights of each content tab
///

$(function () {
  $('.menu_info').each (function () {

    $(this).height (height + 68).hide;
    $(this).find ("> .menu_right").height (height - 20);
	});

	$('#main_menu').tabs ();

});
