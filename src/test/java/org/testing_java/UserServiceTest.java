package org.testing_java;

import org.junit.Before;
import org.junit.ClassRule;
import org.junit.jupiter.api.*;
import org.testcontainers.junit.jupiter.Container;
import org.testcontainers.junit.jupiter.Testcontainers;
import org.testcontainers.containers.MySQLContainer;
import org.testcontainers.utility.DockerImageName;
import org.testing_java.util.TestUtils;

import java.sql.ResultSet;
import java.sql.SQLException;

import static org.junit.jupiter.api.Assertions.*;
@Testcontainers
@TestMethodOrder(MethodOrderer.MethodName.class)
class UserServiceTest {
    @ClassRule
    static MySQLContainer<?> mySQLContainer = new MySQLContainer<>("mysql:latest");
    static UserService userService;
    static ConnectionPool connectionPool;
    static TestUtils testUtils;

    @BeforeAll
    static void startDb() throws SQLException {
        mySQLContainer.start();
        String url = mySQLContainer.getJdbcUrl();
        connectionPool =  new ConnectionPool(url, mySQLContainer.getUsername(), mySQLContainer.getPassword());
        testUtils = new TestUtils(connectionPool);
        testUtils.setUpData();
        userService = new UserService( connectionPool);
    }

    @AfterAll
    static void stopDb(){
        mySQLContainer.stop();
    }

    @Test
    public void containerRunning(){
        assertTrue(mySQLContainer.isRunning());
    }

    @Test
    public void getUser() throws SQLException {
        User testUser = testUtils.getRandomUser();
        User user = userService.getUser(testUser.getId());
        assertEquals(testUser.getId(), user.getId());
        assertEquals(testUser.getName(), user.getName());
    }

    @Test
    public void createUser() throws SQLException {
        String testName = testUtils.getRandomString(10);
        User testUser = new User(testName);
        User newUser = userService.createUser(testUser);
        assertNotNull(newUser.getId());
        User user = userService.getUser(newUser.getId());
        assertEquals(testName, user.getName());
    }
}