# CSYE 6225 - Summer 2019

## Team Information

| Name | NEU ID | Email Address |
| --- | --- | --- |
| Nikunj Lad | 001422467 | lad.n@husky.neu.edu |
| Puneet Tanwar | 001409671 | tanwar.p@husky.neu.edu |
| Prabhpreet Ahuja | 001279030 | ahuja.pra@husky.neu.edu |

## Technology Stack

1. Framework : Spring Boot
2. Database : PostgreSQL
3. Version Control : Git

## Build Instructions

Pre-requisites: Postman, IDE
1. First, clone the repository:git@github.com:ahujaprabhpreet/ccwebapp.git with SSH Key in your local machine
2. Open the directory csye6225/dev/ccwebapp/webapp
3. Download the required maven dependencies by going to File > Maven > Re-import dependencies
4. Run the WebappApplication from your IDE running the  csye6225/dev/ccwebapp/webapp/src/main/java/com/neu/webapp/WebappApplication.java file

## Deploy Instructions

1. Register User: 
	curl -X POST \
	  http://localhost:8080/user/register \
	  -H 'Accept: */*' \
	  -H 'Cache-Control: no-cache' \
	  -H 'Connection: keep-alive' \
	  -H 'Content-Type: application/json' \
	  -H 'Host: localhost:8080' \
	  -H 'User-Agent: PostmanRuntime/7.13.0' \
	  -H 'accept-encoding: gzip, deflate' \
	  -H 'cache-control: no-cache' \
	  -H 'content-length: 67' \
	  -d '{
		"emailId" : "xyz@gmail.com",
		"password" : "abc@123!"
	}
  
2. Get Current Time:
	curl -X GET \
	  http://localhost:8080/ \
	  -H 'Accept: */*' \
	  -H 'Authorization: Basic cHBAcHAuY29tOk9uZXBsdXM2IQ==' \
	  -H 'Cache-Control: no-cache' \
	  -H 'Connection: keep-alive' \
	  -H 'Content-Type: application/json' \
	  -H 'Host: localhost:8080' \
	  -H 'User-Agent: PostmanRuntime/7.13.0' \
	  -H 'accept-encoding: gzip, deflate' \
	  -H 'cache-control: no-cache'
    
3. Register a Book:
	curl -X POST \
	  http://localhost:8080/book \
	  -H 'Accept: */*' \
	  -H 'Authorization: Basic cHBAcHAuY29tOk9uZXBsdXM2IQ==' \
	  -H 'Cache-Control: no-cache' \
	  -H 'Connection: keep-alive' \
	  -H 'Content-Type: application/json' \
	  -H 'Host: localhost:8080' \
	  -H 'User-Agent: PostmanRuntime/7.13.0' \
	  -H 'accept-encoding: gzip, deflate' \
	  -H 'cache-control: no-cache' \
	  -H 'content-length: 126' \
	  -d '{
		"title" : "History",
		"author" : "History Author",
		"isbn" : "453-8907654",
		"quantity" : "2"
	}'
  
4. Get a Book by ID:
	curl -X GET \
	  http://localhost:8080/book/306a3a27-7ce4-4a6f-bf4e-006ce8ffef13 \
	  -H 'Accept: */*' \
	  -H 'Authorization: Basic cHBAcHAuY29tOk9uZXBsdXM2IQ==' \
	  -H 'Cache-Control: no-cache' \
	  -H 'Connection: keep-alive' \
	  -H 'Content-Type: application/json' \
	  -H 'Host: localhost:8080' \
	  -H 'User-Agent: PostmanRuntime/7.13.0' \
	  -H 'accept-encoding: gzip, deflate' \
	  -H 'cache-control: no-cache'  

5. Delete a Book by ID:
	curl -X DELETE \
	  http://localhost:8080/book/306a3a27-7ce4-4a6f-bf4e-006ce8ffef13 \
	  -H 'Accept: */*' \
	  -H 'Authorization: Basic cHBAcHAuY29tOk9uZXBsdXM2IQ==' \
	  -H 'Cache-Control: no-cache' \
	  -H 'Connection: keep-alive' \
	  -H 'Content-Type: application/json' \
	  -H 'Host: localhost:8080' \
	  -H 'User-Agent: PostmanRuntime/7.13.0' \
	  -H 'accept-encoding: gzip, deflate' \
	  -H 'cache-control: no-cache' \
	  -H 'content-length: '
    
