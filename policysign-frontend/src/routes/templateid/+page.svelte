<script>
    import { writable, get } from 'svelte/store';
    import { userId } from '../../auth.service';
    import { onMount } from 'svelte';

    // State variables
    const step = writable(1);
    const pdfUrl = writable('');
    const pdfHeight = writable('500px');
    const markerX = writable(null);
    const markerY = writable(null);
    const markerSize = writable(50);
    const successMessage = writable('');
    let link = true;
    let email = '';
    let language = 'java';

    let userIdValue;
    let template = {};
    let error = '';

    // Fetch template function
    async function fetchTemplate(id) {
        try {
            const response = await fetch(`http://localhost:8080/api/templates/${id}`);
            if (!response.ok) {
                throw new Error('Failed to fetch template');
            }
            template = await response.json();
        } catch (err) {
            error = err.message;
            console.error('Failed to fetch template:', error);
        }
    }

    // Get the template ID from the URL
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

    // PDF click handler
    function onPdfClick(event) {
        const rect = event.currentTarget.getBoundingClientRect();
        const x = event.clientX - rect.left;
        const y = event.clientY - rect.top;

        const actualWidth = 595.28;
        const actualHeight = 841.89;

        const displayedWidth = rect.width;
        const displayedHeight = rect.height;

        const scaleX = actualWidth / displayedWidth;
        const scaleY = actualHeight / displayedHeight;

        const adjustedX = x * scaleX;
        const adjustedY = y * scaleY;

        const clampedX = Math.min(Math.max(adjustedX, 0), actualWidth);
        const clampedY = Math.min(Math.max(adjustedY, 0), actualHeight);

        markerX.set(clampedX);
        markerY.set(clampedY);
    }

    // Generate API call code
    function generateApiCall(language, id, link, email, userId) {
        switch (language) {
            case 'java':
                return `
                    import java.io.*;
                    import java.net.*;
                    public class ApiCall {
                        public static void main(String[] args) throws Exception {
                            URL url = new URL("http://localhost:8080/api/templates/use/${id}?link=${link}&signedByEmail=${email}&userId=${userId}");
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
                    curl -X POST "http://localhost:8080/api/templates/use/${id}?link=${link}&signedByEmail=${email}&userId=${userId}" -H "Content-Type: application/json"
                `;
            case 'python':
                return `
                    import requests

                    url = "http://localhost:8080/api/templates/use/${id}?link=${link}&signedByEmail=${email}&userId=${userId}"
                    response = requests.post(url, headers={"Content-Type": "application/json"})
                    print(response.text)
                `;
            case 'javascript':
                return `
                    fetch("http://localhost:8080/api/templates/use/${id}?link=${link}&signedByEmail=${email}&userId=${userId}", {
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
                    $url = "http://localhost:8080/api/templates/use/${id}?link=${link}&signedByEmail=${email}&userId=${userId}";
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
</script>

{#if error}
    <p class="error">{error}</p>
{:else}
    {#if template.title}
        <div>
            <h1>{template.title}</h1>
            <p>{template.description}</p>

            <label>
                <input type="checkbox" bind:checked={link}>
                Link
            </label>

            <div class:visible={!link}>
                <label for="email">E-Mail of the signee</label>
                <input
                    type="email"
                    bind:value={email}
                    id="email"
                    placeholder="E-Mail"
                    class="input"
                    required={!link}
                />
            </div>

            <h2>API Calls</h2>
            <div>
                <label>
                    <input type="radio" name="language" value="java" bind:group={language}> Java
                </label>
                <label>
                    <input type="radio" name="language" value="curl" bind:group={language}> Curl
                </label>
                <label>
                    <input type="radio" name="language" value="python" bind:group={language}> Python
                </label>
                <label>
                    <input type="radio" name="language" value="javascript" bind:group={language}> JavaScript
                </label>
                <label>
                    <input type="radio" name="language" value="php" bind:group={language}> PHP
                </label>
            </div>

            <pre>
                <code>{generateApiCall(language, template.id, link, email, userIdValue)}</code>
            </pre>
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
