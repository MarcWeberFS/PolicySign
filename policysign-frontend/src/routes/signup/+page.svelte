<script>
  import { isAuthenticated } from "../../store";
  import auth from "../../auth.service";

  let email = "";
  let password = "";
  let firstName = "";
  let lastName = "";
  let signupForm;
  let showEmailError = false;
  let showPasswordError = false;
  let showFirstNameError = false;
  let showLastNameError = false;

  const api_root = "http://localhost:8080";

  $: if ($isAuthenticated) {
    email = "";
    password = "";
    firstName = "";
    lastName = "";
  }

  async function signup() {
    showEmailError = !email;
    showPasswordError = !password;
    showFirstNameError = !firstName;
    showLastNameError = !lastName;

    if (signupForm.checkValidity()) {
      try {
        await auth.signup(email, password, firstName, lastName);
      } catch (error) {
        console.error("Signup failed", error);
      }
    }
    signupForm.classList.add("was-validated");
  }

  
</script>

<h1 class="text-3xl font-bold text-center mb-6">Sign up</h1>
<div class="flex justify-center">
  <div class="w-full max-w-md">
    <div class="bg-white shadow-md rounded px-8 pt-6 pb-8 mb-4">
      <form
        on:submit|preventDefault={signup}
        bind:this={signupForm}
        class="needs-validation"
        novalidate
      >
        <div class="flex space-x-4">
          <div class="mb-4">
            <label for="first-name" class="block text-gray-700 text-sm font-bold mb-2">First Name</label>
            <input
              bind:value={firstName}
              type="text"
              class="shadow appearance-none border rounded w-full py-2 px-3 text-gray-700 leading-tight focus:outline-none focus:shadow-outline"
              id="first-name"
              name="first-name"
            />
            {#if showFirstNameError}
              <p class="text-red-500 text-xs italic">Please enter your first name.</p>
            {/if}
          </div>
          <div class="mb-4">
            <label for="last-name" class="block text-gray-700 text-sm font-bold mb-2">Last Name</label>
            <input
              bind:value={lastName}
              type="text"
              class="shadow appearance-none border rounded w-full py-2 px-3 text-gray-700 leading-tight focus:outline-none focus:shadow-outline"
              id="last-name"
              name="last-name"
            />
            {#if showLastNameError}
              <p class="text-red-500 text-xs italic">Please enter your last name.</p>
            {/if}
          </div>
        </div>
        
        <div class="mb-4">
          <label for="username" class="block text-gray-700 text-sm font-bold mb-2">E-Mail</label>
          <input
            bind:value={email}
            type="email"
            class="shadow appearance-none border rounded w-full py-2 px-3 text-gray-700 leading-tight focus:outline-none focus:shadow-outline"
            id="username"
            name="username"
            required
          />
          {#if showEmailError}
            <p class="text-red-500 text-xs italic mt-2">Please provide an e-mail address.</p>
          {/if}
        </div>
        <div class="mb-6">
          <label for="password" class="block text-gray-700 text-sm font-bold mb-2">Password</label>
          <input
            bind:value={password}
            type="password"
            class="shadow appearance-none border rounded w-full py-2 px-3 text-gray-700 mb-3 leading-tight focus:outline-none focus:shadow-outline"
            id="password"
            name="password"
            required
          />
          {#if showPasswordError}
            <p class="text-red-500 text-xs italic">Please choose a password.</p>
          {/if}
        </div>
        <div class="flex items-center justify-between">
          <button type="submit" class="bg-blue-500 hover:bg-blue-700 text-white font-bold py-2 px-4 rounded focus:outline-none focus:shadow-outline">Sign up</button>
          <a href="/login" class="inline-block align-baseline font-bold text-sm text-blue-500 hover:text-blue-800">Log in</a>
        </div>
      </form>
    </div>
  </div>
</div>
