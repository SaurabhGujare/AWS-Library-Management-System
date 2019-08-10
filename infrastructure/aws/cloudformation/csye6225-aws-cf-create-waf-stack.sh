#!/bin/bash
read -p 'Enter stack name: ' STACK_NAME
load_balancer=$(aws elbv2 describe-load-balancers --query "LoadBalancers[0].LoadBalancerArn" --output text)


echo "Creating stack.."

STACK_ID=$(\aws cloudformation create-stack --stack-name ${STACK_NAME} \
	--template-body file://csye6225-aws-cf-waf.json\
	--parameters ParameterKey=stackName,ParameterValue=${STACK_NAME} ParameterKey=loadBalancer,ParameterValue=${load_balancer} \
	--capabilities CAPABILITY_IAM \
	--capabilities CAPABILITY_NAMED_IAM \
 	| jq -r .StackId \
)
	
	
#Job Done!
echo "Waiting on ${STACK_ID} create completion.."
aws cloudformation wait stack-create-complete --stack-name ${STACK_ID}
if [ $? -ne 0 ]; then
	echo "Application Stack creation failed!"
    exit 1
else
    echo "EC2 Instance, RDS, security groups, DynamoDB Table and S3 Bucket created!"
fi
