#!/bin/bash

echo 'AWS Virtual Private Cloud Setup Initiated...'

# reading parameters from the user
echo -n 'VPC Name: '
read VPC_NAME
echo -n 'Subnet 1: '
read SUBNET_NAME_1
echo -n 'Subnet 2: '
read SUBNET_NAME_2
echo -n 'Subnet 3: '
read SUBNET_NAME_3

# defining the CIDR block
VPC_CIDR="192.170.0.0/16"
SUBNET_CIDR1="192.170.0.0/26"
SUBNET_ZONE1="us-east-1a"
SUBNET_CIDR2="192.170.0.64/26"
SUBNET_ZONE2="us-east-1b"
SUBNET_CIDR3="192.170.0.128/26"
SUBNET_ZONE3="us-east-1c"

# creating the VPC
echo 'Creating VPC...'
VPC=$(aws ec2 create-vpc \
	--cidr-block $VPC_CIDR \
	--query 'Vpc.{VpcId:VpcId}'\
	--output text )

if [[ "$VPC" ==  "" ]]; then
	echo "[error] VPC creation failed!" 1>&2
	exit 1
fi
echo "	VPC ID '$VPC' "

# Naming the VPC
echo "Naming the created VPC..."
aws ec2 create-tags \
 --resources $VPC \
	--tags "Key=Name, Value=$VPC_NAME-csye6225-vpc"
echo " VPC ID '$VPC' NAMED as $VPC_NAME-csye6225-vpc.  "

#add dns support
modify_response=$(aws ec2 modify-vpc-attribute \
    --vpc-id "$VPC" \
    --enable-dns-support "{\"Value\":true}")

#add dns hostnames
modify_response=$(aws ec2 modify-vpc-attribute \
    --vpc-id "$VPC" \
    --enable-dns-hostnames "{\"Value\":true}")

# Creating Public Subnet 1
echo "Creating Subnet 1..."
SUBNET_ID1=$(aws ec2 create-subnet \
	--vpc-id $VPC \
    --cidr-block $SUBNET_CIDR1 \
    --availability-zone $SUBNET_ZONE1 \
    --query 'Subnet.{SubnetId:SubnetId}' \
    --output text )
if [[ "$SUBNET_ID1" == "" ]]; then
	echo "[error] Subnet 1 creation failed!" 1>&2
	exit 1
fi
echo "  Subnet ID '$SUBNET_ID1' CREATED in '$SUBNET_ZONE1'" \
   "Availability Zone."

# Adding name tags to Subnet 1
aws ec2 create-tags \
	--resources $SUBNET_ID1 \
	--tags "Key=Name,Value=$SUBNET_NAME_1"
echo "  Subnet ID '$SUBNET_ID1' NAMED as" \
  "'$SUBNET_NAME_1'."

# Creating Public Subnet 2
echo "Creating Subnet 2..."
SUBNET_ID2=$(aws ec2 create-subnet \
	--vpc-id $VPC \
    --cidr-block $SUBNET_CIDR2 \
    --availability-zone $SUBNET_ZONE2 \
    --query 'Subnet.{SubnetId:SubnetId}' \
    --output text )
if [[ "$SUBNET_ID2" == "" ]]; then
	echo "[error] Subnet 2 creation failed!" 1>&2
	exit 1
fi
echo "  Subnet ID '$SUBNET_ID2' CREATED in '$SUBNET_ZONE2'" \
   "Availability Zone."

# Adding name tags to Subnet 2
aws ec2 create-tags \
	--resources $SUBNET_ID2 \
	--tags "Key=Name,Value=$SUBNET_NAME_2"
echo "  Subnet ID '$SUBNET_ID2' NAMED as" \
  "'$SUBNET_NAME_2'."

# Creating Public Subnet 3
echo "Creating Subnet 3..."
SUBNET_ID3=$(aws ec2 create-subnet \
	--vpc-id $VPC \
    --cidr-block $SUBNET_CIDR3 \
    --availability-zone ${SUBNET_ZONE3} \
    --query 'Subnet.{SubnetId:SubnetId}' \
    --output text )
if [[ "$SUBNET_ID3" == "" ]]; then
	echo "[error] Subnet 3 creation failed!" 1>&2
	exit 1
