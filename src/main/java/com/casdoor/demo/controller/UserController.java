package com.casdoor.demo.controller;

import com.casdoor.demo.configuration.CasdoorConfig;
import com.casdoor.demo.constants.Constants;
import com.casdoor.demo.dto.*;
import com.casdoor.demo.entity.ApplicationUser;
import com.casdoor.demo.mapper.ApplicationUserMapper;
import com.casdoor.demo.repository.ApplicationUserRepository;
import jakarta.servlet.http.HttpServletResponse;
import org.apache.oltu.oauth2.client.OAuthClient;
import org.apache.oltu.oauth2.client.URLConnectionClient;
import org.apache.oltu.oauth2.client.request.OAuthClientRequest;
import org.apache.oltu.oauth2.client.response.OAuthJSONAccessTokenResponse;
import org.apache.oltu.oauth2.common.OAuth;
import org.apache.oltu.oauth2.common.exception.OAuthProblemException;
import org.apache.oltu.oauth2.common.exception.OAuthSystemException;
import org.apache.oltu.oauth2.common.message.types.GrantType;
import org.casbin.casdoor.entity.Role;
import org.casbin.casdoor.service.AuthService;
import org.casbin.casdoor.entity.User;
import org.casbin.casdoor.service.RoleService;
import org.casbin.casdoor.service.UserService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.ui.Model;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;


import javax.validation.Valid;
import javax.validation.constraints.NotNull;
import java.io.IOException;
import java.time.Instant;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.*;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/api/user/v1")
public class UserController {

    private final AuthService authService;
    private final UserService userService;
    private final RoleService roleService;
    private final CasdoorConfig config;
    private final ApplicationUserMapper userMapper;

    //Don't mark as private
    final ApplicationUserRepository applicationUserRepository;
    private final PasswordEncoder passwordEncoder;

    public UserController(AuthService authService, UserService userService,
                          RoleService roleService, CasdoorConfig config,
                          ApplicationUserMapper userMapper,
                          PasswordEncoder passwordEncoder,
                          ApplicationUserRepository applicationUserRepository) {
        this.authService = authService;
        this.userService = userService;
        this.roleService = roleService;
        this.config = config;
        this.passwordEncoder = passwordEncoder;
        this.userMapper = userMapper;
        this.applicationUserRepository = applicationUserRepository;
    }

    //    @Autowired
//    private CasdoorTokenProcessor casdoorTokenProcessor;


    private static Logger log = LoggerFactory.getLogger(UserController.class);

    @PostMapping("/login")
    public ResponseEntity<APIResponse> loginPage(@Validated @RequestBody WebLoginDTO webLoginDTO) {

        //Invoke the casdoor login API and pass the userid, password to get t

        String username = webLoginDTO.getUsername();
        String password = webLoginDTO.getPassword();


        String accessTokenURL = String.format("%s/api/login/oauth/access_token", config.getEndpoint());

        log.info("access token URL is {}", accessTokenURL);

        try {
            OAuthClientRequest oAuthClientRequest = OAuthClientRequest
                    .tokenLocation(accessTokenURL)
                    .setGrantType(GrantType.PASSWORD)
                    .setClientId(config.getClientId())
                    .setClientSecret(config.getClientSecret())
                    .setUsername(username)
                    .setPassword(password)
                    .buildQueryMessage();

            OAuthClient oAuthClient = new OAuthClient(new URLConnectionClient());
            OAuthJSONAccessTokenResponse oAuthResponse = oAuthClient.accessToken(oAuthClientRequest, OAuth.HttpMethod.POST);

            String accessToken = oAuthResponse.getAccessToken();

            User user = authService.parseJwtToken(accessToken);

            System.out.println(String.format("user name is %s", user.name));


//            Map<String, Object> claims = casdoorTokenProcessor.extractPayloadInfo(accessToken);

//            claims.put("roles", user.roles.stream().map(r -> r.name).collect(Collectors.toList()));
//
//            String[] aud = (String[]) claims.get("aud");
//            // Convert array to a comma-separated String
//            String audString = Arrays.stream(aud).collect(Collectors.joining(","));
//
//            long epochSeconds = (long) claims.get("exp");
//            String iss = (String) claims.get("iss");
//            // Convert epoch seconds to milliseconds
//            long epochMillis = epochSeconds * 1000;
//
//            // Create a Date object
//            Date date = new Date(epochMillis);
//
//           String tokenWithRole = casdoorTokenProcessor.generateToken(claims, user.name, iss, audString, date);

            String refreshToken = oAuthResponse.getRefreshToken();
            String scope = oAuthResponse.getScope();
            Long expiresIn = oAuthResponse.getExpiresIn();

            LinkedHashMap<String, Object> userDataInfo = new LinkedHashMap<>(5);
            log.info("generating JWT token to access the app");
            userDataInfo.put("name", "Yaantrac");
            userDataInfo.put("userId", user.name);
            userDataInfo.put("token", accessToken);
            userDataInfo.put("refreshToken", refreshToken);
            userDataInfo.put("expiresIn", expiresIn);
            userDataInfo.put("scope", scope);

            List<LinkedHashMap<String, Object>> loginDetails = new ArrayList<>(1);
            loginDetails.add(userDataInfo);
            log.info("Logged in successfully");


            return new ResponseEntity<>(new APIResponse(Constants.SUCCESS, "Logged in successfully", loginDetails), HttpStatus.CREATED);
        } catch (OAuthSystemException | OAuthProblemException e) {
            log.error("{} exception caught in login API: {}", e.getClass().getSimpleName(), e.getMessage());
            return ResponseEntity.internalServerError().body(new APIResponse(Constants.ERROR, Constants.SERVER_ERROR_MESSAGE, null));
        }

    }

