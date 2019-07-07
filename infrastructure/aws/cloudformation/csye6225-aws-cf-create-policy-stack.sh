#! /bin/bash


# check if the earlier code ran successfully.
if [[ $? -eq 0 ]]; then

    # reading AMI ID from the user
    echo -n "Enter policy stack name: "
    read APP_STACK_NAME

    echo -n "Enter S3 Code Deploy Bucket name: "
    read MyS3

    echo -n "Enter S3 Image Bucket name: "
    read ImageS3Bucket


    # create application resources stack
    aws cloudformation create-stack \
        --stack-name ${APP_STACK_NAME} \
        --template-body file://csye6225-cf-policy.json \
        --parameters ParameterKey=MyS3,ParameterValue=$MyS3 ParameterKey=ImageS3Bucket,ParameterValue=$ImageS3Bucket --capabilities CAPABILITY_NAMED_IAM

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
