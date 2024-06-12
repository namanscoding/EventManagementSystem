# Event Management System using Spring Security

The Event Management System is a web application that allows users to manage events. The system has two types of users: admin and user. Admins have the ability to add, update, delete, and view events, whereas regular users can only view events. The application uses Spring Security with JWT (JSON Web Token) for authentication and authorization.

# Technologies Used
Spring Boot
Spring Security
JWT (JSON Web Token)
JPA/Hibernate
PostgresSQL
Maven

# User Roles
Admin: Can add, update, delete, and view events.
User: Can only view events.

# Application Port
8081

- Please update username and password according to the local postgresSQL's username and password in application.properties
#PostgresConfig
spring.datasource.url=jdbc:postgresql://localhost:5432/Event_Registration?useSSL=false
spring.datasource.username=
spring.datasource.password=

- Before running the application, create a new database named Event_Registration
Create Database Event_Registration

- After running the application successfully, hit the following query:
INSERT INTO roles(name) VALUES('ROLE_USER');
INSERT INTO roles(name) VALUES('ROLE_ADMIN');

API Endpoints
User Authentication
- POST http://localhost:8081/api/auth/signup - Allows to add new user.
Body:
{
    "username":"naman2",
    "email":"naman2@gmail.com",
    "password":"123456789",
    "role":["admin"]

}

Response: 200 OK User added Successfully

- POST http://localhost:8081/api/auth/signin - Allows user to signin and generated the token
Body:
{
    "username":"naman2",
    "password":"123456789"

}

Response: 200 OK {
    "id": 2,
    "username": "naman2",
    "email": "naman2@gmail.com",
    "roles": [
        "ROLE_ADMIN"
    ],
    "tokenType": "Bearer",
    "accessToken": "eyJhbGciOiJIUzI1NiJ9.eyJzdWIiOiJuYW1hbjIiLCJpYXQiOjE3MTgyMDc2NjUsImV4cCI6MTcxODI5NDA2NX0.nc--ftUgtJqtPpokF4mqK6T0BZYj4lwqrjzAsvw5UdM"
}

Event Management
- POST: http://localhost:8081/api/test/addEvent?Authorization=Bearer eyJhbGciOiJIUzI1NiJ9.eyJzdWIiOiJuYW1hbjIiLCJpYXQiOjE3MTgyMDc2NjUsImV4cCI6MTcxODI5NDA2NX0.nc--ftUgtJqtPpokF4mqK6T0BZYj4lwqrjzAsvw5UdM - Add an event, only admin can add a new event. Also, provide Authorization Parameters(example given in API).
Body:
{
    "eventName": "Sample Event1",
    "eventDate" : "2024-05-07",
    "eventLocation" : "Bangalore",
    "registrationDetails" : "sampleRegistrationdetail 1"

}

- PUT: http://localhost:8081/api/test/updateEvent?Authorization=Bearer eyJhbGciOiJIUzI1NiJ9.eyJzdWIiOiJuYW1hbjIiLCJpYXQiOjE3MTgyMDc2NjUsImV4cCI6MTcxODI5NDA2NX0.nc--ftUgtJqtPpokF4mqK6T0BZYj4lwqrjzAsvw5UdM - Update any event, only admin can update any event. Also provide Authorization Parameters(example given in API).
Body:
{
    "id": 1,
    "eventName": "Sample Event updated",
    "eventDate": "2024-05-06T00:00:00.000+00:00",
    "eventLocation": "Hyderabad",
    "registrationDetails": "sampleRegistrationdetail"
} 
Response 200 OK Event Updated Successfully

- DELETE: http://localhost:8081/api/test/deleteEvent?id=1&Authorization=Bearer eyJhbGciOiJIUzI1NiJ9.eyJzdWIiOiJuYW1hbjIiLCJpYXQiOjE3MTgyMDc2NjUsImV4cCI6MTcxODI5NDA2NX0.nc--ftUgtJqtPpokF4mqK6T0BZYj4lwqrjzAsvw5UdM - delete any event, only admin can delete any event. Also provide eventId and Authorization Parameters(example given in API).
Response 200 OK Event Deleted Successfully

