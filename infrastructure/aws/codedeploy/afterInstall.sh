#!/bin/bash

#sudo systemctl stop tomcat.service
#
#sudo rm -rf /opt/tomcat/webapps/docs  /opt/tomcat/webapps/examples /opt/tomcat/webapps/host-manager  /opt/tomcat/webapps/manager /opt/tomcat/webapps/ROOT
#
#sudo chown tomcat:tomcat /opt/tomcat/webapps/ROOT.war
#
## cleanup log files
#sudo rm -rf /opt/tomcat/logs/catalina*
#sudo rm -rf /opt/tomcat/logs/*.log
#sudo rm -rf /opt/tomcat/logs/*.txt

# sudo /opt/aws/amazon-cloudwatch-agent/bin/amazon-cloudwatch-agent-ctl -m ec2 -a status
# sudo /opt/aws/amazon-cloudwatch-agent/bin/amazon-cloudwatch-agent-ctl -m ec2 -a stop
# sudo /opt/aws/amazon-cloudwatch-agent/bin/amazon-cloudwatch-agent-ctl -a fetch-config -m ec2 \
# -c file:/opt/cloudwatch-config.json

cd home/centos/tomcat/bin
sudo /opt/aws/amazon-cloudwatch-agent/bin/amazon-cloudwatch-agent-ctl -a fetch-config -m ec2 -c file:/home/centos/tomcat/cloudwatch_config.json -s
sudo ./startup.sh
sudo ./shutdown.sh