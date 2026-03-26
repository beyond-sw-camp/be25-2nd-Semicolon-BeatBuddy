#!/bin/bash
CLIENT_ID=$(grep 'client-id:' src/main/resources/application-dev.yml | awk '{print $2}')
CLIENT_SECRET=$(grep 'client-secret:' src/main/resources/application-dev.yml | awk '{print $2}')

# 1. Get Token
echo "Getting token..."
TOKEN_RES=$(curl -s -X POST https://accounts.spotify.com/api/token \
  -u "$CLIENT_ID:$CLIENT_SECRET" \
  -d "grant_type=client_credentials")
echo $TOKEN_RES
TOKEN=$(echo $TOKEN_RES | grep -o '"access_token":"[^"]*' | grep -o '[^"]*$')

echo "Token: $TOKEN"

# 2. Search
echo "Searching..."
curl -v -s -X GET "https://api.spotify.com/v1/search?q=IU&type=track&limit=1" \
  -H "Authorization: Bearer $TOKEN"
