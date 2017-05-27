 <?php
ini_set('display_errors', 1);
 $db_name = "";
 $mysql_user = "";
 $mysql_pass = "";
 $server_name = "";
 $conn = mysqli_connect($server_name,$mysql_user,$mysql_pass,$db_name);
 $name = $_POST["message"];
$sign = $_POST["signature"];
 $sql =mysqli_query($conn, "insert into whiteboard (message, signature)  values$



?>