- GET: http://localhost:8081/api/test/viewEvents?Authorization=Bearer eyJhbGciOiJIUzI1NiJ9.eyJzdWIiOiJuYW1hbiIsImlhdCI6MTcxODIwNjAzMSwiZXhwIjoxNzE4MjkyNDMxfQ.PlFgr9L2QcBUzJqW4i9WlHqZ8dnXNKCzFXT6Smqx8bY - Both Admin and User can view the events.
Response 200 OK {
    "id": 1,
    "eventName": "Sample Event updated",
    "eventDate": "2024-05-06T00:00:00.000+00:00",
    "eventLocation": "Bangalore",
    "registrationDetails": "sampleRegistrationdetail"
}

- GET: GET: http://localhost:8081/api/test/viewEventsByName?name=Sample Event&Authorization=Bearer eyJhbGciOiJIUzI1NiJ9.eyJzdWIiOiJuYW1hbiIsImlhdCI6MTcxODIwNjAzMSwiZXhwIjoxNzE4MjkyNDMxfQ.PlFgr9L2QcBUzJqW4i9WlHqZ8dnXNKCzFXT6Smqx8bY - Both Admin and User can view the events by it's name.
Response 200 OK {
    "id": 2,
    "eventName": "Sample Event",
    "eventDate": "2024-05-06T00:00:00.000+00:00",
    "eventLocation": "Hyderabad",
    "registrationDetails": "sampleRegistrationdetail"
}


# Security Configuration
JWT Token Generation
JWT tokens are generated upon successful authentication and must be included in the Authorization header of subsequent requests. The token has a limited validity period and includes the user’s roles and username.

Spring Security Configuration
WebSecurityConfig: Configures security settings, including endpoint protection based on roles.
JwtAuthenticationFilter: Filters incoming requests to validate JWT tokens.
JwtProvider: Generates and validates JWT tokens.
Example Security Configuration
    @Bean
    public DaoAuthenticationProvider authenticationProvider() {
        DaoAuthenticationProvider authProvider = new DaoAuthenticationProvider();

        authProvider.setUserDetailsService(userDetailsService);
        authProvider.setPasswordEncoder(passwordEncoder());

        return authProvider;
    }
    @Bean
    public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {
        http.csrf(csrf -> csrf.disable())
                .exceptionHandling(exception -> exception.authenticationEntryPoint(unauthorizedHandler))
                .sessionManagement(session -> session.sessionCreationPolicy(SessionCreationPolicy.STATELESS))
                .authorizeHttpRequests(auth ->
                        auth.requestMatchers("/api/auth/**").permitAll()
                                .requestMatchers("/api/test/**").permitAll()
                                .anyRequest().authenticated()
                );

        http.authenticationProvider(authenticationProvider());

        http.addFilterBefore(authenticationJwtTokenFilter(), UsernamePasswordAuthenticationFilter.class);

        return http.build();
    }

JWT Utility Class

@Component
public class JwtUtils {
  private static final Logger logger = LoggerFactory.getLogger(JwtUtils.class);

  @Value("${bezkoder.app.jwtSecret}")
  private String jwtSecret;

  @Value("${bezkoder.app.jwtExpirationMs}")
  private int jwtExpirationMs;

  public String generateJwtToken(Authentication authentication) {

    UserDetailsImpl userPrincipal = (UserDetailsImpl) authentication.getPrincipal();

    return Jwts.builder()
        .setSubject((userPrincipal.getUsername()))
        .setIssuedAt(new Date())
        .setExpiration(new Date((new Date()).getTime() + jwtExpirationMs))
        .signWith(key(), SignatureAlgorithm.HS256)
        .compact();
  }
  
  private Key key() {
    return Keys.hmacShaKeyFor(Decoders.BASE64.decode(jwtSecret));
  }

  public String getUserNameFromJwtToken(String token) {
    return Jwts.parserBuilder().setSigningKey(key()).build()
               .parseClaimsJws(token).getBody().getSubject();
  }

  public boolean validateJwtToken(String authToken) {
    try {
      Jwts.parserBuilder().setSigningKey(key()).build().parse(authToken);
      return true;
    } catch (MalformedJwtException e) {
      logger.error("Invalid JWT token: {}", e.getMessage());
    } catch (ExpiredJwtException e) {
      logger.error("JWT token is expired: {}", e.getMessage());
    } catch (UnsupportedJwtException e) {
      logger.error("JWT token is unsupported: {}", e.getMessage());
    } catch (IllegalArgumentException e) {
      logger.error("JWT claims string is empty: {}", e.getMessage());
    }

    return false;
  }
}

Custom User Details Service
@Service
public class UserDetailsServiceImpl implements UserDetailsService {
  @Autowired
  UserRepository userRepository;

