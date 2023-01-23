# Technical design

BASE_URL = "/api/v1"

## Manage Users and Roles
<details>

### Entity

**Class User**
- Long id
- String username
- String firstName
- String lastName
- String email
- String password
- String phoneNumber
- Role(enum) role
- boolean isBlocked
- LocalDateTime createdAt
- LocalDateTime updatedAt
- LocalDateTime disabledAt

### Dto

**Class UserAdminViewDto**
- Long id
- String username
- String firstName
- String lastName
- String email
- String phoneNumber
- Role role
- boolean isBlocked
- LocalDateTime createdAt

**Class UserDto**
- Long id
- String username
- LocalDateTime createdAt

**Class UserWithAccessDto**
- Long id
- String username
- String firstName
- String lastName
- String email
- String phoneNumber
- LocalDateTime createdAt

**Class UserFilterDto**
- String username
- String email
- String role
- boolean isBlocked
- LocalDate createdAtStart
- LocalDate createdAtEnd
- LocalDate disabledAtStart
- LocalDate disabledAtEnd

**Class SignUpDto**
- String username
- String firstName
- String lastName
- String email
- String password
- String phoneNumber

### Repository

**Interface UserRepository extends JpaRepository\<User, Long>, JpaSpecificationExecutor<User>**

### Service

**CLass UserService**

- Page\<UserDto> getAllUsers(Pageable pageable, UserFilterDto userFilterDto)
- UserDto getUserById(Long id)
- UserDto createUser(SignUpDto signUpDto)
- UserDto updateUser(UserDto userDto)
- void disableUser(Long id)
- UserDto reverseIsBlocked(Long id)

### Converter

**Class UserConverter**
- UserDto convertToDto(User user)
- UserAdminViewDto convertToAdminViewDto(User user)
- User convertToEntity(UserDto dto)
- User convertToEntity(SignUpDto dto)

### Controller

**Class UserController**
- @GetMapping("/users") List\<UserAdminViewDto> getAllUsers()
- @GetMapping("/users/{id}") UserDto getUserById(@PathVariable("id") Long id)
- @GetMapping("/users/profile") UserDto getUserProfile() `After Authorization feature`
- @PostMapping("/users") UserDto addUser(@RequestBody SignUpDto signUpDto)
- @PutMapping("/users/{id}") UserDto updateUserById(@PathVariable("id") Long id, @RequestBody UserDto userDto)
- @DeleteMapping("/users/{id}") void disableUserById(@PathVariable("id") Long id)
- @DeleteMapping("/users") void disableAccount() `After Authorization feature`
- @PutMapping("/users/blocked/{id}") UserAdminViewDto reverseIsBlockedUserById(@PathVariable("id") Long id)
</details>

## Authentication and Authorization
<details>

### Entity

**Class RefreshToken**
- Long id
- User user
- String token
- Instant expireDate

### Dto

**Class LoginRequestDto**
- String username
- String password

**Class UpdatePasswordDto**
- String currentPassword
- String newPassword

**Class LoginResponseDto (impls AuthResponse)**
- String cookie
- String refreshCookie
- UserDto userDto

**Interface AuthResponse**

### Repository

**Interface RefreshTokenRepository extends JpaRepository\<RefreshToken, Long>**
- Optional\<RefreshToken> findByToken(String token)
- void deleteByUserId(Long userId)
- Optional\<RefreshToken> findByUserId(Long userId)

### Service

**CLass AuthService**
- AuthResponse login(LoginRequestDto loginRequestDto)
- LoginResponseDto refreshToken(HttpServletRequest request)
- LoginResponseDto logout(User user)
- void updatePassword(UpdatePasswordDto updatePasswordDto)

**Class UserDetailsServiceImpl**
- UserDetails loadUserByUsername(String email)

**Class RefreshTokenService**
- LoginResponseDto refreshAccessToken(String accessToken)
- RefreshToken createRefreshToken(Long userId)
- RefreshToken verifyExpiration(String token)
- void deleteByUserId(Long userId)

### Controller

