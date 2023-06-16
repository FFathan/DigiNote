THIS IS API METHODS FOR DIGINOTE BACKEND

# Registration API Documentation

This document provides information on how to use the Registration API to register a new user account.

## Base URL

The base URL for all API endpoints is `https://example.com`.

## Endpoint

### Register a new user

Endpoint: `POST /register`

Registers a new user account.

#### Request Body

The request body should be in JSON format and include the following fields:

- `username` (string): The username for the new account.
- `email` (string): The email address for the new account.
- `password` (string): The password for the new account. It must be at least 8 characters long and include at least one uppercase letter, one number, and one special character.

Example:

```json
{
  "username": "digiNote",
  "email": "Digi@note.id",
  "password": "HasshedPassword"
}

