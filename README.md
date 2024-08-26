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


Instructions of necessary setups and run the application with examples provided