    @PostMapping("/refresh")
    public ResponseEntity<APIResponse> refreshToken(@Valid @RequestBody RefreshTokenDTO refreshTokenDTO) {

        String refreshTokenURL = String.format("%s/api/login/oauth/refresh_token", config.getEndpoint());

        log.info("access token URL is {}", refreshTokenURL);

        String refreshToken = refreshTokenDTO.getRefreshToken();
        String username = refreshTokenDTO.getUsername();

        try {
            OAuthClientRequest oAuthClientRequest = OAuthClientRequest
                    .tokenLocation(refreshTokenURL)
                    .setRefreshToken(refreshToken)
                    .setGrantType(GrantType.REFRESH_TOKEN)
                    .setScope("")
                    .setClientId(config.getClientId())
                    .setClientSecret(config.getClientSecret())
                    .buildQueryMessage();

            OAuthClient oAuthClient = new OAuthClient(new URLConnectionClient());
            OAuthJSONAccessTokenResponse oAuthResponse = oAuthClient.accessToken(oAuthClientRequest, OAuth.HttpMethod.POST);

            String accessToken = oAuthResponse.getAccessToken();

            refreshToken = oAuthResponse.getRefreshToken();
            String scope = oAuthResponse.getScope();
            Long expiresIn = oAuthResponse.getExpiresIn();

            log.info("Refresh token generated successfully");

            LinkedHashMap<String, Object> userDataInfo = new LinkedHashMap<>(5);
            userDataInfo.put("name", "Yaantrac");
            userDataInfo.put("userId", username);
            userDataInfo.put("token", accessToken);
            userDataInfo.put("refreshToken", refreshToken);
            userDataInfo.put("expiresIn", expiresIn);
            userDataInfo.put("scope", scope);

            List<LinkedHashMap<String, Object>> loginDetails = new ArrayList<>(1);
            loginDetails.add(userDataInfo);


            return new ResponseEntity<>(new APIResponse(Constants.SUCCESS, "Refresh token generated successfully", loginDetails), HttpStatus.CREATED);
        } catch (OAuthSystemException | OAuthProblemException e) {
            log.error("{} exception caught in login API: {}", e.getClass().getSimpleName(), e.getMessage());
            return ResponseEntity.internalServerError().body(new APIResponse(Constants.ERROR, Constants.SERVER_ERROR_MESSAGE, null));
        }

    }


    @PostMapping("/addRole")
    public ResponseEntity<APIResponse> addRole(@Valid @RequestBody AddRoleDTO addRoleDTO) {

        try {
            String roleName = addRoleDTO.getName();

            String description = addRoleDTO.getDescription();

            Role role = new Role();
            role.owner = config.getApplicationName();
            role.name = roleName;
            role.description = description;


            roleService.addRole(role);

            String message = String.format("%s role created successfully", roleName);
            log.info(message);


            return new ResponseEntity<>(new APIResponse(Constants.SUCCESS, message, role), HttpStatus.CREATED);
        } catch (IOException e) {
            log.error("{} caught in addRole method: {}", e.getClass().getSimpleName(), e.getMessage());
            return ResponseEntity.internalServerError().body(new APIResponse(Constants.ERROR, Constants.SERVER_ERROR_MESSAGE, null));
        }
    }

    @PostMapping("/userRoleMapping")
    public ResponseEntity<APIResponse> mapUserWithRole(@RequestParam @NotNull String username,
                                                       @RequestParam @NotNull String roleName) {

        try {

            User user = userService.getUser(username);
            Role role = roleService.getRole(roleName);

            addUserToRole(role, user);

            String message = String.format("%s role successfully mapped to user: %s", roleName, username);
            log.info(message);


            return new ResponseEntity<>(new APIResponse(Constants.SUCCESS, message, role), HttpStatus.CREATED);

        } catch (IOException e) {
            log.error("{} exception caught in login API: {}", e.getClass().getSimpleName(), e.getMessage());
            return ResponseEntity.internalServerError().body(new APIResponse(Constants.ERROR, Constants.SERVER_ERROR_MESSAGE, null));
        }

    }

