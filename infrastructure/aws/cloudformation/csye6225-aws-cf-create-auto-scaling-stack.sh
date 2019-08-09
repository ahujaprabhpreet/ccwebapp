#! /bin/bash

# check if the earlier code ran successfully.
if [[ $? -eq 0 ]]; then

#    echo -n "Enter auto scaling stack name: "
#    read AUTO_SCALING_STACK_NAME
#
#    if [[ ${AUTO_SCALING_STACK_NAME} == "" ]]; then
#        echo "Please enter an Application stack name!"
#        exit 1
#    else
#        echo "Auto Scaling stack name : ${AUTO_SCALING_STACK_NAME}"
#    fi
#
#    echo -n "Enter policy stack name: "
#    read POLICY_NAME
#
#    if [[ ${POLICY_NAME} == "" ]]; then
#        echo "Please enter Policy stack name!"
#        exit 1
#    else
#        echo "Policy stack name : ${POLICY_NAME}"
#    fi
#
#    echo -n "Enter Network stack name: "
#    read NETWORK_STACK
#
#    if [[ ${NETWORK_STACK} == "" ]]; then
#        echo "Please enter Network stack name!"
#        exit 1
#    else
#        echo "Network stack name : ${NETWORK_STACK}"
#    fi
#
#    echo -n "Enter subnet1 name: "
#    read SUBNET1
#
#    if [[ ${SUBNET1} == "" ]]; then
#        echo "Please enter Subnet1 name!"
#        exit 1
#    else
#        echo "Subnet1 name : ${SUBNET1}"
#    fi
#
#    echo -n "Enter subnet2 name: "
#    read SUBNET2
#
#    if [[ ${SUBNET2} == "" ]]; then
#        echo "Please enter Subnet2 name!"
#        exit 1
#    else
#        echo "Subnet2 name : ${SUBNET2}"
#    fi
#
#    echo -n "Enter subnet3 name: "
#    read SUBNET3
#
#    if [[ ${SUBNET3} == "" ]]; then
#        echo "Please enter Subnet3 name!"
#        exit 1
#    else
#        echo "Subnet3 name : ${SUBNET3}"
#    fi
#
#    # reading AMI ID from the user
#    echo -n "Enter AMI-ID: "
#    read AMI_ID
#
#    if [[ ${AMI_ID} == "" ]]; then
#        echo "Please enter AMI ID!"
#        exit 1
#    else
#        echo "AMI ID : ${AMI_ID}"
#    fi
#
#    echo -n "Enter Code Deploy Bucket name: "
#    read CODEDEPLOYS3BUCKET
#
#    if [[ ${CODEDEPLOYS3BUCKET} == "" ]]; then
#        echo "Please enter Enter Code Deploy Bucket name!"
#        exit 1
#    else
#        echo "Code Deploy Bucket name : ${CODEDEPLOYS3BUCKET}"
#    fi
#
#    echo -n "Enter image S3 bucket name: "
#    read S3_IMAGE_BUCKET
#
#    if [[ ${S3_IMAGE_BUCKET} == "" ]]; then
#        echo "Please enter S3 Image bucket name!"
#        exit 1
#    else
#        echo "S3 Image bucket name : ${S3_IMAGE_BUCKET}"
#    fi
#
#    echo -n "Enter certificate name: "
#    read CERTIFICATE
#
#    if [[ ${CERTIFICATE} == "" ]]; then
#        echo "Please enter Certificate name!"
#        exit 1
#    else
#        echo "Certificate name : ${CERTIFICATE}"
#    fi
#
#    echo -n "Enter DNS domain name: "
#    read DNSDOMAIN
#
#    if [[ ${DNSDOMAIN} == "" ]]; then
#        echo "Please enter DNS Domain name!"
#        exit 1
#    else
#        echo "DNS Domain name : ${DNSDOMAIN}"
#    fi

    AUTO_SCALING_STACK_NAME="Auto"
    AMI_ID="ami-0a2c6962662087a36"
    NETWORK_STACK="Cloud"
    POLICY_NAME="Policy"
    CODEDEPLOYS3BUCKET="code-deploy.csye6225-su19-ladn.me"
    S3_IMAGE_BUCKET="csye6225-su19-ladn.me.csye6225.com"
    SUBNET1="Shubham"
    SUBNET2="Mansi"
    SUBNET3="Nikunj"
    CERTIFICATE="arn:aws:acm:us-east-1:325281477233:certificate/96141c4c-75fe-45c3-8e80-46230372c906"
    DNSDOMAIN="csye6225-su19-ladn.me."

    # create application resources stack
    aws cloudformation create-stack \
        --stack-name ${AUTO_SCALING_STACK_NAME} \
        --template-body file://csye6225-cf-auto-scaling-application.json \
        --parameters ParameterKey=AMIID,ParameterValue=${AMI_ID} \
        ParameterKey=NETWORK,ParameterValue=${NETWORK_STACK} \
        ParameterKey=POLICY,ParameterValue=${POLICY_NAME} \
        ParameterKey=CODEDEPLOYS3BUCKET,ParameterValue=${CODEDEPLOYS3BUCKET} \
        ParameterKey=IMAGES3BUCKET,ParameterValue=${S3_IMAGE_BUCKET} \
        ParameterKey=SUBNET1,ParameterValue=${SUBNET1} \
        ParameterKey=SUBNET2,ParameterValue=${SUBNET2} \
        ParameterKey=SUBNET3,ParameterValue=${SUBNET3} \
        ParameterKey=CERTIFICATE,ParameterValue=${CERTIFICATE} \
        ParameterKey=DNSDOMAIN,ParameterValue=${DNSDOMAIN} \
        --capabilities CAPABILITY_NAMED_IAM

    # check if the
    if [[ $? -ne 0 ]]; then
        echo "Auto Scaling Resource Stack creation not completed"
        exit 1
    fi
    echo "Auto Scaling Stack creation in progress..."

    # waiting stack to create
    aws cloudformation wait stack-create-complete --stack-name ${AUTO_SCALING_STACK_NAME}
    if [[ $? -eq 0 ]]; then
        echo "Auto Scaling Resource Stack created successfully"
    else
        echo "Auto Scaling Stack not created"
    fi
fi
