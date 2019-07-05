#! /bin/bash

#./csye6225-aws-cf-create-stack.sh
#./csye6225-aws-cf-create-policy-stack.sh

# check if the earlier code ran successfully.
if [[ $? -eq 0 ]]; then

    # reading AMI ID from the user
    echo -n "Enter AMI-ID: "
    read AMIID

    echo -n "Enter application stack name: "
    read APP_STACK_NAME

    echo -n "Enter Network stack name: "
    read NETWORK

    echo -n "Enter policy stack name: "
    read policy

    echo -n "Enter image s3bucket  name: "
    read s3Bucket

    # create application resources stack
    aws cloudformation create-stack \
        --stack-name ${APP_STACK_NAME} \
        --template-body file://csye6225-cf-application.json \
        --parameters ParameterKey=AMIID,ParameterValue=$AMIID \
        ParameterKey=NETWORK,ParameterValue=$NETWORK \
          ParameterKey=policy,ParameterValue=$policy \
          ParameterKey=s3Bucket,ParameterValue=$s3Bucket --capabilities CAPABILITY_NAMED_IAM

    # check if the
    if [[ $? -ne 0 ]]; then
        echo "Application Resource Stack creation not completed"
        exit 1
    fi
    echo "Application Resource Stack creation in progress..."

    # waiting stack to create
    aws cloudformation wait stack-create-complete --stack-name $APP_STACK_NAME
    if [[ $? -eq 0 ]]; then
        echo "Application Resource Stack created successfully"
    else
        echo "Stack not created"
    fi
fi
