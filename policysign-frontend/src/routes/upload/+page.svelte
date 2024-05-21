<script>
    import { writable } from "svelte/store";
    import { v4 as uuidv4 } from "uuid";

    const api_root = "http://localhost:8080";

    let file;
    let pdfUrl = writable("");
    let pdfHeight = writable("500px");
    let step = writable(1);
    let markerX = writable(null);
    let markerY = writable(null);
    let markerSize = writable(50);
    const fileUrl = writable('');

    let email = "";
    let title = "";
    let description = "";
    let userIdValue;
    let successMessage = writable("");

    const userSession = sessionStorage.getItem("user");
    if (userSession) {
        const userObject = JSON.parse(userSession);
        userIdValue = userObject.sub;
        console.log("User ID retrieved from session storage:", userIdValue);
    } else {
        console.log("No user found in session storage.");
    }

    const loadPdfJs = () => {
        return new Promise((resolve, reject) => {
            if (window.pdfjsLib) {
                resolve();
                return;
            }
            const script = document.createElement("script");
            script.src = "https://cdnjs.cloudflare.com/ajax/libs/pdf.js/2.10.377/pdf.min.js";
            script.onload = () => resolve();
            script.onerror = () => reject(new Error("Failed to load PDF.js"));
            document.head.appendChild(script);
        });
    };

    const uploadFile = async (event) => {
        const files = event.target.files;
        if (files.length > 0) {
            file = files[0];
            const objectUrl = URL.createObjectURL(file);
            pdfUrl.set(objectUrl);

            await loadPdfJs();
            const pdfDoc = await pdfjsLib.getDocument(objectUrl).promise;
            const pageCount = pdfDoc.numPages;
            pdfHeight.set(`${pageCount * 1100}px`);
        }
    };

    function onPdfClick(event) {
        const rect = event.currentTarget.getBoundingClientRect();
        const x = event.clientX - rect.left;
        const y = event.clientY - rect.top;
        console.log(`Clicked coordinates: X=${x}, Y=${y}`);
        markerX.set(x);
        markerY.set(y);
    }

    const handleNext = () => {
        step.set(2);
    };

    const handleSubmit = async () => {
        step.set(3); 
        if (!userIdValue) {
            alert("User ID is not set. Please login again.");
            return;
        }

        const formData = new FormData();
        formData.append('file', file);
        formData.append('signedByEmail', email);
        formData.append('xSignature', $markerX);
        formData.append('ySignature', $markerY);
        formData.append('signatureWidth', $markerSize.toString());
        formData.append('title', title);
        formData.append('description', description);
        formData.append('userId', userIdValue);

        try {
            const response = await fetch(api_root + "/api/upload", {
                method: 'POST',
                body: formData
            });

            if (response.ok) {
                const result = await response.json();
                fileUrl.set(result.fileUrl);
                successMessage.set("File uploaded successfully!");
                console.log("Upload successful:", result);
            } else {
                const error = await response.json();
                successMessage.set(`Error: ${error.message}`);
            }
            
        } catch (error) {
            console.error("Error uploading file:", error);
            successMessage.set("Error uploading file. Please try again.");
        }
    };
</script>

{#if $step === 1}
    <div class="max-w-4xl mx-auto px-4 sm:px-6 lg:px-8 py-12">
        <h1 class="text-3xl font-bold text-center mb-6">Upload Document</h1>
        <div class="bg-white shadow-md rounded px-6 pt-6 pb-8 mb-4">
            <div class="mb-4">
                <label class="block text-gray-700 text-sm font-bold mb-2">Upload PDF</label>
                <input
                    type="file"
                    on:change={uploadFile}
                    accept="application/pdf"
                    class="shadow border rounded py-2 px-3 text-gray-700 leading-tight focus:outline-none focus:shadow-outline"
                />
            </div>
            {#if $pdfUrl}
                <div class="flex items-center justify-between">
                    <button
                        on:click={handleNext}
                        class="bg-blue-500 hover:bg-blue-700 text-white font-bold py-2 px-4 rounded focus:outline-none focus:shadow-outline"
                    >
                        Next
                    </button>
                </div>
            {/if}
        </div>
    </div>
{/if}

{#if $step === 2}
    <div class="max-w-4xl mx-auto px-4 sm:px-6 lg:px-8 py-12">
        <h1 class="text-3xl font-bold text-center mb-6">Complete the Form</h1>
        <form
            on:submit|preventDefault={handleSubmit}
            class="bg-white shadow-md rounded px-6 pt-6 pb-8 mb-4"
        >
            <div class="mb-4">
                <label for="email" class="block text-gray-700 text-sm font-bold mb-2">E-Mail of the signee</label>
                <input
                    type="text"
                    bind:value={email}
                    id="email"
                    placeholder="E-Mail"
                    class="shadow appearance-none border rounded w-full py-2 px-3 text-gray-700 leading-tight focus:outline-none focus:shadow-outline"
                    required
                />
            </div>
            <div class="mb-4">
                <label for="title" class="block text-gray-700 text-sm font-bold mb-2">Title</label>
                <input
                    type="text"
                    bind:value={title}
                    id="title"
                    placeholder="Title"
                    class="shadow appearance-none border rounded w-full py-2 px-3 text-gray-700 leading-tight focus:outline-none focus:shadow-outline"
                    required
                />
            </div>
            <div class="mb-4">
                <label for="description" class="block text-gray-700 text-sm font-bold mb-2">Description</label>
                <textarea
                    bind:value={description}
                    id="description"
                    placeholder="Description"
                    class="shadow appearance-none border rounded w-full py-2 px-3 text-gray-700 leading-tight focus:outline-none focus:shadow-outline"
                ></textarea>
            </div>

            {#if $pdfUrl}
                <div class="relative overflow-auto mb-4" style="height: {$pdfHeight};">
                    <object data={$pdfUrl} type="application/pdf" class="w-full h-full"></object>
                    <div
                        class="absolute top-0 left-0 w-full h-full cursor-crosshair"
                        on:click={onPdfClick}
                    ></div>
                    {#if $markerX !== null && $markerY !== null}
                        <div class="absolute" style="top: {$markerY}px; left: {$markerX}px;">
                            <div
                                class="bg-red-500 rounded-full"
                                style="width: {$markerSize}px; height: {$markerSize / 2}px;"
                            ></div>
                            <div
                                class="absolute bg-white p-2 border rounded shadow-md"
                                style="top: -60px; left: 0; width: 200px"
                            >
                                <input
                                    type="range"
                                    min="10"
                                    max="200"
                                    bind:value={$markerSize}
                                    class="w-full"
                                />
                            </div>
                        </div>
                    {/if}
                </div>
            {/if}

            <div class="flex items-center justify-between">
                <button
                    type="submit"
                    class="bg-blue-500 hover:bg-blue-700 text-white font-bold py-2 px-4 rounded focus:outline-none focus:shadow-outline"
                >
                    Submit
                </button>
            </div>
        </form>
    </div>
{/if}

{#if $step === 3}
    <div class="max-w-4xl mx-auto px-4 sm:px-6 lg:px-8 py-4">
        <div class="bg-green-100 border border-green-400 text-green-700 px-4 py-3 rounded relative" role="alert">
            <span class="block sm:inline">{$successMessage}</span>
        </div>
    </div>
{/if}

<style>
    object {
        width: 100%;
        min-height: 100%;
    }
</style>
