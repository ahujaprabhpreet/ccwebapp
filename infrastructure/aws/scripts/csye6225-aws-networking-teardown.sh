#!/bin/bash

# reading the stack to be deleted
echo -n 'Enter Stack name to be deleted: '
read STACK_NAME


vpc=$(aws ec2 describe-vpcs \
    --filters "Name=tag:Name,Values="$STACK_NAME-csye6225-vpc"" \
    --query "Vpcs[*].VpcId" \
    --output text)

# check if VPC exits
if [[ "$vpc" == "" ]]; then
	echo "[error] VPC '$vpc' not found" 1>&2
	exit 1
fi
echo ${vpc}

# check if subnet 1 exits
subnet=$(aws ec2 describe-subnets --filters Name=vpc-id,Values=${vpc})

subnetid1=$(echo -e "$subnet" | jq '.Subnets[0].SubnetId' | tr -d '"')
if [[ "$subnetid1" == "null" ]]; then
	echo "[error] subnet 1 not found" 1>&2
	exit 1
fi
echo "Subnet 1 found '$subnetid1'"

# check if subnet 2 exists
subnetid2=$(echo -e "$subnet" | jq '.Subnets[1].SubnetId' | tr -d '"')
if [[ "$subnetid2" == "null" ]]; then
	echo "[error] subnet 2 not found" 1>&2
	exit 1
fi
echo "Subnet 2 found '$subnetid2'"

# check if subnet 3 exists
subnetid3=$(echo -e "$subnet" | jq '.Subnets[2].SubnetId' | tr -d '"')
if [[ "$subnetid3" == "null" ]]; then
	echo "[error] subnet 3 not found" 1>&2
	exit 1
fi
echo "Subnet 3 found '$subnetid3'"

# delete subnets
aws ec2 delete-subnet --subnet-id $subnetid1
if [[ "$?" != "0" ]]; then
    echo "Couldn't delete subnet 1"
    exit 77
fi
echo "Subnet 1 deleted successfully!"

aws ec2 delete-subnet --subnet-id $subnetid2
if [[ "$?" != "0" ]]; then
    alias
    exit 77
fi
echo "Subnet 2 deleted successfully!"

aws ec2 delete-subnet --subnet-id $subnetid3
if [[ "$?" != "0" ]]; then
    echo "Couldn't delete subnet 3"
    exit 77
fi
echo "Subnet 3 deleted successfully!"

# check if route exists
route=$(aws ec2 describe-route-tables \
    --filters "Name=tag:Name,Values="$STACK_NAME-csye6225-rt"" \
    --query "RouteTables[*].RouteTableId" \
    --output text)
echo "Route $route found."

# delete route
aws ec2 delete-route --route-table-id $route --destination-cidr-block 0.0.0.0/0
if [[ "$?" != "0" ]]; then
    echo "Couldn't delete route"
    exit 77
else
    echo "Route deleted."
    echo "Deleting route table..."
    aws ec2 delete-route-table --route-table-id $route
    if [[ "$?" != "0" ]]; then
        echo "Couldn't delete route table"
        exit 77
    else
        echo "Route Table deleted successfully!"
        echo "Detaching Internet Gateway..."
        gateway_id=$(aws ec2 describe-internet-gateways \
            --filters "Name=tag-value,Values="$STACK_NAME"" \
            --query "InternetGateways[*].InternetGatewayId" \
            --output text)
        
        if [[ "$gateway_id" == "" ]]; then
          	echo "[error] Internet Gateway not found" 1>&2
        	exit 77
        else
            echo "Gateway found!"
            aws ec2 detach-internet-gateway --internet-gateway-id $gateway_id --vpc-id $vpc
            if [[ "$?" != "0" ]]; then
                echo "Couldn't detach internet gateway"
                exit 77
            else
                echo "Internet Gateway detached successfully!"
                echo "Deleting internet gateway..."
                aws ec2 delete-internet-gateway --internet-gateway-id $gateway_id
                if [[ "$?" != "0" ]]; then
                    echo "Couldn't delete internet gateway"
                    exit 77
                else
                    echo "Internet Gateway deleted successfully!"
                    echo "Deleting VPC"
                    aws ec2 delete-vpc --vpc-id $vpc
                    if [[ "$?" != "0" ]]; then
                        echo "Couldn't delete vpc"
                        exit 77
                    else
                        echo "VPC and all it's dependencies are deleted successfully"
                    fi
                fi
            fi
        fi
    fi
fi

