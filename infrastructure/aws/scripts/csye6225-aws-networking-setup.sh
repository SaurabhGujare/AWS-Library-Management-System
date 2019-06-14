# !/bin/bash

CIDR=$1
AVAILABILITY_REGION=$2
SUBNET1_CIDR=$3
SUBNET2_CIDR=$4
SUBNET3_CIDR=$5
AVAIL_ZONE1=$6
AVAIL_ZONE2=$7
AVAIL_ZONE3=$8

#VALIDATE PASSED PARAMETER
if [ -z "$1" ]
then
	echo "Please ender CIDR"
	exit 1
else
	if [ -z "$2" ]
	then
		echo "Please enter availability region"
		exit 1
	else
		if [ -z "$3" ] && [ -z "$4" ] && [ -z "$5" ] && [ -z "$6" ] && [ -z "$7" ] && [ -z "$8" ]
		then
			echo "Please ender all thr Subnet CIDR"
			exit 1
		fi
	fi
fi

#VPC CREATION
VPCID=$(aws ec2 create-vpc --cidr-block "$CIDR" --query "Vpc.VpcId" --output text) 
if [ $? -eq 0 ]
then 
	echo "VPC Created, VPC ID : "$VPCID
	aws ec2 create-tags --resources "$VPCID" --tags Key=Name,Value="VPC:"$VPCID
else
	echo "****ERROR: Error while VPC creation"
	exit 1
fi

#ADDING AVAILABILITY ZONES

	ZONE1=$AVAIL_ZONE1
	ZONE2=$AVAIL_ZONE2
	ZONE3=$AVAIL_ZONE3


#SUBNET CREATION
echo "Creating subnet for VPD with ID "$VPCID

SUBNETID1=$(aws ec2 create-subnet --vpc-id "$VPCID" --cidr-block "$SUBNET1_CIDR" --availability-zone "$ZONE1" --query "Subnet.SubnetId" --output text)
if [ $? -eq 0 ]
then
	echo "Subnet 1 created  "$SUBNETID1
	aws ec2 create-tags --resources "$SUBNETID1" --tags Key=Name,Value="Subnet:"$SUBNETID1
else
	echo "****ERROR: Failed to create subnet2"
	exit 1
fi

SUBNETID2=$(aws ec2 create-subnet --vpc-id "$VPCID" --cidr-block "$SUBNET2_CIDR" --availability-zone "$ZONE2" --query "Subnet.SubnetId" --output text)
if [ $? -eq 0 ]
then
	echo "Subnet 2 created "$SUBNETID2
	aws ec2 create-tags --resources "$SUBNETID2" --tags Key=Name,Value="Subnet:"$SUBNETID2
else
	echo "****ERROR: Failed to create subnet2"
	exit 1
fi

SUBNETID3=$(aws ec2 create-subnet --vpc-id "$VPCID" --cidr-block "$SUBNET3_CIDR" --availability-zone "$ZONE3" --query "Subnet.SubnetId" --output text)
if [ $? -eq 0 ]
then
	echo "Subnet 3 created  "$SUBNETID3
	aws ec2 create-tags --resources "$SUBNETID3" --tags Key=Name,Value="Subnet:"$SUBNETID3
else
	echo "****ERROR: Failed to create subnet3"
	exit 1
fi
 
#INTERNET GATEWAY CREATION
GATEWAY_ID=$(aws ec2 create-internet-gateway --query "InternetGateway.InternetGatewayId" --output text)
if [ $? -eq 0 ]
then
	echo "Intenet Gateway created successfully with ID "$GATEWAY_ID
	aws ec2 create-tags --resources "$GATEWAY_ID" --tags Key=Name,Value="IG:"$GATEWAY_ID
else
	echo "****ERROR: Failed to create Internet Gateway"
	exit 1
fi

#ATTACHING IG to VPC
GATEWAY_ATTACH_REPONSE=$(aws ec2 attach-internet-gateway --internet-gateway-id "$GATEWAY_ID" --vpc-id "$VPCID" )
if [ $? -eq 0 ]
then
	echo "Intenet Gateway $GATEWAY_ID attached successfully to VPC with ID "$VPCID
else
	echo "****ERROR: Failed to attached Internet Gateway to VPC"
	exit 1
fi

#ROUTE TABLE CREATION
ROUTINGTABLE_ID=$(aws ec2 create-route-table --vpc-id "$VPCID" --query "RouteTable.RouteTableId" --output text)
if [ $? -eq 0 ]
then
	echo "Routing table with ID $ROUTINGTABLE_ID created successfully"
	aws ec2 create-tags --resources "$ROUTINGTABLE_ID" --tags Key=Name,Value="IG:"$ROUTINGTABLE_ID
else
	echo "****ERROR: Failed to create ROUTE TABLE"
	exit 1
fi

#ASSOCIATING SUBNET TO ROUTE TABLE
ASSOCIATION_ID1=$(aws ec2 associate-route-table --route-table-id "$ROUTINGTABLE_ID" --subnet-id "$SUBNETID1")
ASSOCIATION_ID2=$(aws ec2 associate-route-table --route-table-id "$ROUTINGTABLE_ID" --subnet-id "$SUBNETID2")
ASSOCIATION_ID3=$(aws ec2 associate-route-table --route-table-id "$ROUTINGTABLE_ID" --subnet-id "$SUBNETID3")
if [ $? -eq 0 ]
then
	echo "Subnets with Ids $SUBNETID1 $SUBNETID2 $SUBNETID3 attached to routing table with Id "$ROUTINGTABLE_ID
else
	echo "Subnet Association to Route table failed"
	exit 1
fi

#ADD ROUTE TO ROUTE TABLE
ADDINGROUTE=$(aws ec2 create-route --route-table-id "$ROUTINGTABLE_ID" --destination-cidr-block 0.0.0.0/0 --gateway-id "$GATEWAY_ID")
if [ $? -eq 0 ]
then
	echo "Route 0.0.0.0 added Successfully to routing table "$ROUTINGTABLE_ID
else	
	echo "****ERROR: Failed to add Route"	
	exit 1
fi

















