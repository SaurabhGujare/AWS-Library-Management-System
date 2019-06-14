# CSYE 6225 - Summer 2019

## Team Information

| Name | NEU ID | Email Address |
| Naqiyah Lakdawala	 | 001449938	 | lakdawala.n@husky.neu.edu |
| Saurabh Gujare         | 001424874     | gujare.s@husky.neu.edu |
| Gaurao Thakur		 | 001417955     | thakur.ga@husky.neu.edu |


## Technology Stack
1. J2EE
2. Spring Boot
3. MySQL

## Build Instructions
1. Import Spring Boot project from start.spring.io with appropriate dependencies
2. Developing the source code for Rest based API endpoints.
3. Mention persistant database environment (like MySQL) based details in applications.properties file

## Deploy Instructions
1. Download the csyeassign1 folder and unzip it.
2. Create a MySQL database with name csyeassign1
3. Open the project in Intellij
4. Run the CloudAssign1Application file as a Java Application
5. To run the bash files for cloudformation run  for creation of a stack : "./ csye6225-aws-cf-create-stack.sh <Stack-      name> <Available-Zone-1> <Available-Zone-2 <Available-Zone-3>
6. For Termination "./ csye6225-aws-cf-terminate-stack.sh <Stack-name>" in the cloudformation folder.
7. To run the aws-cli scripts For creation of VPC run "./ csye6225-aws-networking-setup.sh <CIDR>  <aws-region-code> <subnet-1-name> <subnet-2-name> <subnet-3-name> <Available-Zone-1> <Available-Zone-2> <Available-Zone-3>"
8. To run the aws-cli scripts for TearDown of VPC run "./ csye6225-aws-networking-taerdown.sh {VPC-ID}"

## Running Tests
1. testShowBook() function returns all the books
2. testCreateBook() fucntion tests the creattion of the particular book

## CI/CD
