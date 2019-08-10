#! /bin/bash

# check if the earlier code ran successfully.
if [[ $? -eq 0 ]]; then

    echo -n "Enter Auto Scaling Stack name: "
    read AUTO_SCALING_STACK_NAME

    if [[ ${AUTO_SCALING_STACK_NAME} == "" ]]; then
        echo "Please enter an Application stack name!"
        exit 1
    else
        echo "Auto Scaling stack name : ${AUTO_SCALING_STACK_NAME}"
    fi

    echo -n "Enter Policy stack name: "
    read POLICY_NAME

    if [[ ${POLICY_NAME} == "" ]]; then
        echo "Please enter Policy stack name!"
        exit 1
    else
        echo "Policy stack name : ${POLICY_NAME}"
    fi

    echo -n "Enter Network stack name: "
    read NETWORK_STACK

    if [[ ${NETWORK_STACK} == "" ]]; then
        echo "Please enter Network stack name!"
        exit 1
    else
        echo "Network stack name : ${NETWORK_STACK}"
    fi

    echo -n "Enter DNS domain name: "
    read DNSDOMAIN

    if [[ ${DNSDOMAIN} == "" ]]; then
        echo "Please enter DNS Domain name!"
        exit 1
    else
        echo "DNS Domain name : ${DNSDOMAIN}"
    fi

#    AWSID=$(aws iam get-user | jq '.["User"]["UserId"]')
    AMI_ID=$(aws ec2 describe-images --filters "Name=tag:Name,Values=csye6225-Cloud" --query 'Images[0].{ID:ImageId}' --output text)
    CODEDEPLOYS3BUCKET=$(aws s3api list-buckets --query "Buckets[].Name" | jq '.[]' | grep 'code-deploy')
    S3_IMAGE_BUCKET=$(aws s3api list-buckets --query "Buckets[].Name" | jq '.[]' | grep '.com')
    CERTIFICATE=$(aws acm list-certificates | jq '.["CertificateSummaryList"][0]["CertificateArn"]')

    # create application resources stack
    aws cloudformation create-stack \
        --stack-name ${AUTO_SCALING_STACK_NAME} \
        --template-body file://csye6225-cf-auto-scaling-application.json \
        --parameters ParameterKey=AMIID,ParameterValue=${AMI_ID} \
        ParameterKey=NETWORK,ParameterValue=${NETWORK_STACK} \
        ParameterKey=POLICY,ParameterValue=${POLICY_NAME} \
        ParameterKey=CODEDEPLOYS3BUCKET,ParameterValue=${CODEDEPLOYS3BUCKET} \
        ParameterKey=IMAGES3BUCKET,ParameterValue=${S3_IMAGE_BUCKET} \
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
