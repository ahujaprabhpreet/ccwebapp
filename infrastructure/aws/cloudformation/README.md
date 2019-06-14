## Cloud Formation

## Description

csye6225-cf-networking.json file contains the template for aws cloud formation 

csye6225-aws-cf-create-stack.sh is a bash file which takes the template from json file and create a stack 

csye6225-aws-cf-terminate-stack.sh is a bash file to delete a stack 


## Command to create stack 
Enter this command 
./csye6225-aws-cf-create-stack.sh

follow the command promt
Please enter vpc name : my-vpc

Stack Created Successfully 
OR
Unsuccessfull




## Command to Delete stack 
Enter this command 
./csye6225-aws-cf-terminate-stack.sh

follow the command promt
Please enter vpc name : my-vpc

Stack Deleted Successfully 
OR
Unsuccessfull

