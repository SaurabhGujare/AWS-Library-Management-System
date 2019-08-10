STACK_NAME=$1
read -p 'Enter your EC2 key pair name ' KEY_PAIR

read -p 'Enter your NUID ID (for ex. If NUID  is doe.j@husky.neu.edu then enter doej) ' BUCKET_NAME

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
export DOMAIN_NAME = csye6225-su19-thakurga.me
echo "DOMAIN NAME: ${DOMAIN_NAME}"
export certificate_arn=$(aws acm list-certificates --query "CertificateSummaryList[0].CertificateArn" --output text)
echo "CERTIFICATE: ${certificate_arn}"

aws cloudformation create-stack --stack-name $STACK_NAME --template-body file://csye6225-cf-auto-scaling-application.json --parameters ParameterKey=VpcId,ParameterValue=$vpcId ParameterKey=EC2Name,ParameterValue=$EC2_Name ParameterKey=SubnetId1,ParameterValue=$subnetId1 ParameterKey=SubnetId2,ParameterValue=$subnetId2 ParameterKey=SubnetId3,ParameterValue=$subnetId3 ParameterKey=AMI,ParameterValue=$AMI ParameterKey=keyName,ParameterValue=$KEY_PAIR ParameterKey=BucketName,ParameterValue=$BUCKET_NAME ParameterKey=CertificateArn,ParameterValue=$certificate_arn ParameterKey=HostedZoneName,ParameterValue=$DOMAIN_NAME --capabilities CAPABILITY_IAM --capabilities CAPABILITY_NAMED_IAM


export STACK_STATUS=$(aws cloudformation describe-stacks --stack-name $STACK_NAME --query "Stacks[][ [StackStatus ] ][]" --output text)

while [ $STACK_STATUS != "CREATE_COMPLETE" ]
do
	STACK_STATUS=`aws cloudformation describe-stacks --stack-name $STACK_NAME --query "Stacks[][ [StackStatus ] ][]" --output text`
done
echo "Created Stack ${STACK_NAME} successfully!"
