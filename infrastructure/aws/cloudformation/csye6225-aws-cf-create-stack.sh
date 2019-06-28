#! /bin/bash

# get VPC name
echo -n "Please enter VPC stack name: "
read STACK_NAME

if [[ ${STACK_NAME} == "" ]]; then
    echo "Please enter a VPC stack name!"
    exit 1
else
    echo "VPC Stack name : ${STACK_NAME}"
fi

# checking stack exit or not
statuscheck=$(aws cloudformation describe-stacks --stack-name $STACK_NAME --query "Stacks[0].StackStatus" >/dev/null 2>&1)

if [[ $? -eq 0 ]]; then
    echo "VPC Stack ${STACK_NAME} already exists"
else

    # get subnet 1 name
    echo -n "Enter subnet 1 name: "
    read SUBNET1

    if [[ ${SUBNET1} == "" ]]; then
        echo "Please enter a Subnet 1  name!"
        exit 1
    else
        echo "Subnet 1 name : ${SUBNET1}"
    fi

    # get subnet 2 name
    echo -n "Enter subnet 2 name: "
    read SUBNET2

    if [[ ${SUBNET2} == "" ]]; then
        echo "Please enter a Subnet 2 name!"
        exit 1
    else
        echo "Subnet 2 name : ${SUBNET2}"
    fi

    # get subnet 3 name
    echo -n "Enter subnet 3 name: "
    read SUBNET3

    if [[ ${SUBNET3} == "" ]]; then
        echo "Please enter a Subnet 3 name!"
        exit 1
    else
        echo "Subnet 3 name : ${SUBNET3}"
    fi

    echo "VPC Stack generation initiated...!"
fi


if [[ $? -ne 0 ]]; then

    #creating variables
    VPCName=${STACK_NAME}
    Subnet1Name=${STACK_NAME}-${SUBNET1}
    Subnet2Name=${STACK_NAME}-${SUBNET2}
    Subnet3Name=${STACK_NAME}-${SUBNET3}
    InternetGatewayName=${STACK_NAME}-"InternetGateway"
    RouteTableName=${STACK_NAME}-"RouteTable"


    CidrVPC="11.0.0.0/16"
    CidrSubnet1="11.0.0.0/26"
    CidrSubnet2="11.0.0.64/26"
    CidrSubnet3="11.0.0.128/26"


    #creating stack from template
    aws cloudformation create-stack \
        --stack-name ${STACK_NAME} \
        --template-body file://csye6225-cf-networking.json \
        --parameters ParameterKey=VPCName,ParameterValue=${VPCName} \
            ParameterKey=Subnet1Name,ParameterValue=${Subnet1Name} \
            ParameterKey=Subnet2Name,ParameterValue=${Subnet2Name} \
            ParameterKey=Subnet3Name,ParameterValue=${Subnet3Name} \
            ParameterKey=InternetGatewayName,ParameterValue=${InternetGatewayName} \
            ParameterKey=RouteTableName,ParameterValue=${RouteTableName} \
            ParameterKey=CidrVPC,ParameterValue=${CidrVPC} \
            ParameterKey=CidrSubnet1,ParameterValue=${CidrSubnet1} \
            ParameterKey=CidrSubnet2,ParameterValue=${CidrSubnet2} \
            ParameterKey=CidrSubnet3,ParameterValue=${CidrSubnet3}

    if [[ $? -ne 0 ]]; then
        echo "Cloud Formation Stack creation not completed"
        exit 1
    fi
    echo "VPC creation using Cloud Formation in progress..."

    # waiting stack to create
    aws cloudformation wait stack-create-complete --stack-name ${STACK_NAME}
    if [[ $? -eq 0 ]]; then
        echo "VPC stack created successfully"
    else
        echo "VPC Stack ${STACK_NAME} not created"
    fi

else
    echo "Stack creation operation bypassed!"
fi




