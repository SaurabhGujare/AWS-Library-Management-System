#! /bin/bash
STACK_NAME=$1
AWSREGION1=$2
AWSREGION2=$3
AWSREGION3=$4
VPC_NAME=${STACK_NAME}-csye6225-vpc
SUBNET1_NAME=${STACK_NAME}-csye6225-subnet1
SUBNET2_NAME=${STACK_NAME}-csye6225-subnet2
SUBNET3_NAME=${STACK_NAME}-csye6225-subnet3
IGNAME=${STACK_NAME}-csye6225-InternetGateway
PUBLIC_ROUTE_TABLE=${STACK_NAME}-csye6225-public-route-table

aws cloudformation create-stack --stack-name $STACK_NAME --template-body file://csye6225-cf-networking.json --parameters ParameterKey=VPCName,ParameterValue=$VPC_NAME ParameterKey=SubnetName1,ParameterValue=$SUBNET1_NAME ParameterKey=SubnetName2,ParameterValue=$SUBNET2_NAME ParameterKey=SubnetName3,ParameterValue=$SUBNET3_NAME ParameterKey=PublicRouteTableName,ParameterValue=$PUBLIC_ROUTE_TABLE ParameterKey=AWSREGION1,ParameterValue=$AWSREGION1 ParameterKey=AWSREGION2,ParameterValue=$AWSREGION2 ParameterKey=AWSREGION3,ParameterValue=$AWSREGION3 ParameterKey=IGName,ParameterValue=$IGNAME

aws cloudformation wait stack-create-complete --stack-name $STACK_NAME

if [ $? -ne "0" ]
then
	echo "Creation of Stack failed"
else
	echo "Creation of Stack Success"
fi
