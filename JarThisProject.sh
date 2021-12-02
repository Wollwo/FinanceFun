#!/bin/bash
#
# run like: ./JarThisProject.sh .jar    <= this is The way
#
#

if [ -z "$1" ]
then
    echo "forgor to add \".jar\" (or other) as parameter"
    exit 1
else
    VAR_END=$1
fi

if [ ! $(echo $VAR_END | egrep "^\.") ]
then
    echo "forgot \".\" before "$VAR_END
	exit 2
fi

if [ -f "MANIFEST.MF" ]
then

    VAR_MAIN_CLASS=$(grep Main-Class: MANIFEST.MF | awk -F" " '{print $2}')

    if [ -f $VAR_MAIN_CLASS".class" ]
    then
        jar cfm $VAR_MAIN_CLASS""$VAR_END MANIFEST.MF *.class
    else
        echo "main class \""$VAR_MAIN_CLASS"\" was not found in this forlder"
        exit 3
    fi
else
    echo "\"MANIFEST.MF\" does not exist in this forder"
fi

if [ -f $VAR_MAIN_CLASS"."$VAR_END ]
then
    echo "new Java file \""$VAR_MAIN_CLASS""$VAR_END"\" created"
else
    echo "unable to create \""$VAR_MAIN_CLASS""$VAR_END"\" file"
fi

echo "Press ENTER to close"
read a
exit 0