    private void addUserToRole(Role role, User user) throws IOException {

        if (role.users == null) {
            role.users = new String[0];
        }

        // Convert the existing array to a List to allow modifications
        List<String> userList = new ArrayList<>(Arrays.asList(role.users));

        // Add the new user to the list
        userList.add(user.name);

        // Convert the list back to an array
        role.users = userList.toArray(new String[0]);

        // Update the role with the new user list
        roleService.updateRole(role);
    }

    @PostMapping("/signup")
    public ResponseEntity<APIResponse> signupPage(@Validated @RequestBody final AddUserDTO addUserDTO) {

        try {

            //Invoke the casdoor signup API to add these user information to database

            User user = new User();

            user.name = addUserDTO.getUsername();
            user.password = addUserDTO.getPassword();

            log.debug("application name is {}", config.getApplicationName());

            user.owner = config.getApplicationName();
            user.signupApplication = config.getApplicationName();

            // Get the current date and time
            LocalDateTime now = LocalDateTime.now();

            // Define the desired format with seconds included
            DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");

            // Format the LocalDateTime object into a String
            String formattedDateTime = now.format(formatter);
            user.createdTime = formattedDateTime;

            user.displayName = addUserDTO.getUsername();
            user.gender = addUserDTO.gender;
            user.email = addUserDTO.getEmail();
            user.phone = addUserDTO.getMobileNumber();
            user.isAdmin = false;

            String userDTORole = addUserDTO.getRole();

            List<Role> availableRoles = roleService.getRoles();

            boolean roleFound = false;

            for (Role role : availableRoles) {

                if (role.name.equalsIgnoreCase(userDTORole)) {

                    roleFound = true;

                    List<Role> userRoles = new ArrayList<>();

                    addUserToRole(role, user);

                    userRoles.add(role);

                    user.roles = userRoles;

                    userService.addUser(user);
                }
            }
            List<String> roleNames = availableRoles.stream().map(role -> role.name).collect(Collectors.toList());

            if (!roleFound) {
                String message = String.format("Invalid role type: %s. Valid roles are : %s", userDTORole, Arrays.toString(roleNames.toArray()));
                return ResponseEntity.badRequest().body(new APIResponse(Constants.ERROR, message, null));
            } else {



                ApplicationUser applicationUser = userMapper.fromDto(addUserDTO);


                log.info(" returned user is {}", applicationUser);

//                applicationUser.setCreatedDate(new Date());
//                applicationUser.setStatus("active");
//                applicationUser.setCreatedBy("SuperAdmin");
//                applicationUser.setCasdoorPassword(passwordEncoder.encode(addUserDTO.getPassword()));

                applicationUserRepository.save(applicationUser);

                Instant.now().getEpochSecond();
                log.info("user added successfully");
                return new ResponseEntity<>(new APIResponse(Constants.SUCCESS, "new user added", new ArrayList<>()), HttpStatus.CREATED);
            }


        } catch (IOException e) {
            log.error("IOException happened in signup method: {}", e.getMessage());
            return ResponseEntity.internalServerError().body(new APIResponse(Constants.ERROR, Constants.SERVER_ERROR_MESSAGE, null));
        }
    }


    //Callback Endpoint
    @GetMapping("/login/oauth2/code/casdoor")
    public ResponseEntity<APIResponse> casdoorCallback(@RequestParam("code") String code, @RequestParam("state") String state, Model model, HttpServletResponse response) throws IOException {
        APIResponse apiResponse = new APIResponse();
        try {
            String token = authService.getOAuthToken(code, state);
            User user = authService.parseJwtToken(token);

            // Load user details and set authentication.

            User user1 = userService.getUser(user.name);
            org.springframework.security.core.userdetails.User userDetails = new org.springframework.security.core.userdetails.User(user1.name, user.password, new ArrayList<>());
//            UserDetails userDetails =
            UsernamePasswordAuthenticationToken authentication = new UsernamePasswordAuthenticationToken(userDetails, null, userDetails.getAuthorities());
            SecurityContextHolder.getContext().setAuthentication(authentication);

            apiResponse.setStatus(Constants.SUCCESS);
            apiResponse.setMessage("Authentication successful");
            apiResponse.setData(user);
            return ResponseEntity.ok().body(apiResponse);
        } catch (Exception e) {

            apiResponse.setStatus(Constants.ERROR);
            apiResponse.setMessage("Authentication failed: " + e.getMessage());
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(apiResponse);
        }
    }

    @GetMapping("/signup/oauth/authorize")
    public void redirectSignup(HttpServletResponse response) throws IOException {
        String signupUrl = authService.getSignupUrl();
        response.sendRedirect(signupUrl);
    }
}
