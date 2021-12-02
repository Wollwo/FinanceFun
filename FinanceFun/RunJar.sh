#!/bin/bash
#
#
#
if [ ! -f $(ls | grep .jar) ] 
then
	echo "No JAR to run"
else
	java -jar $(ls | grep .jar)
fi

echo "Press ENTER"
exit
