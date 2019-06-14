## Cloud Formation

## Description

csye6225-cf-networking.json : This file contains the template for aws cloud formation. VPC properties like name of the VPC, availability zones, CIDR blocks, IP route tables, subnet allocations and internet gateways can be defined here.

csye6225-aws-cf-create-stack.sh : This is a bash script which takes the template from json file and create a stack based on the user configured properties

csye6225-aws-cf-terminate-stack.sh : This is a bash script to delete the VPC stack using the cloud formation procedure. 


## Command to create stack 
Enter this command 
./csye6225-aws-cf-create-stack.sh : This command runs the said script and creates the cloud architecture based on the structure provided in the JSON. 

From the command prompt enter the name of the VPC, if the name exists it gives a proper response message
Please enter vpc name : my-vpc

## Command to Delete stack 
Enter this command 
./csye6225-aws-cf-terminate-stack.sh: This command deletes the cloud stack 

From the command promt enter name of the stack to delete.
Please enter vpc name : my-vpc

