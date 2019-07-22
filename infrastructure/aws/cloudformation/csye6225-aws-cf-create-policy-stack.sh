#! /bin/bash


# check if the earlier code ran successfully.
if [[ $? -eq 0 ]]; then

    # reading AMI ID from the user
    echo -n "Enter policy stack name: "
    read APP_STACK_NAME

    if [[ ${APP_STACK_NAME} == "" ]]; then
        echo "Please enter an Application stack name!"
        exit 1
    else
        echo "Application Stack name : ${APP_STACK_NAME}"
    fi

    echo -n "Enter S3 Code Deploy Bucket name: "
    read S3_CODE_DEPLOY_BUCKET

    if [[ ${S3_CODE_DEPLOY_BUCKET} == "" ]]; then
        echo "Please enter an S3 CodeDeploy bucket name!"
        exit 1
    else
        echo "S3 CodeDeploy Bucket name : ${S3_CODE_DEPLOY_BUCKET}"
    fi

    echo -n "Enter S3 Image Bucket name: "
    read S3_IMAGE_BUCKET

    if [[ ${S3_IMAGE_BUCKET} == "" ]]; then
        echo "Please enter an S3 Image bucket name!"
        exit 1
    else
        echo "S3 Image Bucket name : ${S3_IMAGE_BUCKET}"
    fi

    # create application resources stack
    aws cloudformation create-stack \
        --stack-name ${APP_STACK_NAME} \
        --template-body file://csye6225-cf-policy.json \
        --parameters ParameterKey=MyS3,ParameterValue=${S3_CODE_DEPLOY_BUCKET} ParameterKey=ImageS3Bucket,ParameterValue=${S3_IMAGE_BUCKET} \
        --capabilities CAPABILITY_NAMED_IAM

    # check if the
    if [[ $? -ne 0 ]]; then
        echo "Policy  Stack creation not completed"
        exit 1
    fi
    echo "Policy Resource Stack creation in progress..."

    # waiting stack to create
    aws cloudformation wait stack-create-complete --stack-name ${APP_STACK_NAME}
    if [[ $? -eq 0 ]]; then
        echo "Policy Resource Stack created successfully"
    else
        echo "Stack not created"
    fi
fi
