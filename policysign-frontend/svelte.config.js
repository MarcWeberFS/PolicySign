import preprocess from 'svelte-preprocess';
import adapter from '@sveltejs/adapter-static';

/** @type {import('@sveltejs/kit').Config} */
const config = {
    // Enables the use of preprocessors for Svelte files
    preprocess: preprocess({
        postcss: true  // Assumes you have PostCSS config set up as needed
    }),

    kit: {
        adapter: adapter({
            // The "pages" option specifies where to output built files
            pages: '../src/main/resources/static',  // Customize this path as necessary
            assets: 'build',  // Customize this path as necessary
            fallback: null   // Set to a file like 'index.html' if you want a SPA fallback
        }),

        // Uncomment and adjust if your SPA has a specific base path
        // paths: {
        //     base: '/your-base-path'
        // },

        // Uncomment if you need to target a specific prerendering or build environment
        // prerender: {
        //     default: true
        // }
    }
};

export default config;
