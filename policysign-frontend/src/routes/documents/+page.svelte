<script>
    import { onMount } from 'svelte';
    import { userId } from '../../auth.service';
    import { get } from 'svelte/store';

    let documents = [];
    let errorMessage = "";

    const api_root = "http://localhost:8080";

    $: if (!get(userId)) {
        window.location.href = "/login";
    }

    onMount(async () => {
        const currentUserId = get(userId);
        if (currentUserId) {
            try {
                const response = await fetch(`${api_root}/api/upload/user/${currentUserId}`, {
                    method: 'GET'
                });

                if (response.ok) {
                    documents = await response.json();
                } else {
                    errorMessage = "Failed to fetch documents";
                }
            } catch (error) {
                console.error("Error fetching documents:", error);
                errorMessage = "Error fetching documents";
            }
        }
    });
</script>

<style>
    .status-label {
        padding: 0.25rem 0.5rem;
        border-radius: 0.375rem;
        font-weight: 600;
    }
    .status-pending {
        background-color: #f59e0b;
        color: white;
    }
    .status-signed {
        background-color: #10b981;
        color: white;
    }
    .document-card {
        display: flex;
        flex-direction: column;
    }
    .document-details {
        display: flex;
        flex-wrap: wrap;
        gap: 0.5rem;
        align-items: center;
    }
    .document-details > div {
        flex: 1;
    }
    .label {
        font-weight: bold;
    }
</style>

<h1 class="text-3xl font-bold text-center mb-6">My Documents</h1>

{#if errorMessage}
    <p class="text-red-500 text-center">{errorMessage}</p>
{/if}

<div class="container mx-auto px-4">
    <div class="flex flex-wrap -mx-4">
        {#each documents as document}
            <div class="document-card w-full p-4">
                <div class="bg-white shadow-md rounded-lg p-6">
                    <h2 class="text-xl font-bold mb-2">
                        <a href={`${api_root}/api/upload/download/${document.id}`} download="{document.title}.pdf">{document.title}</a>
                    </h2>
                    <div class="document-details">
                        <div>
                            <p><span class="label">Signed by:</span> {document.signedByEmail}</p>
                        </div>
                        <div>
                            <p class="status-label {document.status === 'PENDING' ? 'status-pending' : 'status-signed'}">
                                {document.status}
                            </p>
                        </div>
                        <div>
                            <p><span class="label">Created on:</span> {new Date(document.creationDate).toLocaleDateString()}</p>
                        </div>
                        <div>
                            <p><span class="label">Last updated:</span> {new Date(document.updateDate).toLocaleDateString()}</p>
                        </div>
                    </div>
                </div>
            </div>
        {/each}
    </div>
</div>
