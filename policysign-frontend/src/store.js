import { writable, derived } from "svelte/store";

// user 
export const user = writable({});
let sessionUser = sessionStorage.getItem("user");
if (sessionUser) {
    user.set(JSON.parse(sessionUser));
} else {
    sessionStorage.setItem("user", JSON.stringify({}));
}
// update the user in the sessionStorage on changes
user.subscribe(user => sessionStorage.setItem("user", JSON.stringify(user)));

// isAuthenticated: we assume that users are authenticated if the property "user.name" exists
export const isAuthenticated = derived(
    user,
    $user => $user && $user.name
);

export const jwt_token = writable("");
let sessionToken = sessionStorage.getItem("jwt_token");
if (sessionToken) {
    jwt_token.set(sessionToken);
} else {
    sessionStorage.setItem("jwt_token", "");
}
// update the token in the sessionStorage on changes
jwt_token.subscribe(token => sessionStorage.setItem("jwt_token", token));
