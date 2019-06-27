#! /bin/bash

# asking user for stack name to be deleted
echo -n "Please enter Application stack name to delete: "
read STACK_NAME

#checking stack exit or not
statuscheck=$(aws cloudformation describe-stacks \
    --stack-name $STACK_NAME \
    --query "Stacks[0].StackStatus" >/dev/null 2>&1)

if [[ $? -eq 0 ]]; then

    # deleting stack
    aws cloudformation delete-stack --stack-name $STACK_NAME
    if [[ $? -eq 0 ]]; then
        echo "VPC stack deletion in progress..."
    else
        echo "VPC stack deletion terminated due to errors!"
        exit 1
    fi

    # waiting stack to complete
    aws cloudformation wait stack-delete-complete --stack-name $STACK_NAME
    if [[ $? -eq 0 ]]; then
        echo "VPC Stack deleted successfully"
    else
        echo "VPC stack deletion unsuccessfull!"
    fi

else
    echo "This stack does not exist!"
fi