# Getting Started
Java version 17

To run the application write the commands below
cd be-transfer-app
mvn clean install
java -jar ./target/be-transfer-app-0.0.1-SNAPSHOT.jar

In order to check the API you may visit the address below
http://localhost:8080/swagger-ui/index.html#/

Also you may import post man collection below to test the application
Back End Money Transfer.postman_collection.json

Application will insert allowance account from a1 to a100 and restaurant accounts from r1 to r100.
Allowance accounts have 10 units in their balance and restaurant accounts have 0.  

You may send a post request from allowance a1 to restaurant r1 by using the request body below. 
localhost:8080/transfer
{
"allowanceAccountNumber":"a1",
"restaurantAccountNumber":"r1",
"amount":5
}

I used spring cache which uses ConcurrentHashMap as underlying structure to cache the accounts.

