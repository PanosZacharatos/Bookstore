Bookstore Application

Overview:
Springboot based Restful Service(a service to deal with all related services of the web tier and designed CRUD operations on book inventory). The application uses PostegreSQL to store data and exposes API endpoints for quering the database using HTTP requests.

Technology stack: Springboot, PostegreSQL, Maven, Docker, Heroku

Application Structure:
-MVC(Model-View-Controller): Split application into models, views and controllers
-Data Access Layer: Used Spring JPA. Allow to interact with the database, provides repositories interfaces, where method signatures and Spring features were declared. Also allows to keep the code clean and focused on managing data.
-Service Layer: Business logic of the application. Acts as a middleware between data access layer and controller layer. By isolating business, application is ensured easier to be tested and not affects the rest of the application.
-Controller Layer: Handles the HTTP Requests, ensures responses are corectly formatted and returned.

Security:
-Spring Security: Integrated to manage basic authentication, ensuring that endpoints are protected against unauthorized access.

API Design:
-RESTful Principles: API uses HTTP verbs(GET,POST,CREATE,PUT,DELETE) and status codes to handle CRUD operations.
-Error Handling: Implemented error handling to provide error messages HTTP status codes when exceptions occured(using try catch blocks and exception throwing).

Deployment:
-Docker: The application is containerized using Docker. Dockerfile is provided in the root directory of the project, using all the configurations needed.
-Heroku: The application is deployed in Heroku.

Instructions of necessary setups and run the application with examples provided:

-Database setup instructions:
-Installations: PostgreSQL should be installed in your system.
-Create the database: If PostgreSQL is installed, you may run the following commands from a terminal: 1)psql -U postgres
2)CREATE DATABASE bookstore; 3)\l -> to make sure database was created.
-Configurations: Configurations are already set up in application.properties (username and password: postgres and running port in 5433).

-Run the application:
-Set up: Please be sure that JDK 17 is installed, also an IDE (InteliJ is recommended), and maven is supported
-Steps: After cloning the project from github(link is given through mail) import the project in your prefered ide
-Configurations: Project SDK must be Java 17.
-Run the application: Run BookstoreApplication.java
-Access application: Once the application is running you can access locally at "http://localhost:8080/"

-Testing the endpoints(Postman is recommended):
-Prerequisites: Make sure the application is running.
-Set up: Import the collection given in the mail.
-Details: application runs on port 8080, the URL is http://localhost:8080/books.
-Additional: For POST and PUT requests, set the body type to raw and select JSON from the dropdown menu. Then, input the JSON payload
-Authentication: As basic authentication is implemented in authorization tab choose Basic Auth. Username is set to "user" and password to "password"

-Implementation details and explanation:
Security: The application uses Spring Security for basic authentication, ensuring endpoints require username and password to access and protects endpoints from unauthorized access.

    Pagination: The application supports pagination through spring's Pageable interface. This allows for subsets asking in requests using the parameters page and size. Example http://localhost:8080/books?page=0&size=5 brings first page with(up to) 5 entries.

    Search Functionality: The application allows search by fields author and title. Implemented using Spring Data JPA repositories, where methods findBookByTitle and findBookByAuthor map to SQL queries. Example http://localhost:8080/books?title=Title_1 brings all books with title 1.

    JPA and Hibernate: Used for ORM, simplifies database integrations into methods.

    Springboot: Manages enerything from database connection to MVC setup.

    Unit Testing: Ensures functionality and reliability of its components. Tests are crafted using JUnit and Mockito. Every endpoint has 4-5 methods for cases to be considered (happy path,service exception,book not found etc) to cover basic test scenarios of each endpoint.

-Testing unit tests:
-Configurations: All configurations are already set up and imported through maven dependencies.
-Run unit tests: You may run BookControllerTest class for all tests to be executed or you may run seperately each method to run along and check its functionality.

Dockerize the application:
A Dockerfile was created in the root application of the project to dockerize the application. It uses openjdk:17-slim as a base image and sets the app inside the container. It copies the jar file and exports port 8080 where the application runs.
-Instructions how to run: In the terminal in the root directory of the project execute the command mvn clean package to produce the .jar file. After clean builded successfully and after ensuring that docker desktop running, in the same directory run the following commands 1) docker build -t bookstore .
which builds the docker image named bookstore and 2) docker run -p 8080:8080 bookstore which starts the container.
-Testing application: As before you may use an api client programm to test the endpoints as described above.

Deployment to cloud service(Heroku):
The Procfile tells heroku how to run the application
Installations: Be sure that Heroku CLI is installed locally and heroku command is recognized from command line.
Access the application:
URL: https://pure-beach-66031.herokuapp.com/
Before access the link make sure to run "heroku open" from the command line. Once heroku is opened in the default broswer copy paste the link. Credentials are the same are set in Spring Security(user,password).
