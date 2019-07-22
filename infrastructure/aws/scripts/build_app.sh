#! /bin/bash

echo -n "Enter your github username: "
read USER_NAME

if [[ ${USER_NAME} == "" ]]; then
    echo "Please enter a your github  username!"
    exit 1
else
    echo "GitHub username : ${USER_NAME}"
fi

curl -u ${CIRCLECI_TOKEN} \
     -d '{"build_parameters": {"[CIRCLE_JOB]":"build"}}' \
     https://circleci.com/api/v1.1/project/github/${USER_NAME}/ccwebapp/tree/assignment6
