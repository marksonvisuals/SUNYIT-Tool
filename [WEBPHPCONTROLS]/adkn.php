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
	$contents = preg_replace("<br>", "", $contents);
	for($i=0;$i<5;$i++)
	{
		$pos = strpos($contents, "<span class=\"user-avail\">");
		$info = substr($contents,$pos+25,50);
		$contents = preg_replace("<span class=\"user-avail\">", "", $contents,1);

		$info = get_string_between($info, "(", ")");
		$info = "(".$info.")";
		if($i==0)
			{
			$laundry = "ADK N: ". trim($info);
			$adkn = $info;
			}

	
	}
	fclose($handle);
	$laundry = str_replace(">", "", $laundry);
	$laundry = str_replace("<", "", $laundry);
	$laundry = str_replace("/span","",$laundry);
	$laundry = str_replace("                      ","",$laundry);
	$laundry = str_replace("<span class=\"user-avail\">", " ",$laundry);
    $laundry = str_replace("</span>", " ",$laundry);

	
	print $adkn;
?>