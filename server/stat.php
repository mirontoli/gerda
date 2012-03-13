<?php
$fil = fopen("fil.txt", "a");
$text = $_POST["name"] . "\n";
fwrite($fil, $text);
fclose($fil);
?>
<html>
	<head>
		<title>Tack</title>
	</head>
	<body>
		<h1>Tack</h1>
		<p><?php echo date("Y-m-d");?></p>
		<p><?php echo $text;?></p>
	</body>
</html>
