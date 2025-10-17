#!/usr/bin/env bash

URL="http://localhost:8080/realms/DAT152/protocol/openid-connect/token"
CLIENT_ID="dat152oblig2"
OUTFILE="tokens.json"

{
  echo "# user1"
  curl -s -X POST "$URL" \
    --data "grant_type=password&client_id=$CLIENT_ID&username=user1&password=user1"
  echo -e "\n\n# user2"
  curl -s -X POST "$URL" \
    --data "grant_type=password&client_id=$CLIENT_ID&username=user2&password=user2"
  echo -e "\n\n# user3"
  curl -s -X POST "$URL" \
    --data "grant_type=password&client_id=$CLIENT_ID&username=user3&password=user3"
} > "$OUTFILE"
