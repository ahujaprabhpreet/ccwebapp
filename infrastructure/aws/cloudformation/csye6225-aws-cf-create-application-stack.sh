#! /bin/bash

# check if the earlier code ran successfully.
if [[ $? -eq 0 ]]; then

    # reading AMI ID from the user
    echo -n "Enter AMI-ID: "
    read AMI_ID

    if [[ ${AMI_ID} == "" ]]; then
        echo "Please enter AMI ID!"
        exit 1
    else
        echo "AMI ID : ${AMI_ID}"
    fi

    echo -n "Enter application stack name: "
    read APP_STACK_NAME

    if [[ ${APP_STACK_NAME} == "" ]]; then
        echo "Please enter an Application stack name!"
        exit 1
    else
        echo "Application stack name : ${APP_STACK_NAME}"
    fi

    echo -n "Enter Network stack name: "
    read NETWORK_STACK

    if [[ ${NETWORK_STACK} == "" ]]; then
        echo "Please enter Network stack name!"
        exit 1
    else
        echo "Network stack name : ${NETWORK_STACK}"
    fi

    echo -n "Enter policy stack name: "
    read POLICY_NAME

    if [[ ${POLICY_NAME} == "" ]]; then
        echo "Please enter Policy stack name!"
        exit 1
    else
        echo "Policy stack name : ${POLICY_NAME}"
    fi

    echo -n "Enter image S3 bucket name: "
    read S3_IMAGE_BUCKET

    if [[ ${S3_IMAGE_BUCKET} == "" ]]; then
        echo "Please enter S3 Image bucket name!"
        exit 1
    else
        echo "S3 Image bucket name : ${S3_IMAGE_BUCKET}"
    fi

    # create application resources stack
    aws cloudformation create-stack \
        --stack-name ${APP_STACK_NAME} \
        --template-body file://csye6225-cf-application.json \
        --parameters ParameterKey=AMIID,ParameterValue=${AMI_ID} \
        ParameterKey=NETWORK,ParameterValue=${NETWORK_STACK} \
        ParameterKey=policy,ParameterValue=${POLICY_NAME} \
        ParameterKey=s3Bucket,ParameterValue=${S3_IMAGE_BUCKET} \
        --capabilities CAPABILITY_NAMED_IAM

    # check if the
    if [[ $? -ne 0 ]]; then
        echo "Application Resource Stack creation not completed"
        exit 1
    fi
    echo "Application Resource Stack creation in progress..."

    # waiting stack to create
    aws cloudformation wait stack-create-complete --stack-name ${APP_STACK_NAME}
    if [[ $? -eq 0 ]]; then
        echo "Application Resource Stack created successfully"
    else
        echo "Stack not created"
    fi
fi