**Class AuthController**
- @PostMapping("/auth/login") UserDto authenticateUser(LoginRequestDto loginRequestDto)
- @PostMapping("/auth/logout") void logoutUser()
- @PostMapping("/auth/refresh-token") void refreshToken(HttpServletRequest httpServletRequest)
- @PutMapping("/auth/update-password) void updatePassword(UpdatePasswordDto updatePasswordDto)

### Util

**Class JwtUtil**
- ResponseCookie generateJwtCookie(UserDetailsImpl userDetails)
- ResponseCookie generateJwtCookie(User user)
- ResponseCookie generateRefreshJwtCookie(String refreshToken)
- String getJwtFromCookie(HttpServletRequest httpServletRequest)
- String getJwtRefreshFromCookies(HttpServletRequest httpServletRequest)
- ResponseCookie cleanJwtCookie()
- ResponseCookie clean JwtRefreshCookie()
- String GetUsernameFromJwtToken(String token)
- String validateJwtToken(String token)
- String generateTokenFromUsername(String username)
- ResponseCookie generateCookie(String name, String value, String path)
- String getCookieValueByName(HttpServletRequest httpServletRequest, String name)

### Security
**Class JwtTokenFilter**
- void doFilterInternal(HttpServletRequest httpServletRequest, HttpServletResponse httpServletResponse, FilterChain filterChain)
- String parseJwt(HttpServletRequest httpServletRequest)

**Class UserDetailsImpl**
- Long id
- String password
- Collection\<? extends GrantedAuthority> authorities
- UserDetailsImpl build(User)

**Class AuthEntryPointJwt**
- void commence(HttpServletRequest httpServletRequest, HttpServletResponse httpServletResponse, AuthenticationException exception)

**Class WebSecurityConfig**
- @Bean JwtTokenFilter jwtTokenFilter
- @Bean DaoAuthenticationProvider daoAuthenticationProvider
- @Bean AuthenticationManager authenticationManager
- @Bean PasswordEncoder passwordEncoder
- @Bean SecurityFilterChain securityFilterChain
- @Bean CorsFilter corsFilter

</details>

## Manage Orders
<details>

### Entity

**Class Order**
- Long id
- User owner
- User performer
- Status(enum) status
- String title
- String description
- double price
- String departureCountry
- String departureLocation
- String destinationCountry
- String destinationLocation
- boolean isBlocked
- LocalDateTime createdAt

### Dto

**Class OrderFilterDto**
- String ownerUsername
- String performerUsername
- String status
- String title
- double priceStart
- double priceEnd
- String departureCountry
- String departureLocation
- String destinationCountry
- String destinationLocation
- LocalDate createdAtStart
- LocalDate createdAtEnd

**Class OrderChangeDto**
- Long id
- String title
- String description
- double price
- String departureCountry
- String departureLocation
- String destinationCountry
- String destinationLocation

**Class OrderDto**
- Long id
- User ownerId
- String title
- String description
- double price
- String departureCountry
- String departureLocation
- String destinationCountry
- String destinationLocation
- LocalDateTime createdAt

**Class OrderPersonalViewDto**
- Long id
- User ownerId
- User performerId
- Status(enum) status
- String title
- double price
- LocalDateTime createdAt

**Class OrderShortViewDto**
- Long id
- String title
- double price
- String departureCountry
- String departureLocation
- String destinationCountry
- String destinationLocation
- LocalDateTime createdAt

### Repository
**Interface OrderRepository extends JpaRepository\<Order, Long>, JpaSpecificationExecutor<Order>**

### Service

**Class OrderService**
- Page\<OrderShortViewDto> getAllOrders(OrderFilterDto filterDto, Pageable pageable)
- Page\<OrderShortViewDto> getOwnerOrdersByUserId(Long id, OrderFilterDto filterDto, Pageable pageable)
- Page\<OrderPersonalViewDto> getAuthorisedUserOwnOrders(OrderFilterDto filterDto, Pageable pageable)
- Page\<OrderPersonalViewDto> getAuthorisedUserPerformOrders(OrderFilterDto filterDto, Pageable pageable) `After Perform requests feature`
- OrderDto getOrderById(Long id)
- OrderDto createOrder(OrderChangeDto orderChangeDto)
- OrderDto updateOrderById(OrderChangeDto orderChangeDto)
- void deleteOrderById(Long id)
- void blockOrderById(Long id)
- OrderDto unblockOrderById(Long id)

### Specification

**OrderSpecification**
- Specification<Order> buildGetAllSpecification(OrderFilterDto filter)
- Specification<Order> buildGetAllByUserIdSpecification(Long id, OrderFilterDto filter)

### Converter

**OrderConverter**
- Order convertToEntity (CreateOrderDto dto)
- Order convertToEntity (CreateOrderDto dto, Order comp)
- OrderDto convertToDto(Order order)
- OrderShortViewDto convertToShortViewDto(Order order)

### Controller

**Class OrderController**
- @GetMapping("/orders") Page<OrderShortViewDto> getAllOrders(OrderFilterDto filterDto, Pageable pageable)
- @GetMapping("/orders/authorised") Page<OrderShortViewDto> getAuthorisedUserOrders(OrderFilterDto filterDto, Pageable pageable)
- @GetMapping("/orders/{id}") OrderDto getOrderById(@PathVariable("id") Long id)
- @GetMapping("/orders/users/{id}") Page<OrderShortViewDto> getOrdersByUserId(@PathVariable("id") Long id, OrderFilterDto filterDto, Pageable pageable)
- @PostMapping("/orders") OrderDto createOrder(@Valid @RequestBody OrderDto orderDto)
- @PutMapping("/orders/{id}") OrderDto updateOrder(@PathVariable("id") Long id, @Valid @RequestBody OrderDto orderDto)
- @DeleteMapping("/orders/{id}") void deleteOrderById(@PathVariable("id") Long id)
</details>

## Manage Perform requests
<details>

### Entity

**Class PerformRequest**
- Long id
- Order order
- User performer
- Boolean isApproved
- LocalDateTime createdAt

### Dto

**Class PerformRequestDto**
- Long id
- String orderId
- String performerId
- Boolean isApproved
- LocalDateTime createdAt

### Repository
**Interface PerformRequestRepository extends JpaRepository\<PerformRequest, Long>**

### Service

**Class PerformRequestService**

- Page\<PerformRequestDto> getOrderPerformRequestsByOrderId(Long id, Boolean isApproved, Pageable pageable)
- Page\<PerformRequestDto> getAuthorisedUserPerformRequests(Boolean isApproved, Pageable pageable)
- PerformRequestDto getPerformRequestById(Long id)
- PerformRequestDto createPerformRequestByOrderId(Long id)
- void deletePerformRequestByOrderId(Long id)
- PerformRequestDto approvePerformRequestById(Long id)
- PerformRequestDto rejectPerformRequestById(Long id)

### Converter

**PerformRequestConverter**
- PerformRequestDto convertToDto(PerformRequest performRequest)

### Controller

**Class PerformRequestController**
- @GetMapping("/perform-requests/orders/{id}") Page\<PerformRequestDto> getOrderPerformRequestsByOrderId(@PathVariable("id") Long id, Boolean isApproved, Pageable pageable)
- @GetMapping("/perform-requests") Page\<PerformRequestDto> getAuthorisedUserPerformRequests(Boolean isApproved, Pageable pageable)
- @GetMapping("/perform-requests/{id}") PerformRequestDto getPerformRequestById(@PathVariable("id") Long id)
- @PostMapping("/perform-requests/orders/{id}") PerformRequestDto createPerformRequestByOrderId(@PathVariable("id") Long id)
- @DeleteMapping("/perform-requests/orders/{id}") void deletePerformRequestByOrderId(@PathVariable("id") Long id)
- @PutMapping("/perform-requests/{id}") PerformRequestDto approvePerformRequestById(@PathVariable("id") Long id)
- @PutMapping("/perform-requests/{id}") PerformRequestDto rejectPerformRequestById(@PathVariable("id") Long id)
</details>
