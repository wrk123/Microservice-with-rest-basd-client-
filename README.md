# Microservice with rest-based client in Spring JPA

This is sample exmaple Spring based microservice. Here there are two microservices.
One is for creating books.  
Second is for creating users and purchaing books.  

After downloading the project, go to respective service directory and  use `mvn clean install` from the command line to build the project.
Make sure you have maven installed on your machine. Follow below procedure.


For user and purchases -
`java -jar target/app-0.0.1-SNAPSHOT.jar --server.port=8080`

For Books -
`java -jar target/app-0.0.1-SNAPSHOT.jar --server.port=8090`