fi
echo "  Subnet ID '$SUBNET_ID3' CREATED in '$SUBNET_ZONE3'" \
   "Availability Zone."

# Adding name tags to Subnet 3
aws ec2 create-tags \
	--resources $SUBNET_ID3 \
	--tags "Key=Name,Value=$SUBNET_NAME_3"
echo "  Subnet ID '$SUBNET_ID3' NAMED as" \
  "'$SUBNET_NAME_3'."


# Internet Gateway creation
echo "Creating Internet Gateway..."
IGW=$(aws ec2 create-internet-gateway \
    --query 'InternetGateway.{InternetGatewayId:InternetGatewayId}' \
    --output text )
if [[ "$IGW" == "" ]]; then
	echo "[error] Internet Gateway creation failed!" 1>&2
	exit 1
fi
echo "  Internet Gateway ID '$IGW' CREATED."

# Adding Name tag to Internet Gateway
aws ec2 create-tags \
    --resources $IGW \
    --tags "Key=Name,Value=$VPC_NAME"
echo "  Subnet ID '$IGW' NAMED as" \
  "'$VPC_NAME'."

# Attach Internet gateway to your VPC
aws ec2 attach-internet-gateway \
    --vpc-id $VPC \
    --internet-gateway-id $IGW
echo "  Internet Gateway ID '$IGW' ATTACHED to VPC ID '$VPC'."

# Route Table Creation
echo "Creating Route Table..."
ROUTE_TABLE=$(aws ec2 create-route-table \
    --vpc-id $VPC \
    --query 'RouteTable.{RouteTableId:RouteTableId}' \
    --output text )
if [[ "$ROUTE_TABLE" == "" ]]; then
	echo "[error] Route Table creation failed!" 1>&2
	exit 1
fi
echo "  Route Table ID '$ROUTE_TABLE' CREATED."

# Add Name tag to Route table
aws ec2 create-tags \
    --resources $ROUTE_TABLE \
    --tags "Key=Name,Value=$VPC_NAME-csye6225-rt"
echo "  Subnet ID '$ROUTE_TABLE' NAMED as" \
  "$VPC_NAME-csye6225-rt."

# Associate Subnet 1 with Route Table
RESULT_1=$(aws ec2 associate-route-table  \
    --subnet-id $SUBNET_ID1 \
    --route-table-id $ROUTE_TABLE )
if [[ "$RESULT_1" == "" ]]; then
	echo "[error] Subnet1 association with Route Table failed!" 1>&2
	exit 1
fi
echo "Subnet ID '$SUBNET_ID1' ASSOCIATED with Route Table ID" \
  "'$ROUTE_TABLE'."

# Associate Subnet 2 with Route Table
RESULT_2=$(aws ec2 associate-route-table  \
    --subnet-id $SUBNET_ID2 \
    --route-table-id $ROUTE_TABLE )
 if [[ "$RESULT_2" == "" ]]; then
	echo "[error] Subnet2 association with Route Table failed!" 1>&2
	exit 1
fi
echo "Subnet ID '$SUBNET_ID2' ASSOCIATED with Route Table ID" \
  "'$ROUTE_TABLE'."

# Associate Subnet 3 with Route Table
RESULT_3=$(aws ec2 associate-route-table  \
    --subnet-id $SUBNET_ID3 \
    --route-table-id $ROUTE_TABLE )
if [[ "$RESULT_3" == "" ]]; then
	echo "[error] Subnet3 association with Route Table failed!" 1>&2
	exit 1
fi
echo "Subnet ID '$SUBNET_ID3' ASSOCIATED with Route Table ID" \
  "'$ROUTE_TABLE'."

# Create route to Internet Gateway
RESULT=$(aws ec2 create-route \
    --route-table-id $ROUTE_TABLE \
    --destination-cidr-block 0.0.0.0/0 \
    --gateway-id $IGW )
if [[ "$RESULT" == "" ]]; then
	echo "[error] Route creation to Internet Gateway failed!" 1>&2
	exit 1
fi
echo "  Route to '0.0.0.0/0' via Internet Gateway ID '$IGW' ADDED to" \
  "Route Table ID '$ROUTE_TABLE'."


