#!/bin/bash
echo Before proceeding please update rootPOM path in nrby_automation.xml file
echo Enter Project directory path : "$ppath"
read ppath
echo Enter nrby_automation.xml path: "$xmlpath"
read xmlpath
cd $xmlpath
echo Enter the Jenkins Job name(Any): "$jobname"
read jobname
echo Please enter the Host name[eg:http://208.78.110.81:8080/]: "$hostname"
read hostname
java -jar jenkins-cli.jar -s $hostname create-job $jobname < nrby_automation.xml
echo Jenkins Job created successfully.
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
