<?php

$db = "";
$user = "";
$pass = "";
$server = "";


$con = mysqli_connect($server,$user,$pass,$db);

$sql = "SELECT * FROM whiteboard";

$r = mysqli_query($con,$sql);

$result = array();

while($row = mysqli_fetch_array($r)){
    array_push($result,array(
        'message'=>$row['message'],
        'signature'=>$row['signature']
    ));
}

echo json_encode(array('result'=>$result));

mysqli_close($con);
?>
