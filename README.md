# HOW TO RUN
1. from directory /docker/dev run command ``docker-compose up``
2. Use java 11 
3. run main class com.usermind.rule.RuleApp with VM options ``-Dspring.profiles.active=dev,no-security``
4. http://localhost:8080/swagger-ui.html 
5. mvn clean install 

# WHAT TO DEMONSTRATE
1. Old List organization no Pageable
2. Old jdbi3 sql files detached from code to many files
3. schema.sql, data.sql
4. Table name chaos lower upper,"Organizations" fields not equal to Object ones, fields not used,
    @Transaction annotation not working in rule-store 
    how to correct
    spring.liquibase.enabled=false 
    @Table(name = "`Organizations`")  spring.datasource.initialization-mode=always
5. integration testing "mvn clean install -P integration", -Dspring.profiles.active=h2
    
6. @DataJpaTest @Sql("/tests/getOrganizationsTest.sql") vs @SpringBootTest
8. @ActiveProfiles("h2") 
9. @WebMvcTest(controllers = OrganizationController.class)  
    -1 for page validations
10. Spring security + JWT, For latter OAuth2 with Keycloak -Dspring.profiles.active=dev,security
11. http://localhost:8080/swagger-ui.html 
    authentication exception, actuator


Note that by default the application context containing all these components, 
including the in-memory database, is shared between all test methods within all 
@DataJpaTest-annotated test classes.

This is why, by default, each test method runs in its own transaction, 
which is rolled back after the method has executed. 
This way, the database state stays pristine between tests and the tests stay 
independent of each other.



