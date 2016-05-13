# Microservice with rest-basd client in Spring JPA

This is sample exmaple Spring based microservice. Here there are two microservices. 
One is for creating books.
Second is for creating users and purchaing books. 
Do a git checkout and then got to the directoy and execute the jar file in the target folder using the below code on different ports. Make sure to run books on the port mentioned as given below. Books API will not work on any other ports except what is mentioned on the link.

For user and purchases -
java -jar target/app-0.0.1-SNAPSHOT.jar --server.port=8080

For Books 
java -jar target/app-0.0.1-SNAPSHOT.jar --server.port=8090
