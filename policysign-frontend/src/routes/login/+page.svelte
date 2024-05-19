<script>
  import { isAuthenticated } from "../../store";
  import auth from "../../auth.service";

  let email = "";
  let password = "";
  let loginForm;

  $: if ($isAuthenticated) {
    // Redirect or perform actions after successful login
  }

  function login() {
    if (loginForm.checkValidity()) {
      console.log("login");
      auth.login(email, password);
    }
    loginForm.classList.add("was-validated");
  }
</script>

<h1 class="text-3xl font-bold text-center my-6">Login</h1>
<div class="flex justify-center">
  <div class="w-full max-w-sm">
    <div class="bg-white shadow-md rounded px-8 pt-6 pb-8 mb-4">
      <form
        on:submit|preventDefault={login}
        bind:this={loginForm}
        class="needs-validation"
        novalidate
      >
        <div class="mb-4">
          <label for="email" class="block text-gray-700 text-sm font-bold mb-2">E-Mail</label>
          <input bind:value={email} type="email" class="shadow appearance-none border rounded w-full py-2 px-3 text-gray-700 leading-tight focus:outline-none focus:shadow-outline" id="email" required />
          <p class="text-red-500 text-xs italic mt-2">Please enter your email address.</p>
        </div>
        <div class="mb-6">
          <label for="password" class="block text-gray-700 text-sm font-bold mb-2">Password</label>
          <input bind:value={password} type="password" class="shadow appearance-none border rounded w-full py-2 px-3 text-gray-700 mb-3 leading-tight focus:outline-none focus:shadow-outline" id="password" required />
          <p class="text-red-500 text-xs italic">Please enter your password.</p>
        </div>
        <div class="flex items-center justify-between">
          <button type="submit" class="bg-blue-500 hover:bg-blue-700 text-white font-bold py-2 px-4 rounded focus:outline-none focus:shadow-outline">Log in</button>
          <a href="/signup" class="inline-block align-baseline font-bold text-sm text-blue-500 hover:text-blue-800">Sign up</a>
        </div>
      </form>
    </div>
  </div>
</div>
