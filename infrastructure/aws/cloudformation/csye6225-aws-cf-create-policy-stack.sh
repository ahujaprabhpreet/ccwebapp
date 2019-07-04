#! /bin/bash

# create the VPC from here
# ./csye6225-aws-cf-create-stack.sh

# check if the earlier code ran successfully.
if [[ $? -eq 0 ]]; then

    # reading AMI ID from the user
    echo -n "Enter application stack name: "
    read APP_STACK_NAME

    echo -n "Enter User: "
    read MyUser

    echo -n "Enter s3 name: "
    read MyS3

    echo -n "Enter AWSACCOUNTID: "
    read AWSACCOUNTID

    # create application resources stack
    aws cloudformation create-stack \
        --stack-name ${APP_STACK_NAME} \
        --template-body file://csye6225-cf-policy.json \
        --parameters ParameterKey=MyUser,ParameterValue=$MyUser \
          ParameterKey=MyS3,ParameterValue=$MyS3 \
          ParameterKey=AWSACCOUNTID,ParameterValue=$AWSACCOUNTID --capabilities CAPABILITY_NAMED_IAM

    # check if the
    if [[ $? -ne 0 ]]; then
        echo "policy  Stack creation not completed"
        exit 1
    fi
    echo "policy Resource Stack creation in progress..."

    # waiting stack to create
    aws cloudformation wait stack-create-complete --stack-name $APP_STACK_NAME
    if [[ $? -eq 0 ]]; then
        echo "policy Resource Stack created successfully"
    else
        echo "Stack not created"
    fi
fi
