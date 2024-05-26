<script>
    import { writable, get } from 'svelte/store';
    import { onMount, afterUpdate } from 'svelte';
    import hljs from 'highlight.js';
    import 'highlight.js/styles/github.css';
    import { userId, jwtToken } from '../../auth.service';
    
    let jwt_token;
    jwtToken.subscribe(value => {
        jwt_token = value;
    });

    let link = true;
    let language = 'java';
    let userIdValue;
    let template = {};
    let error = '';

    async function fetchTemplate(id) {
        try {
            const response = await fetch(`https://policysign.azurewebsites.net/api/templates/${id}`, {
                headers: {
                    Authorization: "Bearer " + jwt_token
                }
            });
            if (!response.ok) {
                throw new Error('Failed to fetch template');
            }
            template = await response.json();
        } catch (err) {
            error = err.message;
            console.error('Failed to fetch template:', error);
        }
    }

    onMount(async () => {
        userIdValue = get(userId);
        if (!userIdValue) {
            window.location.href = '/login';
            return;
        }

        const params = new URLSearchParams(window.location.search);
        const id = params.get('id');
        await fetchTemplate(id);
    });

    $: codeContent = generateApiCall(language, template.id, link, userIdValue);

    afterUpdate(() => {
        document.querySelectorAll('pre code').forEach((block) => {
            hljs.highlightElement(block);
        });
    });

    function generateApiCall(language, id, link, userId) {
        switch (language) {
            case 'java':
                return `
import java.io.*;
import java.net.*;
public class ApiCall {
    public static void main(String[] args) throws Exception {
        URL url = new URL("https://policysign.azurewebsites.net/api/templates/use/${id}?link=${link}&signedByEmail=SIGNERSEMAIL&userId=${userId}");
        HttpURLConnection con = (HttpURLConnection) url.openConnection();
        con.setRequestMethod("POST");
        con.setRequestProperty("Content-Type", "application/json; utf-8");
        con.setRequestProperty("Accept", "application/json");
        con.setDoOutput(true);
        try (OutputStream os = con.getOutputStream()) {
            byte[] input = "{}".getBytes("utf-8");
            os.write(input, 0, input.length);
        }
        try (BufferedReader br = new BufferedReader(new InputStreamReader(con.getInputStream(), "utf-8"))) {
            StringBuilder response = new StringBuilder();
            String responseLine;
            while ((responseLine = br.readLine()) != null) {
                response.append(responseLine.trim());
            }
            System.out.println(response.toString());
        }
    }
}
                `;
            case 'curl':
                return `
curl -X POST "https://policysign.azurewebsites.net/api/templates/use/${id}?link=${link}&signedByEmail=SIGNERSEMAIL&userId=${userId}" -H "Content-Type: application/json"
                `;
            case 'python':
                return `
import requests

url = "https://policysign.azurewebsites.net/api/templates/use/${id}?link=${link}&signedByEmail=SIGNERSEMAIL&userId=${userId}"
response = requests.post(url, headers={"Content-Type": "application/json"})
print(response.text)
                `;
            case 'javascript':
                return `
fetch("https://policysign.azurewebsites.net/api/templates/use/${id}?link=${link}&signedByEmail=SIGNERSEMAIL&userId=${userId}", {
    method: 'POST',
    headers: {
        'Content-Type': 'application/json'
    },
    body: JSON.stringify({})
})
.then(response => response.json())
.then(data => console.log(data));
                `;
            case 'php':
                return `
<?php
$url = "https://policysign.azurewebsites.net/api/templates/use/${id}?link=${link}&signedByEmail=SIGNERSEMAIL&userId=${userId}";
$options = array(
    'http' => array(
        'header'  => "Content-type: application/json\\r\\n",
        'method'  => 'POST',
        'content' => '{}',
    ),
);
$context  = stream_context_create($options);
$result = file_get_contents($url, false, $context);
if ($result === FALSE) { /* Handle error */ }
var_dump($result);
?>
                `;
        }
    }

    function copyToClipboard(text) {
        navigator.clipboard.writeText(text).then(() => {
            alert('Copied to clipboard');
        }, (err) => {
            console.error('Could not copy text: ', err);
        });
    }
</script>

{#if error}
    <p class="text-red-500">{error}</p>
{:else}
    {#if template.title}
        <div class="container mx-auto p-4">
            <h1 class="text-3xl font-bold mb-4">{template.title}</h1>
            <p class="text-gray-700 mb-6">{template.description}</p>

            <label class="inline-flex items-center mb-4">
                <input type="checkbox" bind:checked={link} class="form-checkbox text-blue-600">
                <span class="ml-2">Link</span>
            </label>

            <h2 class="text-2xl font-semibold mb-4">API Calls</h2>
            <div class="mb-4">
                <label class="inline-flex items-center mr-4">
                    <input type="radio" name="language" value="java" bind:group={language} class="form-radio text-blue-600">
                    <span class="ml-2">Java</span>
                </label>
                <label class="inline-flex items-center mr-4">
                    <input type="radio" name="language" value="curl" bind:group={language} class="form-radio text-blue-600">
                    <span class="ml-2">Curl</span>
                </label>
                <label class="inline-flex items-center mr-4">
                    <input type="radio" name="language" value="python" bind:group={language} class="form-radio text-blue-600">
                    <span class="ml-2">Python</span>
                </label>
                <label class="inline-flex items-center mr-4">
                    <input type="radio" name="language" value="javascript" bind:group={language} class="form-radio text-blue-600">
                    <span class="ml-2">JavaScript</span>
                </label>
                <label class="inline-flex items-center">
                    <input type="radio" name="language" value="php" bind:group={language} class="form-radio text-blue-600">
                    <span class="ml-2">PHP</span>
                </label>
            </div>

            <pre class="text-sm bg-gray-100 p-4 rounded mb-4 overflow-x-auto">
                <code>{codeContent}</code>
            </pre>
            <button on:click={() => copyToClipboard(codeContent)} class="bg-blue-500 hover:bg-blue-700 text-white font-bold py-2 px-4 rounded">
                Copy to Clipboard
            </button>
        </div>
    {:else}
        <p>Loading template...</p>
    {/if}
{/if}

<style>
    .input {
        padding: 0.5em;
        margin: 0.5em;
        color: #333;
        background: #f9f9f9;
        border: none;
        border-radius: 3px;
    }

    .visible {
        display: block;
    }

    .hidden {
        display: none;
    }
</style>
