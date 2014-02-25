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

	$site = "http://www.sunyit.edu/apps/canceled_classes/";
	$handle = fopen($site, "rb");
	$contents = stream_get_contents($handle);
	
	$contents = get_string_between($contents, "<!-- substituted in backend -->", "<!-- End Canceled Classes TABLE -->");
	$numClasses = substr_count($contents, '<tr>');
	//$numClasses = get_string_between($contents, "<p>", " canceled classes</p>");
	//$contents = strstr($contents, '</tr>');
	//$cancelled = "Cancelled Classes: <br>";
	
	$out = "{\"items\": [";
		
	if($numClasses < 1)
		$out .= "{\"title\":\"0 Classes Canceled\",\"course\":\"\",\"num\":\"\",\"crn\":\"\",\"instructor\":\"\",\"pubDate\":\"\"}";
		//echo($cancelled);
	for($i=0;$i<$numClasses;$i++)
	{
		$classi = get_string_between($contents, "<tr>", "</tr>");
		$classi = "<tr>" . $classi;
		
		if(strpos($classi,"ccdatarev")===FALSE){
			$date = get_string_between($classi, "<tr><td class='ccdata'>","</td>");
			$course = get_string_between($classi,"</A></td><td class='ccdata'>","</td>");	
			$num = get_string_between($classi, ".htm#",">");	
			$crn = get_string_between($classi, $num.">","</A></td>");
			$title = get_string_between($classi,"$course</td><td class='ccdata'>","</td>");	
			$instructor = get_string_between($classi,"$title</td><td class='ccdata'>","</td>");
		}else{
			$date = get_string_between($classi, "<tr><td class='ccdatarev'>","</td>");
			$course = get_string_between($classi,"</A></td><td class='ccdatarev'>","</td>");
			$num = get_string_between($classi, ".htm#",">");
			$crn = get_string_between($classi, $num.">","</A></td>");		
			$title = get_string_between($classi,"$course</td><td class='ccdatarev'>","</td>");
			$instructor = get_string_between($classi,"$title</td><td class='ccdatarev'>","</td>");	
			
			$cancelled = str_replace("<br />"," ",$cancelled);
			$cancelled = str_replace("&amp;","",$cancelled);	
		}

		$contents = str_replace($classi,"", $contents);
		
		//$cancelled = $cancelled . $date . " | " . $num . " | " . $crn . " | ".  $title . " | " . $instructor . "\n\n";
		$cancelled = $cancelled . "{\"title\":\"". $title ."\",\"course\":\"". $course ."\",\"num\":\"". $num ."\",\"crn\":\"". $crn ."\",\"instructor\":\"". $instructor ."\",\"pubDate\":\"". $date . "\"}";
		$cancelled = str_replace("<br />"," ",$cancelled);
		$cancelled = str_replace("&amp;","",$cancelled);
		$out .= $cancelled;
		//echo ($cancelled);
	}
	
	$out .= "]}";
	echo($out);
	
	//$today = "\nLast Updated: " . date("F j, Y, g:i a");
	
	//echo $today;
	fclose($handle);
	//$cancelled = str_replace("<br />"," | ",$cancelled);
	//$cancelled = str_replace("&amp;","",$cancelled);
	
	
	//echo ($cancelled);
	
	?>