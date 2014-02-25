	SUNYIT TOOL Andoid Application
    Copyright (C) 2014  Eric Markson (eric@marksonvisuals.com)

    This program is free software: you can redistribute it and/or modify
    it under the terms of the GNU General Public License as published by
    the Free Software Foundation, either version 3 of the License, or
    (at your option) any later version.

    This program is distributed in the hope that it will be useful,
    but WITHOUT ANY WARRANTY; without even the implied warranty of
    MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
    GNU General Public License for more details.

    You should have received a copy of the GNU General Public License
    along with this program.  If not, see <http://www.gnu.org/licenses/>.
	
<?php

function get_string_between($string, $start, $end){
        $string = " ".$string;
        $ini = strpos($string,$start);
        if ($ini == 0) return "";
        $ini += strlen($start);   
        $len = strpos($string,$end,$ini) - $ini;
        return substr($string,$ini,$len);
}


	$site = "http://www.laundryview.com/lvs.php";
	
	$handle = fopen($site, "rb");
	$contents = stream_get_contents($handle);
	$ork2=get_string_between($contents, "<div id=\"campus1\">","<script type=\"text/javascript\">");
	$ork2=get_string_between($ork2,"<a href=\"laundry_room.php?lr=407065\"", "<br>");
	$ork2=get_string_between($ork2, "user-avail\">", "</span>");
	fclose($handle);


	
	print $ork2;
?>