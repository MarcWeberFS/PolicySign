<script>
    import { onMount } from "svelte";
    import { writable } from "svelte/store";

    const api_root = "https://policysign.azurewebsites.net";

    // Store for the PDF URL
    let pdfUrl = writable("");

    // Store for the signature data URL
    let signatureDataUrl = writable("");

    // Canvas element and context
    let canvas;
    let context;

    // Get the document ID from the URL parameters
    let params = new URLSearchParams(window.location.search);
    let id = params.get("id");

    onMount(async () => {
        if (id) {
            const response = await fetch(`${api_root}/signature/${id}`);
            if (response.ok) {
                const blob = await response.blob();
                const objectUrl = URL.createObjectURL(blob);
                pdfUrl.set(objectUrl);
            } else {
                console.error("Failed to load the document.");
            }
            initCanvas();
        } else {
            console.error("Document ID is missing in the URL parameters.");
        }
    });

    const initCanvas = () => {
        canvas = document.getElementById("signatureCanvas");
        context = canvas.getContext("2d");

        canvas.addEventListener("mousedown", startDrawing);
        canvas.addEventListener("mouseup", stopDrawing);
        canvas.addEventListener("mousemove", draw);
    };

    let drawing = false;

    const startDrawing = (e) => {
        drawing = true;
        draw(e);
    };

    const stopDrawing = () => {
        drawing = false;
        context.beginPath();
    };

    const draw = (e) => {
        if (!drawing) return;
        context.lineWidth = 2;
        context.lineCap = "round";
        context.strokeStyle = "black";

        context.lineTo(e.clientX - canvas.offsetLeft, e.clientY - canvas.offsetTop);
        context.stroke();
        context.beginPath();
        context.moveTo(e.clientX - canvas.offsetLeft, e.clientY - canvas.offsetTop);
    };

    const saveSignature = async () => {
        const dataUrl = canvas.toDataURL("image/png");
        const payload = {
            signatureDataUrl: dataUrl
        };
        const response = await fetch(`${api_root}/signature/${id}`, {
            method: 'POST',
            headers: {
                'Content-Type': 'application/json'
            },
            body: JSON.stringify(payload)
        });

        if (response.ok) {
            alert("Signature saved successfully!");
        } else {
            alert("Failed to save signature.");
        }
    };
</script>

<style>
    canvas {
        border: 1px solid #000;
    }
</style>

<div class="container">
    {#if $pdfUrl}
        <object data={$pdfUrl} type="application/pdf" width="100%" height="500px"></object>
    {/if}
    <canvas id="signatureCanvas" width="600" height="300"></canvas>
    <button on:click={saveSignature}>Save Signature</button>
</div>
