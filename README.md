#homework-lv

Just run HomeworkApplication.java.
Should add Lombok plugin and enable annotation processing.
For IntelliJ: Settings -> Build, Execution, Deployment -> Compiler -> Annotation Processors -> Enable annotation processing

Will run on 8080 port by default. Port can be changed in application.properties.

URLs
http://localhost:8080/create
http://localhost:8080/extend
http://localhost:8080/loans/{username}

When listing loans sorted by 'created' field.

For the sake of simplicity there is no logging, no user authentication (just username is passed by user), no exception handling and custom error page (just default Spring error page).

Just run src/integration_test/RunCukesTest.groovy to run integration tests (REST API tests) or run 'gradle cucumber'
