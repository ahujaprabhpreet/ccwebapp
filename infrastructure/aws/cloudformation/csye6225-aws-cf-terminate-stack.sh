#! /bin/bash

echo "plz enter stack name"
read STACK_NAME


#checking stack exit or not
statuscheck=$(aws cloudformation describe-stacks --stack-name $STACK_NAME --query "Stacks[0].StackStatus" >/dev/null 2>&1)
if [ $? -eq 0 ]; then

    #deleting stack
    aws cloudformation delete-stack --stack-name $STACK_NAME


    #waiting stack to complete
    aws cloudformation wait stack-delete-complete --stack-name $STACK_NAME


     if [ $? -eq 0 ]; then
     echo "deleted succesfully"
     else
     echo "unsucessfull"
     fi

else
echo "this stack not exit"
fi