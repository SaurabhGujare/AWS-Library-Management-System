## Getting Started

Clone the repository on your local machine

### Prerequisites

Install jmeter binary from [Apache Jmeter](http://jmeter.apache.org/)
Execute below commad in terminal 
```
bash on bin/jmeter.sh
```

### Execution steps
1. Load AutoScaleLoadTest.jmx in jmeter 
2. Change the server Id in all the thread groups to your domain name
3. Change the csv directory path in CSV Data Set Config to your local directory path
4. Change the image path in "View Books" Thread Group
4. Run the test plan which will run all the thread groups consecutively