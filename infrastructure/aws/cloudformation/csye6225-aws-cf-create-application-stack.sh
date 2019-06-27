STACK_NAME=$1
keyName=$2
EC2_NAME="${STACK_NAME}-csye6225-ec2"
echo "VPC_NAME : ${VPC_NAME}"
echo "EC2_NAME : ${EC2_NAME}"
export vpcId=$(aws ec2 describe-vpcs --filters "Name=tag-key,Values=Name" --query "Vpcs[*].[CidrBlock, VpcId][-1]" --output text|grep 10.0.0.0/16|awk '{print $2}')
echo "vpcId : $vpcId"
export subnetId1=$(aws ec2 describe-subnets --filters "Name=vpc-id,Values=$vpcId" --query 'Subnets[*].[SubnetId, VpcId, AvailabilityZone, CidrBlock]' --output text|grep 10.0.1.0/24|grep us-east-1a|awk '{print $1}')
echo "subnetid : ${subnetId1}"
export subnetId2=$(aws ec2 describe-subnets --filters "Name=vpc-id,Values=$vpcId" --query 'Subnets[*].[SubnetId, VpcId, AvailabilityZone, CidrBlock]' --output text|grep 10.0.2.0/24|grep us-east-1b|awk '{print $1}')
echo "subnetid2 : ${subnetId2}"
export subnetId3=$(aws ec2 describe-subnets --filters "Name=vpc-id,Values=$vpcId" --query 'Subnets[*].[SubnetId, VpcId, AvailabilityZone, CidrBlock]' --output text|grep 10.0.3.0/24|grep us-east-1c|awk '{print $1}')
echo "subnetid3 : ${subnetId3}"
export AMI=$(aws ec2 describe-images --filters "Name=name,Values=*csye6225*" --query 'sort_by(Images, &CreationDate)[-1].ImageId' --output text)
echo "AMI: ${AMI}" 

aws cloudformation create-stack --stack-name $STACK_NAME --template-body file://csye6225-cf-application.json --parameters ParameterKey=VpcId,ParameterValue=$vpcId ParameterKey=EC2Name,ParameterValue=$EC2_Name ParameterKey=SubnetId1,ParameterValue=$subnetId1 ParameterKey=SubnetId2,ParameterValue=$subnetId2 ParameterKey=SubnetId3,ParameterValue=$subnetId3 ParameterKey=AMI,ParameterValue=$AMI ParameterKey=keyName,ParameterValue=$keyName

export STACK_STATUS=$(aws cloudformation describe-stacks --stack-name $STACK_NAME --query "Stacks[][ [StackStatus ] ][]" --output text)

while [ $STACK_STATUS != "CREATE_COMPLETE" ]
do
	STACK_STATUS=`aws cloudformation describe-stacks --stack-name $STACK_NAME --query "Stacks[][ [StackStatus ] ][]" --output text`
done
echo "Created Stack ${STACK_NAME} successfully!"