  @Override
  @Transactional
  public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
    User user = userRepository.findByUsername(username)
        .orElseThrow(() -> new UsernameNotFoundException("User Not Found with username: " + username));

    return UserDetailsImpl.build(user);
  }

}

Authentication Controller
@RequestMapping("/api/auth")
public class AuthController {
  @Autowired
  AuthenticationManager authenticationManager;

  @Autowired
  UserRepository userRepository;

  @Autowired
  RoleRepository roleRepository;

  @Autowired
  PasswordEncoder encoder;

  @Autowired
  JwtUtils jwtUtils;

  @PostMapping("/signin")
  public ResponseEntity<?> authenticateUser(@Valid @RequestBody LoginRequest loginRequest) {

    Authentication authentication = authenticationManager.authenticate(
        new UsernamePasswordAuthenticationToken(loginRequest.getUsername(), loginRequest.getPassword()));

    SecurityContextHolder.getContext().setAuthentication(authentication);
    String jwt = jwtUtils.generateJwtToken(authentication);
    
    UserDetailsImpl userDetails = (UserDetailsImpl) authentication.getPrincipal();
    List<String> roles = userDetails.getAuthorities().stream()
        .map(item -> item.getAuthority())
        .collect(Collectors.toList());

    return ResponseEntity.ok(new JwtResponse(jwt,
                         userDetails.getId(), 
                         userDetails.getUsername(), 
                         userDetails.getEmail(), 
                         roles));
  }

  @PostMapping("/signup")
  public ResponseEntity<?> registerUser(@Valid @RequestBody SignupRequest signUpRequest) {
    if (userRepository.existsByUsername(signUpRequest.getUsername())) {
      return ResponseEntity
              .badRequest()
              .body(new MessageResponse("Error: Username is already taken!"));
    }

    if (userRepository.existsByEmail(signUpRequest.getEmail())) {
      return ResponseEntity
              .badRequest()
              .body(new MessageResponse("Error: Email is already in use!"));
    }

    // Create new user's account
    User user = new User(signUpRequest.getUsername(),
            signUpRequest.getEmail(),
            encoder.encode(signUpRequest.getPassword()));

    Set<String> strRoles = signUpRequest.getRole();
    Set<Role> roles = new HashSet<>();

    if (strRoles == null) {
      Role userRole = roleRepository.findByName(ERole.ROLE_USER)
              .orElseThrow(() -> new RuntimeException("Error: Role is not found."));
      roles.add(userRole);
    } else {
      strRoles.forEach(role -> {
        switch (role) {
          case "admin":
            Role adminRole = roleRepository.findByName(ERole.ROLE_ADMIN)
                    .orElseThrow(() -> new RuntimeException("Error: Role is not found."));
            roles.add(adminRole);

            break;
          case "mod":
            Role modRole = roleRepository.findByName(ERole.ROLE_MODERATOR)
                    .orElseThrow(() -> new RuntimeException("Error: Role is not found."));
            roles.add(modRole);

            break;
          default:
            Role userRole = roleRepository.findByName(ERole.ROLE_USER)
                    .orElseThrow(() -> new RuntimeException("Error: Role is not found."));
            roles.add(userRole);
        }
      });
    }

    user.setRoles(roles);
    userRepository.save(user);

    return ResponseEntity.ok(new MessageResponse("User registered successfully!"));
  }

Event Controller
@RequestMapping("/api/test")
public class eventController {
    @Autowired
    private eventService eventServiceObj;

    @PostMapping("/addEvent")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<Object> addEvent(@RequestBody events eventsObj)
    {
        return eventServiceObj.addEvent(eventsObj);
    }

    @GetMapping("/viewEvents")
    @PreAuthorize("hasRole('USER') or hasRole('MODERATOR') or hasRole('ADMIN')")
    public ResponseEntity<Object> viewEvents()
    {
        return eventServiceObj.viewEvent();
    }

    @GetMapping("/viewEventsByName")
    @PreAuthorize("hasRole('USER') or hasRole('MODERATOR') or hasRole('ADMIN')")
    public ResponseEntity<Object> viewEventsByName(@RequestParam String name)
    {
        return eventServiceObj.viewEventByName(name);
    }

    @PutMapping("/updateEvent")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<Object> updateEvent(@RequestBody events eventsObj)
    {
        return eventServiceObj.updateEvent(eventsObj);
    }

    @DeleteMapping("/deleteEvent")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<Object> deleteEvent(@RequestParam Long id)
    {
        return eventServiceObj.deleteEvent(id);
    }
}
