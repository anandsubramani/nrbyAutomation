#!/bin/bash
echo Please provide nrby_automation.xml path: "$xmlpath"
read xmlpath
cd $xmlpath
echo Please enter the Job name: "$jobname"
read jobname
echo Please enter the Host name[eg:http://10.6.5.13:8080/]: "$hostname"
read hostname
java -jar jenkins-cli.jar -s $hostname create-job $jobname < nrby_automation.xml
echo Job created successfully
java -jar jenkins-cli.jar -s $hostname build $jobname -s