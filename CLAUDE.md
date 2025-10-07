# CLAUDE.md

This file provides guidance to Claude Code (claude.ai/code) when working with code in this repository.

## Project Overview

This is a Spring Boot 3.5.6 application (Java 21) demonstrating **custom CSRF token management** with database persistence. The application implements a custom `CsrfTokenRepository` that stores CSRF tokens in PostgreSQL, indexed by a custom `X-IDENTITY` header rather than traditional session-based storage.

## Build & Development Commands

### Build and Run
```bash
# Build the project
./mvnw clean package

# Run the application
./mvnw spring-boot:run

# Run tests
./mvnw test
```

### Testing CSRF Protection
```bash
# Test CSRF functionality with the provided script
./test-csrf.sh
```

This script demonstrates:
1. Getting a CSRF token with authentication
2. Attempting POST without CSRF token (fails)
3. Making POST with valid CSRF token (succeeds)

## Architecture

### Custom CSRF Token Management

The core architecture revolves around **database-persisted CSRF tokens** identified by a custom header:

1. **CustomCsrfRepository** (`src/main/java/com/example/csrffilter/CustomCsrfRepository.java`)
   - Implements Spring Security's `CsrfTokenRepository` interface
   - Uses `X-IDENTITY` header to identify clients (not session-based)
   - Persists tokens to PostgreSQL via JPA
   - Key methods:
     - `generateToken()`: Creates UUID-based tokens
     - `saveToken()`: Upserts tokens by identity
     - `loadToken()`: Retrieves tokens by identity

2. **Token Entity & Repository**
   - `Token.java`: JPA entity with `identityType` (client identifier) and `token` (CSRF value)
   - `JpaTokenRepository.java`: Spring Data JPA repository with `findByIdentityType()` query

3. **Security Configuration** (`SecurityConfig.java`)
   - Registers `CustomCsrfRepository` as the CSRF token repository
   - Adds `CsrfTokenLogger` filter before Spring's `CsrfFilter` for debugging
   - Configures HTTP Basic + Form login authentication
   - In-memory user: username `kabbo`, password `kabbo`

### Request Flow

```
Client Request with X-IDENTITY header
    ↓
CsrfTokenLogger (logs token for debugging)
    ↓
Spring Security CsrfFilter
    ↓
CustomCsrfRepository.loadToken(request)
    → Extracts X-IDENTITY header
    → Queries PostgreSQL for matching token
    ↓
Token validation
    ↓
Controller (HelloController or ProductController)
```

### Controllers

- **HelloController**: REST endpoints for testing CSRF
  - `GET /hello`: Simple test endpoint
  - `POST /hello`: Requires valid CSRF token
  - `GET /csrf-token`: Returns current CSRF token for testing

- **ProductController**: Form-based example
  - `GET /product/add`: Displays Thymeleaf form
  - `POST /product/add`: Processes form with CSRF protection

## Database Configuration

- **PostgreSQL**: Database name `demo`, running on `localhost:5432`
- **Credentials**: username/password are `kabbo`/`kabbo` (configured in `application.properties`)
- **Schema**: Auto-generated via Hibernate (`spring.jpa.hibernate.ddl-auto=update`)
- **Table**: `csrf_tokens` with columns `id`, `identity_type`, `token`

## Key Implementation Details

### Custom Header-Based Identity
Unlike typical session-based CSRF protection, this implementation uses the `X-IDENTITY` header to identify clients. This enables:
- Stateless CSRF protection (no server-side sessions)
- Support for distributed systems (tokens stored in shared database)
- Client identification independent of session cookies

### Token Lifecycle
1. Client sends request with `X-IDENTITY` header
2. If no token exists for that identity, generate and save new token
3. If token exists, load and validate against submitted `X-CSRF-TOKEN` header
4. Tokens are updated (not regenerated) for existing identities

### Security Setup
- All endpoints require authentication (HTTP Basic or Form login)
- CSRF protection enabled for all state-changing operations (POST, PUT, DELETE)
- Token passed via `X-CSRF-TOKEN` header (configurable via `CsrfTokenRequestAttributeHandler`)