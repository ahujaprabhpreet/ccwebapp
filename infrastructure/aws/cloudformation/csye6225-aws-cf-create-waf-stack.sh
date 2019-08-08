#!/bin/bash

echo "Starting with Stack creation process......"
echo "Validating the Template......"

aws cloudformation validate-template --template-body file://csye6225-cf-waf.json #>/dev/null 2>&1

if [[ $? -eq 0 ]]
then
    echo -n "Enter Web Application Firewall (WAF) stack name: "
    read WAF_STACK_NAME

    if [[ ${WAF_STACK_NAME} == "" ]]; then
        echo "Please enter an Web Application Firewall (WAF) stack name!"
        exit 1
    else
        echo "Auto Scaling stack name : ${WAF_STACK_NAME}"
    fi

    aws cloudformation create-stack --stack-name ${WAF_STACK_NAME} \
        --template-body file://csye6225-cf-waf.json \
        --capabilities CAPABILITY_NAMED_IAM

     # check if the stack creation was successful
    if [[ $? -ne 0 ]]; then
        echo "Web Application Firewall (WAF)  Stack creation not completed"
        exit 1
    fi
    echo "Web Application Firewall (WAF) Stack creation in progress..."

    # waiting stack to create
    aws cloudformation wait stack-create-complete --stack-name ${WAF_STACK_NAME}
    if [[ $? -eq 0 ]]; then
        echo "Web Application Firewall (WAF) Stack created successfully"
    else
        echo "Web Application Firewall (WAF) Stack not created"
    fi
else
    echo "Unable to validate template"
fi