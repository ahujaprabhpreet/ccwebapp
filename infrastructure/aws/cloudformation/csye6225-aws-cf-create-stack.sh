#! /bin/bash

echo "please enter stack name"
read STACK_NAME

#checking stack exit or not
statuscheck=$(aws cloudformation describe-stacks --stack-name $STACK_NAME --query "Stacks[0].StackStatus" >/dev/null 2>&1)
if [ $? -ne 0 ]; then

    #creating variables
    VPCName=$STACK_NAME-"VPC"
    Subnet1Name=$STACK_NAME-"Subnet1"
    Subnet2Name=$STACK_NAME-"Subnet2"
    Subnet3Name=$STACK_NAME-"Subnet3"
    InternetGatewayName=$STACK_NAME-"InternetGateway"
    RouteTableName=$STACK_NAME-"RouteTable"
    CidrVPC="11.0.0.0/16"
    CidrSubnet1="11.0.0.0/26"
    CidrSubnet2="11.0.0.64/26"
    CidrSubnet3="11.0.0.128/26"


    #creating stack from template
    aws cloudformation create-stack --stack-name $STACK_NAME --template-body file://csye6225-cf-networking.json --parameters ParameterKey=VPCName,ParameterValue=$VPCName ParameterKey=Subnet1Name,ParameterValue=$Subnet1Name ParameterKey=Subnet2Name,ParameterValue=$Subnet2Name ParameterKey=Subnet3Name,ParameterValue=$Subnet3Name ParameterKey=InternetGatewayName,ParameterValue=$InternetGatewayName ParameterKey=RouteTableName,ParameterValue=$RouteTableName ParameterKey=CidrVPC,ParameterValue=$CidrVPC ParameterKey=CidrSubnet1,ParameterValue=$CidrSubnet1 ParameterKey=CidrSubnet2,ParameterValue=$CidrSubnet2 ParameterKey=CidrSubnet3,ParameterValue=$CidrSubnet3


    #wating stack to create
    aws cloudformation wait stack-create-complete --stack-name $STACK_NAME


    if [ $? -eq 0 ]; then
    echo "stack created successfully"
    else
    echo "stack not created"
    fi

else
echo "stack already exist"
fi




