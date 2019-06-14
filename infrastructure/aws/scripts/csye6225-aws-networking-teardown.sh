#!/bin/bash

vpcid=$1

subnet_reponse=$(aws ec2 describe-subnets --filters Name=vpc-id,Values="${vpcid}" --output json)
subnet1=$(echo -e "$subnet_reponse" | jq '.Subnets[0].SubnetId' | tr -d '""')
subnet2=$(echo -e "$subnet_reponse" | jq '.Subnets[1].SubnetId' | tr -d '""')
subnet3=$(echo -e "$subnet_reponse" | jq '.Subnets[2].SubnetId' | tr -d '""')

routing_table_id=$(aws ec2 describe-route-tables --filter Name="association.subnet-id",Values="$subnet1" \
 --query "RouteTables[0].RouteTableId" --output text)

aws ec2 delete-subnet --subnet-id "$subnet1"
if [ $? -eq 0 ]
then
    echo "Subnet 1 deleted, ID : "$subnet1
else
    echo "Error while deleting Subnet 1" 
    exit 1
fi

aws ec2 delete-subnet --subnet-id "$subnet2"
if [ $? -eq 0 ]
then
    echo "Subnet 2 deleted, ID : "$subnet2
else
    echo "Error while deleting Subnet 2" 
    exit 1
fi

aws ec2 delete-subnet --subnet-id "$subnet3"
if [ $? -eq 0 ]
then
    echo "Subnet 3 deleted, ID : "$subnet3
else
    echo "Error while deleting Subnet 3" 
    exit 1
fi

aws ec2 delete-route-table --route-table-id "$routing_table_id"
if [ $? -eq 0 ]
then
    echo "Routing table deleted, ID : "$routing_table_id
else
    echo "Error while deleting routing table" 
    exit 1
fi

internet_gateway_Id=$(aws ec2 describe-internet-gateways --filters Name="attachment.vpc-id",\
Values="$vpcid" --query "InternetGateways[0].InternetGatewayId" --output text)

aws ec2 detach-internet-gateway --internet-gateway-id "$internet_gateway_Id" --vpc-id "$vpcid" 

aws ec2 delete-internet-gateway --internet-gateway-id "$internet_gateway_Id"
if [ $? -eq 0 ]
then
    echo "Internet Gateway deleted, ID : "$internet_gateway_Id
else
    echo "Error while deleting Internet Gateway" 
    exit 1
fi

aws ec2  delete-vpc --vpc-id "$vpcid"
if [ $? -eq 0 ]
then
    echo "VPC deleted, ID : "$vpcid
else
    echo "Error while VPC" 
    exit 1
fi

echo "Stack delete successfully"