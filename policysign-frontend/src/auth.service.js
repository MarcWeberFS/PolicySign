// auth.service.js
import { createAuth0Client } from "@auth0/auth0-spa-js"; 
import { writable } from "svelte/store";
import { user, jwt_token } from "./store";
import { goto } from '$app/navigation';
import axios from "axios";
import config from "./auth.config";

export const userId = writable(localStorage.getItem('userId')); // Initialize userId store from local storage
export const jwtToken = jwt_token;
const api_root = "http://localhost:8080";
let auth0Client;

async function createClient() {
  auth0Client = await createAuth0Client({
    domain: config.auth0_domain,
    clientId: config.auth0_client_id,
  });
}

async function signup(email, password, firstName = null, lastName = null) {
  const options = {
    method: "post",
    url: `https://${config.auth0_domain}/dbconnections/signup`,
    data: {
      client_id: config.auth0_client_id,
      email: email,
      password: password,
      connection: "Username-Password-Authentication",
      given_name: firstName,
      family_name: lastName,
    },
  };

  try {
    const signupResponse = await axios(options);
    console.log("Signup successful", signupResponse);

    // Wait 2 seconds to ensure roles are set in Auth0
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
  const options = {
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
      localStorage.setItem("jwt_token", id_token);
      getUserInfo(access_token);
      if (redirectToHome) {
        setTimeout(() => {
          goto("/");
        }, 500);
      }
    })
    .catch(function (error) {
      alert("Login failed");
      console.log(error);
    });
}

function getUserInfo(access_token) {
  const options = {
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
      user.set(userInfo);
      if (userInfo.sub) {
        userId.set(userInfo.sub);
        localStorage.setItem('userId', userInfo.sub);
      }
    })
    .catch(function (error) {
      alert("Get user info failed");
      console.log(error);
    });
}

async function logout() {
  try {
    await createClient();
    user.set({});
    jwt_token.set("");
    userId.set(null);
    localStorage.removeItem('userId');
    await auth0Client.logout({ logoutParams: { returnTo: window.location.origin } });
  } catch (e) {
    console.error(e);
  }
  goto("/");
}

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
        id: `auth0|${userInfo._id}`,
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

const auth = {
  signup,
  login,
  logout,
};

export default auth;
