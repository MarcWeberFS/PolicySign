import { createAuth0Client } from "@auth0/auth0-spa-js"; // npm install @auth0/auth0-spa-js
import { writable } from "svelte/store";
import { user, jwt_token } from "./store";
import { goto } from '$app/navigation';
import axios from "axios";
import config from "./auth.config";

export const userId = writable(null); // Define userId store
const api_root = "http://localhost:8080";
let auth0Client;

async function createClient() {
  auth0Client = await createAuth0Client({
    domain: config.auth0_domain,
    clientId: config.auth0_client_id,
  });
}

// Auth0 signup endpoint documentation: see https://auth0.com/docs/libraries/custom-signup#using-the-api
async function signup(email, password, firstName = null, lastName = null) {
  var options = {
    method: "post",
    url: `https://${config.auth0_domain}/dbconnections/signup`,
    data: {
      client_id: config.auth0_client_id,
      email: email,
      password: password,
      connection: "Username-Password-Authentication",
      // you can set any of these properties as well if needed
      // username: "johndoe", // if not provided, email will be used as username for login. if provided, username has to be validated (must not already exist)
      // given_name: firstName,
      // family_name: lastName,
      // nickname: "Johnny", // if not provided, the part before the @ of the e-mail address will be used
      // name: "John Doe",
      // picture: "http://example.org/jdoe.png",
    },
  };

  if (firstName && firstName.length > 0) {
    options.data.given_name = firstName;
  }

  if (lastName && lastName.length > 0) {
    options.data.family_name = lastName;
  }

  try {
    const signupResponse = await axios(options);
    console.log("Signup successful1", signupResponse);

    // wait 2 seconds to ensure roles are set in Auth0
    setTimeout(async () => {
      await saveUserToDB(signupResponse.data);
      await login(email, password, true);
    }, 2000);
  } catch (error) {
    alert("Signup failed: " + error);
    console.log(error);
  }
}

function login(username, password, redirectToHome = false) {
  var options = {
    method: "post",
    url: `https://${config.auth0_domain}/oauth/token`,
    data: {
      grant_type: "password",
      username: username,
      password: password,
      audience: `https://${config.auth0_domain}/api/v2/`,
      scope: "openid profile email",
      client_id: config.auth0_client_id,
    },
  };

  axios(options)
    .then((response) => {
      const { id_token, access_token } = response.data;
      jwt_token.set(id_token);
      console.log(id_token);
      getUserInfo(access_token);
      if (redirectToHome) {
        // go to start page after 500ms. Explanation: if we do not wait, the login form on the
        // start page might still be visible because $isAuthenticated is not yet set to true.
        setTimeout(() => {
          goto("/") 
        }, 500);
      }
    })
    .catch(function (error) {
      alert("login failed");
      console.log(error);
    });
}

function getUserInfo(access_token) {
  var options = {
    method: "get",
    url: `https://${config.auth0_domain}/oauth/userinfo`,
    headers: {
      "Content-Type": "application/json",
      Authorization: "Bearer " + access_token,
    },
  };

  axios(options)
    .then((response) => {
      const userInfo = response.data;
      console.log("User Info:", userInfo); // Log user info to confirm
      user.set(userInfo);
      if (userInfo.sub) {
        userId.set(userInfo.sub); // Set userId store
        console.log("User ID set:", userInfo.sub); // Log to confirm userId is set
      } else {
        console.log("No sub found in userInfo");
      }
    })
    .catch(function (error) {
      alert("getUserInfo failed");
      console.log(error);
    });
}

async function logout() {
  try {
    await createClient();
    user.set({});
    jwt_token.set("");
    userId.set(null);
    await auth0Client.logout({logoutParams:{returnTo: window.location.origin}});
  } catch (e) {
    console.error(e);
  }
  goto("/") // return to main page
}

const auth = {
  signup,
  login,
  logout,
};

async function saveUserToDB(userInfo) {
  try {
    const response = await fetch(`${api_root}/api/users`, {
      method: 'POST',
      headers: {
        'Content-Type': 'application/json'
      },
      body: JSON.stringify({
        username: `${userInfo.given_name} ${userInfo.family_name}`,
        email: userInfo.email,
        roles: ['user'],
        id: `auth0|${userInfo._id}`, // Assuming signupResponse contains userId or similar field
      })
    });

    if (response.ok) {
      console.log("User saved to DB");
    } else {
      const errorData = await response.json();
      console.error("Failed to save user to DB", errorData);
    }
  } catch (error) {
    console.error("Error saving user to DB", error);
  }
}

export default auth;
