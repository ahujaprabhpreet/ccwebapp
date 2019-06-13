#! /bin/bash

echo "plz enter stack name"
read STACK_NAME

aws cloudformation delete-stack --stack-name $STACK_NAME

aws cloudformation wait stack-delete-complete --stack-name $STACK_NAME

if [ $? -eq 0 ]; then
echo "deleted succesfully"
else
echo "unsucessfull"
fi

