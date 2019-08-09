#! /bin/bash

VAL=$(aws ec2 describe-images --owners 325281477233 --query 'Images[*].{ID:ImageId}')
echo ${VAL[0]['ID']}
