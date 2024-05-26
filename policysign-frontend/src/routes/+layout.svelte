<script>
	import '../app.css';
</script>

<nav class="bg-gray-100 shadow-md">
  <div class="max-w-7xl mx-auto px-4 sm:px-6 lg:px-8">
    <div class="flex justify-between h-16">
      <div class="flex">
        <div class="-ml-2 mr-2 flex items-center md:hidden">
          <!-- Mobile menu button -->
          <button class="inline-flex items-center justify-center p-2 rounded-md text-gray-400 hover:text-gray-500 hover:bg-gray-100 focus:outline-none focus:bg-gray-100 focus:text-gray-500">
            <span class="sr-only">Open main menu</span>
            <!-- Icon when menu is closed. -->
            <svg class="block h-6 w-6" xmlns="http://www.w3.org/2000/svg" fill="none" viewBox="0 0 24 24" stroke="currentColor" aria-hidden="true">
              <path stroke-linecap="round" stroke-linejoin="round" stroke-width="2" d="M4 6h16M4 12h16m-7 6h7" />
            </svg>
            <!-- Icon when menu is open. -->
            <svg class="hidden h-6 w-6" xmlns="http://www.w3.org/2000/svg" fill="none" viewBox="0 0 24 24" stroke="currentColor" aria-hidden="true">
              <path stroke-linecap="round" stroke-linejoin="round" stroke-width="2" d="M6 18L18 6M6 6l12 12" />
            </svg>
          </button>
        </div>
        <a href="/" class="flex-shrink-0 flex items-center">
          <span class="font-bold text-xl">PolicySign</span>
        </a>
        <div class="hidden md:ml-6 md:flex md:space-x-4">
          <a href="/pricing" class="text-gray-900 hover:bg-gray-700 hover:text-white px-3 py-2 mt-3 mb-3 rounded-md text-sm font-medium">Pricing</a>
          <a href="/documents" class="text-gray-900 hover:bg-gray-700 hover:text-white px-3 py-2 mt-3 mb-3 rounded-md text-sm font-medium">Documents</a>
          <a href="/upload" class="text-gray-900 hover:bg-gray-700 hover:text-white px-3 py-2 mt-3 mb-3 rounded-md text-sm font-medium">Upload</a>
          <a href="/template" class="text-gray-900 hover:bg-gray-700 hover:text-white px-3 py-2 mt-3 mb-3 rounded-md text-sm font-medium">Template</a>
          <a href="/admin" class="text-gray-900 hover:bg-gray-700 hover:text-white px-3 py-2 mt-3 mb-3 rounded-md text-sm font-medium">Admin</a>
          <a href="/signup" class="text-gray-900 hover:bg-gray-700 hover:text-white px-3 py-2 mt-3 mb-3 rounded-md text-sm font-medium">Signup</a>
        </div>
      </div>
    </div>
  </div>

  <!-- Mobile menu, show/hide based on menu state. -->
  <div class="md:hidden">
    <div class="px-2 pt-2 pb-3 space-y-1 sm:px-3">
      <a href="/pricing" class="text-gray-700 hover:bg-gray-700 hover:text-white block px-3 py-2 rounded-md text-base font-medium">Pricing</a>
      <a href="/documents" class="text-gray-700 hover:bg-gray-700 hover:text-white block px-3 py-2 rounded-md text-base font-medium">Documents</a>
      <a href="/upload" class="text-gray-700 hover:bg-gray-700 hover:text-white block px-3 py-2 rounded-md text-base font-medium">Upload</a>
      <a href="/template" class="text-gray-700 hover:bg-gray-700 hover:text-white block px-3 py-2 rounded-md text-base font-medium">Template</a>
      <a href="/admin" class="text-gray-700 hover:bg-gray-700 hover:text-white block px-3 py-2 rounded-md text-base font-medium">Admin</a>
      <a href="/signup" class="text-gray-700 hover:bg-gray-700 hover:text-white block px-3 py-2 rounded-md text-base font-medium">Signup</a>
    </div>
  </div>
</nav>

<div class="container mx-auto px-4 sm:px-6 lg:px-8 mt-3">
  <slot />
</div>
