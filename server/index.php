<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
    <head>
        <title>Gerda </title>
        <meta http-equiv="Content-Type" content="text/html; charset=utf-8">
        <link rel="shortcut icon" href="img/logo.jpg">

        <link rel="stylesheet" href="style.css" type="text/css">

    </head>

    <body>
        <div id="page">

            <div id="header">


                <a href="index.php" title="Hemsida"><img src="img/logo.jpg" width="80" alt="gerda logo"></a> 


                <a  href="index.php?q=soft" title="Program">Program</a> |
                <a  href="index.php?q=stat" title="Statistic">Statistik</a> |
                <a  href="index.php?q=ex" title="Övningar">Övningar</a> |
		<a  href="http://isp.chuvash.eu" title="utvecklar-portalen">Wiki</a>
            </div>


            <div id="leftcol">


                <ul>
                    <li><a  href="http://www.gerdahallen.lu.se/" target="_blank" title="Gerda hemsidan">Gerdahallen</a></li>
                    <li><a  href="http://kroppochhälsa.se/" target="_blank" title="Hälsa">Kropp&Hälsa</a></li>
                    <li><a href="http://www.fitnessbutiken.se/index.php?cPath=15&gclid=CP2r1q-9qpwCFUgTzAodsTnZjw" target="_blank" title="Butiken">Butiken</a></li>
                    <li><a href="http://newsbrook.se/om/gymnastik/" target="_blank" title="Tidning">Tidning</a></li>
                    <li><a href="http://www.bodybuilding.com/fun/exername.php?MainMuscle=Abdominals" target="_blank" title="Övningar">Övningar</a></li>
                    <li><a href="http://www.kolozzeum.com/forum/archive/index.php/t-31453.html" target="_blank" title="Forum">Forum</a></li>
                </ul>
                <p> <a href="http://validator.w3.org/check?uri=referer"><img src="http://www.w3.org/Icons/valid-html401" alt="Valid HTML 4.01 Transitional" height="31" width="88" title="html-validator"></a>
                    <a href="http://jigsaw.w3.org/css-validator/check/referer"><img style="border:0;width:88px;height:31px" src="http://jigsaw.w3.org/css-validator/images/vcss-blue" alt="Valid CSS!" title="css-validator" ></a></p>		
            </div>

            <div id="maincol">
                <!--Denna boxen skall användas för att skriva uppgifter och visa artikel. Här skrev jag vad man kan välja efter denna kurs.-->
                <div id="box">
                    <?php
                    if(!isset($_GET["q"])) {
                        include("welcome.htm"); 
                    }
                    else {
                        if ($_GET["q"]=="ex") {
                            $fil = fopen("fil.txt", "r");
                            $contents = '';
                            while (!feof($fil)) {
                                $contents .= fread($fil, 8192);
                            }
                            fclose($fil);
			    echo "<p>";
                            echo nl2br($contents);
			    echo "</p>";
                            
                        }
                        elseif ($_GET["q"] == "stat") {
			    include("stat.htm");
                        }
			elseif ($_GET["q"] == "soft") {
			    if (file_exists("soft.htm")) {
  			       include("soft.htm");
			    }
			    else {
			       echo "Sidan hittades inte";
			    }
			}
                    }
                    
                    ?>



                </div>
                <h4>Skapad av: Dogan Alkan & Anatoly Mironov</h4>

            </div>

        </div>



    </body>
</html>

