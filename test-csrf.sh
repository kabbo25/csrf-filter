#!/bin/bash

# First, get the CSRF token and session cookie
echo "=== Getting CSRF token ==="
RESPONSE=$(curl -c cookies.txt -b cookies.txt -u user:bc711efe-933d-4274-a559-263bef002d6f -s -i http://localhost:8080/hello)
echo "$RESPONSE"

echo -e "\n=== Trying POST without CSRF token (should fail) ==="
curl -b cookies.txt -u user:bc711efe-933d-4274-a559-263bef002d6f -X POST -s -i http://localhost:8080/hello

echo -e "\n=== Getting CSRF token from a form endpoint ==="
# We need to create an endpoint that provides the CSRF token
curl -b cookies.txt -u user:bc711efe-933d-4274-a559-263bef002d6f -s http://localhost:8080/csrf-token

echo -e "\n=== Using correct CSRF token ==="
# Extract token and use it
CSRF_TOKEN=$(curl -b cookies.txt -u user:bc711efe-933d-4274-a559-263bef002d6f -s http://localhost:8080/csrf-token)
echo "Token: $CSRF_TOKEN"
curl -b cookies.txt -u user:bc711efe-933d-4274-a559-263bef002d6f -X POST -H "X-CSRF-TOKEN: $CSRF_TOKEN" -s -i http://localhost:8080/hello