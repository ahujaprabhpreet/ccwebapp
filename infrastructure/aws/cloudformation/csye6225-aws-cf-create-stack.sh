#! /bin/bash

STACK_NAME=$1

aws cloudformation create-stack --stack-name $STACK_NAME --template-body file:///home/puneet/Downloads/demo/src/main/resources/cloudformation.json

aws cloudformation wait stack-create-complete --stack-name $STACK_NAME

if [ $? -eq 0 ]; then
echo "stack created successfully"
else
echo "stack not created"
fi