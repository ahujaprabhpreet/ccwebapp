#! /bin/bash

VPCName=$STACK_NAME-"VPC"
Subnet1Name=$STACK_NAME-"Subnet1"
Subnet2Name=$STACK_NAME-"Subnet2"
Subnet3Name=$STACK_NAME-"Subnet3"
InternetGatewayName=$STACK_NAME-"InternetGateway"
RouteTableName=$STACK_NAME-"RouteTable"


aws cloudformation create-stack --stack-name $STACK_NAME --template-body file:///home/puneet/csye6225/ccwebapp/infrastructure/aws/cloudformation/csye6225-cf-networking.json --parameters ParameterKey=VPCName,ParameterValue=$VPCName ParameterKey=Subnet1Name,ParameterValue=$Subnet1Name ParameterKey=Subnet2Name,ParameterValue=$Subnet2Name ParameterKey=Subnet3Name,ParameterValue=$Subnet3Name ParameterKey=InternetGatewayName,ParameterValue=$InternetGatewayName ParameterKey=RouteTableName,ParameterValue=$RouteTableName

aws cloudformation wait stack-create-complete --stack-name $STACK_NAME

if [ $? -eq 0 ]; then
echo "stack created successfully"
else
echo "stack not created"
fi