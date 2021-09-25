package com.example.client;

import com.example.exceptions.rest.ConflictException;
import com.example.model.LoginData;
import com.example.model.LoginResponse;
import com.example.model.User;
import com.example.utils.PropertiesReader;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.web.client.HttpStatusCodeException;
import org.springframework.web.client.ResourceAccessException;
import org.springframework.web.client.RestTemplate;


/**
 * Class communicating with server. This class is focused on posting and getting requests related to the User object.
 */

public class UsersClient {

    private final PropertiesReader uriPropertiesReader = new PropertiesReader("uri.properties");
    private final String BASE_URI = uriPropertiesReader.getProperty("base-uri");

    private final ObjectMapper objectMapper = new ObjectMapper();
    private final RestTemplate restTemplate = new RestTemplate();
    private static final Logger logger = LoggerFactory.getLogger(UsersClient.class);

    /**
     * Method needed for login of user. After entering the username and password, this method returns User with given data
     * (if the data is correct and the user was previously registered)
     *
     * @param username username of given user
     * @param password password of given user
     * @return User object to which the data belong.
     */
    public void login(String username, String password)
            throws ResourceAccessException, HttpStatusCodeException {
        logger.info("Logging in user {}", username);
        final String uri = BASE_URI + "/login";
        LoginData loginData = new LoginData(username, password);

        LoginResponse loginResponse = restTemplate.postForObject(uri, loginData, LoginResponse.class);
        // TODO save somewhere tokens from response
        // TODO handle error
    }

    /**
     * Method needed for registration of new user. After entering the first name, last name, username and password.
     *
     * @param user User that is going to be registered,
     * @return ID of newly registered user.
     */
    public void register(User user) throws Exception {
        try {
            logger.info("Registering user {}" + user.getUsername());

            final String uri = BASE_URI + "/auth/register";
            Long id  = restTemplate.postForObject(uri, user, Long.class);

            logger.info("User {} successfully registered with id {}", user.getUsername(), id);
        } catch (Exception e) {
            logger.error("Unsuccessful registration of user " + user.getUsername(), e);
            if(e instanceof HttpStatusCodeException) {
                if(((HttpStatusCodeException) e).getStatusCode() == HttpStatus.CONFLICT) {
                    throw new ConflictException();
                }
            }

            throw e;
        }
    }
}