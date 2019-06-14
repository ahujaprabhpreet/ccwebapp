## Cloud setup using Bash Script

## Description

csye6225-aws-networking-setup.sh : This file contains shell commands for aws cloud formation. VPC properties like name of the VPC, availability zones, CIDR blocks, IP route tables, subnet allocations and internet gateways can be defined here.

csye6225-aws-networking-termination.sh : This is a bash script to delete the VPC stack using the bash procedure. 


## Command to create stack 
Enter this command: <br>
./csye6225-aws-networking-setup.sh : This command asks for VPC names and subnet names from the users and helps in configuring the cloud using bash script 

From the command prompt enter the name of the VPC, if the name exists it gives a proper response message. add the subnet names as well.

## Command to Delete stack 
Enter this command: <br> 
./csye6225-aws-networking-termination.sh: This command deletes the cloud stack 

From the command promt enter name of the stack to delete. <br>
Please enter vpc name : my-vpc
