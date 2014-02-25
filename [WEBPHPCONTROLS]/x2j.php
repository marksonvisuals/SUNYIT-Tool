<?php
function get_string_between($string, $start, $end){
        $string = " ".$string;
        $ini = strpos($string,$start);
        if ($ini == 0) return "";
        $ini += strlen($start);   
        $len = strpos($string,$end,$ini) - $ini;
        return substr($string,$ini,$len);
}

// Open XML file with SimpleXML.
$url = $_GET['url'];

$xml = simplexml_load_file($url);
// Convert XML content to JSON.
$json = json_encode($xml);

$jsonc = str_replace("\\", "", $json);
$jsonf = str_replace(" EST", "", $jsonc);


$getf = "{\"items\": [" . get_string_between($jsonf,"[", "]") . "]}";

// Output JSON.
echo $getf;

?>