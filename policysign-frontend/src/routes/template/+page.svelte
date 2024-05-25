<script>
    import { writable } from "svelte/store";
    import { get, derived } from 'svelte/store';
    import { userId } from '../../auth.service';
    import { onMount, onDestroy } from 'svelte';

    const api_root = "http://localhost:8080";

    // Step states
    const step = writable(1);
    const pdfUrl = writable('');
    const pdfHeight = writable('500px');
    const markerX = writable(null);
    const markerY = writable(null);
    const markerSize = writable(50);
    const successMessage = writable('');

    // Form fields
    let file;
    let fileInput;
    let title = '';
    let description = '';
    let errorMessage = '';
    let showMarker = false;

    // Load userId
    let userIdValue;
    onMount(() => {
        userIdValue = get(userId);
        if (!userIdValue) {
            window.location.href = "/login";
        }
    });

    // Load PDF.js library
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

    // Handle file upload and preview
    const uploadFile = async (event) => {
        const files = event.target.files;
        if (files.length > 0) {
            file = files[0];
            const objectUrl = URL.createObjectURL(file);
            pdfUrl.set(objectUrl);

            await loadPdfJs();
            const pdfDoc = await pdfjsLib.getDocument(objectUrl).promise;
            const page = await pdfDoc.getPage(1);
            const viewport = page.getViewport({ scale: 1 });
            pdfHeight.set(`${viewport.height}px`);
        }
    };

    // Handle click to place marker
    function onPdfClick(event) {
        const rect = event.currentTarget.getBoundingClientRect();
        const x = event.clientX - rect.left;
        const y = event.clientY - rect.top;

        // Actual dimensions of an A4 page in points
        const actualWidth = 595.28;
        const actualHeight = 841.89;

        // Displayed dimensions of the PDF container
        const displayedWidth = rect.width;
        const displayedHeight = rect.height;

        // Calculate the scaling factors
        const scaleX = actualWidth / displayedWidth;
        const scaleY = actualHeight / displayedHeight;

        // Adjust the click coordinates
        const adjustedX = x * scaleX;
        const adjustedY = y * scaleY;

        // Ensure coordinates stay within the boundaries of the actual PDF dimensions
        const clampedX = Math.min(Math.max(adjustedX, 0), actualWidth);
        const clampedY = Math.min(Math.max(adjustedY, 0), actualHeight);

        markerX.set(clampedX);
        markerY.set(clampedY);
    }

    // Upload the template
    async function uploadTemplate() {
        if (!file || !title || !description || get(markerX) === null || get(markerY) === null) {
            errorMessage = "All fields are required";
            return;
        }

        const formData = new FormData();
        formData.append("file", file);
        formData.append("title", title);
        formData.append("description", description);
        formData.append("xSignature", get(markerX));
        formData.append("ySignature", get(markerY));
        formData.append("signatureWidth", $markerSize.toString());
        formData.append("userId", userIdValue);

        try {
            const response = await fetch(`${api_root}/api/templates`, {
                method: 'POST',
                body: formData
            });

            if (response.ok) {
                successMessage.set("Template uploaded successfully");
                errorMessage = '';
                await new Promise(resolve => setTimeout(resolve, 2000));
                window
            } else {
                errorMessage = "Failed to upload template";
            }
        } catch (error) {
            console.error("Error uploading template:", error);
            errorMessage = "Error uploading template";
        }
    }

    // Clean up the object URL
    onDestroy(() => {
        if (fileUrl) {
            URL.revokeObjectURL(fileUrl);
        }
    });

    // Handle next step
    const handleNext = () => {
        step.set(2);
    };

    // Handle form submission
    const handleSubmit = async () => {
        await uploadTemplate();
        step.set(3);
    };
</script>

<!-- Step 1: Upload PDF -->
{#if $step === 1}
    <div class="max-w-4xl mx-auto px-4 sm:px-6 lg:px-8 py-12">
        <h1 class="text-3xl font-bold text-center mb-6">Upload Template</h1>
        <div class="bg-white shadow-md rounded px-6 pt-6 pb-8 mb-4">
            <div class="mb-4">
                <label class="block text-gray-700 text-sm font-bold mb-2">Upload PDF</label>
                <input
                    type="file"
                    bind:this={fileInput}
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

<!-- Step 2: Complete the Form -->
{#if $step === 2}
    <div class="max-w-4xl mx-auto px-4 sm:px-6 lg:px-8 py-12">
        <h1 class="text-3xl font-bold text-center mb-6">Complete the Form</h1>
        <form
            on:submit|preventDefault={handleSubmit}
            class="bg-white shadow-md rounded px-6 pt-6 pb-8 mb-4"
        >
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

<!-- Step 3: Success Message -->
{#if $step === 3}
    <div class="max-w-4xl mx-auto px-4 sm:px-6 lg:px-8 py-4">
        <div class="bg-green-100 border border-green-400 text-green-700 px-4 py-3 rounded relative" role="alert">
            <span class="block sm:inline">{$successMessage}</span>
        </div>
    </div>
{/if}

<style>
    .absolute {
        position: absolute;
    }
    .relative {
        position: relative;
    }
    object {
        width: 600px;
    }
</style>
