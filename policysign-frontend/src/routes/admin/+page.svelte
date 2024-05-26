<script>
    import { onMount } from 'svelte';
    import { get } from 'svelte/store';
    import { userId, jwtToken } from '../../auth.service';
    let jwt_token;
    jwtToken.subscribe(value => {
        jwt_token = value;
    });


    let userCount = 0;
    let documentCount = 0;
    let documents = [];
    let errorMessage = "";

    const api_root = "http://localhost:8080";

    // Redirect to login if not authenticated
    $: if (!get(userId)) {
        window.location.href = "/login";
    }

    onMount(async () => {
        await fetchUserCount();
        await fetchDocumentCount();
        await fetchAllDocuments();
    });

    async function fetchUserCount() {
        try {
            const response = await fetch(`${api_root}/api/users/count`, {
                method: 'GET',
                headers: {
                    Authorization: `Bearer ${jwt_token}`
                }
            });

            if (response.ok) {
                userCount = await response.json();
            } else {
                errorMessage = "Failed to fetch user count";
            }
        } catch (error) {
            console.error("Error fetching user count:", error);
            errorMessage = "Error fetching user count";
        }
    }

    async function fetchDocumentCount() {
        try {
            const response = await fetch(`${api_root}/api/upload/count`, {
                method: 'GET',
                headers: {
                    Authorization: `Bearer ${jwt_token}`
                }
            });

            if (response.ok) {
                documentCount = await response.json();
            } else {
                errorMessage = "Failed to fetch document count";
            }
        } catch (error) {
            console.error("Error fetching document count:", error);
            errorMessage = "Error fetching document count";
        }
    }

    async function fetchAllDocuments() {
        try {
            const response = await fetch(`${api_root}/api/upload/all`, {
                method: 'GET',
                headers: {
                    Authorization: `Bearer ${jwt_token}`
                }
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

    async function deleteDocument(id) {
        try {
            const response = await fetch(`${api_root}/api/upload/delete/${id}`, {
                method: 'POST',
                headers: {
                    Authorization: `Bearer ${jwt_token}`
                }
            });

            if (response.ok) {
                documents = documents.filter(document => document.id !== id);
                window.location.reload();
            } else {
                errorMessage = "Failed to delete document";
            }
        } catch (error) {
            console.error("Error deleting document:", error);
            errorMessage = "Error deleting document";
        }
    }
</script>

<style>
    .status-label {
        padding: 0.25rem 0.5rem;
        border-radius: 0.375rem;
        font-weight: 600;
    }
    .status-pending {
        background-color: #f59e0b; /* yellow */
        color: white;
    }
    .status-signed {
        background-color: #10b981; /* green */
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
    .delete-button {
        background-color: #e3342f;
        color: white;
        padding: 0.5rem;
        border-radius: 0.375rem;
        cursor: pointer;
    }
</style>

<h1 class="text-3xl font-bold text-center mb-6">Admin Dashboard</h1>

{#if errorMessage}
    <p class="text-red-500 text-center">{errorMessage}</p>
{/if}

<div class="container mx-auto px-4 mb-6">
    <div class="flex justify-between">
        <div>
            <h2 class="text-2xl font-bold">User Count: {userCount}</h2>
        </div>
        <div>
            <h2 class="text-2xl font-bold">Document Count: {documentCount}</h2>
        </div>
    </div>
</div>

<h2 class="text-2xl font-bold mb-4 mt-4 text-center">All Documents</h2>
<div class="container mx-auto px-4">
    <div class="flex flex-wrap -mx-4">
        {#each documents as document}
            <div class="document-card w-full p-4">
                <div class="bg-white shadow-md rounded-lg p-6">
                    <h2 class="text-xl font-bold mb-2">
                        <a href={`${api_root}/api/upload/download/${document.id}?token=${jwt_token}`} download="{document.title}.pdf">{document.title}</a>
                    </h2>
                    <div class="document-details">
                        <div>
                            <p><span class="label">UserID:</span> {document.userId}</p>
                        </div>
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
                        <div>
                            <button class="delete-button" on:click={() => deleteDocument(document.id)}>Delete</button>
                        </div>
                    </div>
                </div>
            </div>
        {/each}
    </div>
</div>
