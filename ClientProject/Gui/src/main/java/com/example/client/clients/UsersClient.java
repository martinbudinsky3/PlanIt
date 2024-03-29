package com.example.client.clients;

import com.example.client.exceptions.ConflictException;
import com.example.client.exceptions.UnauthorizedException;
import com.example.client.utils.Utils;
import com.example.dto.mappers.UserDTOMapper;
import com.example.dto.user.UserCreateDTO;
import com.example.dto.user.UserDataDTO;
import com.example.model.LoginData;
import com.example.model.LoginResponse;
import com.example.model.User;
import com.example.utils.PropertiesReader;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.*;
import org.springframework.web.client.HttpStatusCodeException;
import org.springframework.web.client.RestTemplate;

import java.util.prefs.Preferences;


/**
 * Class communicating with server. This class is focused on posting and getting requests related to the User object.
 */

public class UsersClient {
    private static UsersClient instance;

    private final PropertiesReader uriPropertiesReader = new PropertiesReader("uri.properties");
    private final String BASE_URI = uriPropertiesReader.getProperty("base-uri");

    private final RestTemplate restTemplate = new RestTemplate();
    private final ObjectMapper objectMapper = new ObjectMapper();
    private final UserDTOMapper userDTOmapper = new UserDTOMapper();
    private static final Logger logger = LoggerFactory.getLogger(UsersClient.class);
    private final Preferences preferences = Preferences.userRoot();
    private final Utils utils = new Utils();

    private UsersClient() { }

    public static UsersClient getInstance() {
        if(instance == null) {
            instance = new UsersClient();
        }

        return instance;
    }

    /**
     * Method needed for login of user. After entering the username and password, this method returns User with given data
     * (if the data is correct and the user was previously registered)
     *
     * @param username username of given user
     * @param password password of given user
     * @return User object to which the data belong.
     */
    public void login(String username, String password)
            throws Exception {
        try {
            logger.info("Logging in user {}", username);
            final String uri = BASE_URI + "/login";
            LoginData loginData = new LoginData(username, password);

            LoginResponse loginResponse = restTemplate.postForObject(uri, loginData, LoginResponse.class);
            logger.info("User {} successfully logged in", username);
            preferences.put("accessToken", loginResponse.getAccessToken());
            preferences.put("refreshToken", loginResponse.getRefreshToken());
        } catch (Exception e) {
            logger.error("Unsuccessful login attempt", e);
            if(e instanceof HttpStatusCodeException) {
                if(((HttpStatusCodeException) e).getStatusCode() == HttpStatus.UNAUTHORIZED) {
                    throw new UnauthorizedException();
                }
            }

            throw e;
        }

    }

    /**
     * Method needed for registration of new user. After entering the first name, last name, username and password.
     *
     * @param user User that is going to be registered,
     * @return ID of newly registered user.
     */
    public void register(User user) throws Exception {
        logger.info("Registering user {}" + user.getUsername());
        final String uri = BASE_URI + "/auth/register";

        UserCreateDTO userCreateDTO = userDTOmapper.userToUserCreateDTO(user);
        try {
            Long id  = restTemplate.postForObject(uri, userCreateDTO, Long.class);
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

    public User getUserInfo() throws Exception {
        logger.info("Getting user data");
        final String uri = BASE_URI + "/user";

        User user;
        try {
            HttpHeaders headers = utils.createAuthenticationHeader();
            HttpEntity entity = new HttpEntity(headers);
            ResponseEntity response = restTemplate.exchange(uri, HttpMethod.GET, entity, String.class);

            String userDataJSon = (String) response.getBody();
            UserDataDTO userDataDTO = objectMapper.readValue(userDataJSon, new TypeReference<UserDataDTO>() {
            });
            user = userDTOmapper.userDataDTOtoUser(userDataDTO);
            logger.info("Returning user data");
        } catch (Exception e) {
            logger.error("Error while getting user data", e);
            if (e instanceof HttpStatusCodeException) {
                if (((HttpStatusCodeException) e).getStatusCode() == HttpStatus.UNAUTHORIZED) {
                    throw new UnauthorizedException();
                }
            }

            throw e;
        }

        return user;
    }
}
