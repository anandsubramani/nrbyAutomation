#!/bin/bash
echo Please provide your Project directory : "$ppath"
read ppath
xmlpath=$ppath/nrby_jenkins
cd $xmlpath
echo Please enter the Job name: "$jobname"
read jobname
echo Please enter the Host name[eg:http://10.6.5.13:8080/]: "$hostname"
read hostname
java -jar jenkins-cli.jar -s $hostname create-job $jobname < nrby_automation.xml
echo Job created successfully
file="$ppath"
cd $file
mkdir -p target/surefire-reports
cd $file/target/surefire-reports/
rm -f index.html
cd $xmlpath
java -jar jenkins-cli.jar -s $hostname build $jobname -s &
while : ; do 
    [[ -f "$ppath/target/surefire-reports/index.html" ]] && break   
done
    cd $ppath/target/
    pwd
    jepath=$(locate .jenkins/jobs | head -1)
    echo $jepath
    zip -r reports.zip surefire-reports 
    sleep 2
    mv reports.zip $jepath/$jobname/workspace
