#!/bin/bash
echo Please provide your Project directory : "$ppath"
read ppath
echo Please provide nrby_automation.xml path: "$xmlpath"
read xmlpath
cd $xmlpath
echo Please enter the Job name: "$jobname"
read jobname
echo Please enter the Host name[eg:http://10.6.5.13:8080/]: "$hostname"
read hostname
java -jar jenkins-cli.jar -s $hostname create-job $jobname < nrby_automation.xml
echo Job created successfully
file="$ppath/target/surefire-reports/"
cd $file
rm -f index.html
cd $xmlpath
java -jar jenkins-cli.jar -s $hostname build $jobname -s &
while : ; do 
    [[ -f "$ppath/target/surefire-reports/index.html" ]] && break   
    
done
    cd $ppath/target/
    jpath=$(locate .jenkins/jobs | grep /.jenkins/jobs$)
    sleep 1
    zip -r reports.zip surefire-reports 
    cp reports.zip $jpath/$jobname/workspace/