6. Update a Book:
	curl -X PUT \
	  http://localhost:8080/book \
	  -H 'Authorization: Basic cHBAcHAuY29tOk9uZXBsdXM2IQ==' \
	  -H 'Content-Type: application/json' \
	  -H 'cache-control: no-cache' \
	  -d '{
	    "id": "306a3a27-7ce4-4a6f-bf4e-006ce8ffef13",
	    "title": "History 2",
	    "author": "History 2 Author",
	    "isbn": "273-9876543",
	    "quantity": 100
	}'
  
7. Get all Books:
	curl -X GET \
	  http://localhost:8080/book \
	  -H 'Accept: */*' \
	  -H 'Authorization: Basic cHBAcHAuY29tOk9uZXBsdXM2IQ==' \
	  -H 'Cache-Control: no-cache' \
	  -H 'Connection: keep-alive' \
	  -H 'Content-Type: application/json' \
	  -H 'Host: localhost:8080' \
	  -H 'User-Agent: PostmanRuntime/7.13.0' \
	  -H 'accept-encoding: gzip, deflate' \
	  -H 'cache-control: no-cache'
    
 8. Add Image for a Book by BookID:
 curl -X POST \
	  http://localhost:8080/book/306a3a27-7ce4-4a6f-bf4e-006ce8ffef13/image \
	  -H 'Accept: */*' \
	  -H 'Authorization: Basic cHBAcHAuY29tOk9uZXBsdXM2IQ==' \
	  -H 'Cache-Control: no-cache' \
	  -H 'Connection: keep-alive' \
	  -H 'Content-Type: application/json' \
	  -H 'Host: localhost:8080' \
	  -H 'User-Agent: PostmanRuntime/7.13.0' \
	  -H 'accept-encoding: gzip, deflate' \
	  -H 'cache-control: no-cache' \
	  -H 'content-length: 126' \
	  -f '{
		"data" : "Screenshot.jpeg"
	}'
 
9. Get Image for a Book By BookID and ImageID 
 curl -X GET \
	  http://localhost:8080/book/306a3a27-7ce4-4a6f-bf4e-006ce8ffef13/image/4d659705-3550-4064-847f-eca7f3c44b4b \
	  -H 'Accept: */*' \
	  -H 'Authorization: Basic cHBAcHAuY29tOk9uZXBsdXM2IQ==' \
	  -H 'Cache-Control: no-cache' \
	  -H 'Connection: keep-alive' \
	  -H 'Content-Type: application/json' \
	  -H 'Host: localhost:8080' \
	  -H 'User-Agent: PostmanRuntime/7.13.0' \
	  -H 'accept-encoding: gzip, deflate' \
	  -H 'cache-control: no-cache'
    
10. Delete Image for a Book By BookID and ImageID     
curl -X DELETE \
	  http://localhost:8080/book/306a3a27-7ce4-4a6f-bf4e-006ce8ffef13/image/4d659705-3550-4064-847f-eca7f3c44b4b \
	  -H 'Accept: */*' \
	  -H 'Authorization: Basic cHBAcHAuY29tOk9uZXBsdXM2IQ==' \
	  -H 'Cache-Control: no-cache' \
	  -H 'Connection: keep-alive' \
	  -H 'Content-Type: application/json' \
	  -H 'Host: localhost:8080' \
	  -H 'User-Agent: PostmanRuntime/7.13.0' \
	  -H 'accept-encoding: gzip, deflate' \
	  -H 'cache-control: no-cache' \
	  -H 'content-length: '
    
11. Update Image for a Book By BookID and ImageID  
curl -X PUT \
	  http://localhost:8080/book/306a3a27-7ce4-4a6f-bf4e-006ce8ffef13/image/4d659705-3550-4064-847f-eca7f3c44b4b \
	  -H 'Authorization: Basic cHBAcHAuY29tOk9uZXBsdXM2IQ==' \
	  -H 'Content-Type: application/json' \
	  -H 'cache-control: no-cache' \
	  -f '{
	    "data" : "MyImage.jpeg"
	}'


## Running Tests
Frameworks used for Testing: Mockito, JUnit

To Run the test cases on the WebappApplication: 
1. Open webapp aplication in your IDE 
2. Right click on Webapp project and select 'Run All Tests'

## CI/CD


