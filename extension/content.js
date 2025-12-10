console.log("✅ Content script loaded");

// --- Create AI Reply Button ---
function createAIButton(label = 'AI Reply', tooltip = 'Generate AI Reply', className = 'ai-reply-button') {
    const button = document.createElement('div');
    button.className = `T-I J-J5-Ji aoO v7 T-I-atl L3 ${className}`;
    button.style.marginRight = '8px';
    button.innerHTML = label;
    button.setAttribute('role', 'button');
    button.setAttribute('data-tooltip', tooltip);
    return button;
}

// --- Extract Email Content ---
function getEmailContent() {
    const selectors = ['.h7', '.a3s.aiL', '.gmail_quote', '[role="presentation"]'];
    for (const selector of selectors) {
        const content = document.querySelector(selector);
        if (content && content.innerText.trim()) {
            return content.innerText.trim();
        }
    }
    return '';
}

// --- Find Toolbar ---
function findComposeToolbar() {
    const selectors = ['.btC', '.aDh', '[role="toolbar"]', '.gU.Up'];
    for (const selector of selectors) {
        const toolbar = document.querySelector(selector);
        if (toolbar) return toolbar;
    }
    return null;
}

// --- Generate Reply Function (used by AI Reply button) ---
async function generateReply(button, mode = "normal") {
    try {
        button.innerHTML = 'Generating...';
        button.disabled = true;

        const emailContent = getEmailContent();
        if (!emailContent) {
            alert("No email content found.");
            return;
        }

        const response = await fetch('http://localhost:8080/api/email/generate', {
            method: 'POST',
            headers: { 'Content-Type': 'application/json' },
            body: JSON.stringify({
                emailContent: emailContent,
                tone: "professional",
                variant: mode === "regenerate" ? true : false
            })
        });

        if (!response.ok) throw new Error('API Request Failed');

        const generatedReply = await response.text();
        const composeBox = document.querySelector('[role="textbox"][g_editable="true"]');

        if (composeBox) {
            composeBox.focus();
            document.execCommand('selectAll', false, null);
            document.execCommand('insertText', false, generatedReply);
        } else {
            console.error('Compose box was not found');
        }
    } catch (error) {
        console.error(error);
        alert('⚠️ Failed to generate reply');
    } finally {
        button.innerHTML = mode === "regenerate" ? 'Regenerate' : 'AI Reply';
        button.disabled = false;
    }
}

// --- Show Query Popup (for Regenerate button) ---
function showQueryPopup() {
    // Remove existing popup if any
    const existingPopup = document.getElementById('ai-query-popup-overlay');
    if (existingPopup) {
        existingPopup.remove();
    }

    // Create overlay
    const overlay = document.createElement('div');
    overlay.id = 'ai-query-popup-overlay';
    overlay.style.cssText = `
        position: fixed;
        top: 0;
        left: 0;
        width: 100%;
        height: 100%;
        background: rgba(0, 0, 0, 0.5);
        z-index: 10000;
        display: flex;
        align-items: center;
        justify-content: center;
    `;

    // Create popup
    const popup = document.createElement('div');
    popup.style.cssText = `
        background: white;
        border-radius: 12px;
        padding: 24px;
        width: 400px;
        box-shadow: 0 8px 32px rgba(0, 0, 0, 0.2);
        font-family: 'Inter', -apple-system, BlinkMacSystemFont, 'Segoe UI', sans-serif;
    `;

    popup.innerHTML = `
        <h3 style="margin: 0 0 16px 0; font-size: 18px; font-weight: 600; color: #1f2937;">
            ✨ Regenerate with Custom Query
        </h3>
        <label style="display: block; font-size: 13px; font-weight: 500; color: #374151; margin-bottom: 8px;">
            Enter your query or keywords:
        </label>
        <textarea 
            id="ai-query-input" 
            placeholder="E.g., 'Make it brief and polite' or 'suggest meeting tomorrow'"
            style="
                width: 100%;
                padding: 12px;
                border: 2px solid #e5e7eb;
                border-radius: 8px;
                font-family: 'Inter', sans-serif;
                font-size: 14px;
                resize: vertical;
                min-height: 80px;
                box-sizing: border-box;
            "
        ></textarea>
        <div style="display: flex; gap: 12px; margin-top: 16px;">
            <button 
                id="ai-query-cancel" 
                style="
                    flex: 1;
                    padding: 12px;
                    background: #f3f4f6;
                    color: #374151;
                    border: none;
                    border-radius: 8px;
                    font-weight: 600;
                    font-size: 14px;
                    cursor: pointer;
                "
            >
                Cancel
            </button>
            <button 
                id="ai-query-submit" 
                style="
                    flex: 1;
                    padding: 12px;
                    background: linear-gradient(135deg, #667eea 0%, #764ba2 100%);
                    color: white;
                    border: none;
                    border-radius: 8px;
                    font-weight: 600;
                    font-size: 14px;
                    cursor: pointer;
                "
            >
                Generate
            </button>
        </div>
    `;

    overlay.appendChild(popup);
    document.body.appendChild(overlay);

    // Focus on input
    const input = document.getElementById('ai-query-input');
    input.focus();

    // Close popup
    const closePopup = () => {
        overlay.remove();
    };

    // Cancel button
    document.getElementById('ai-query-cancel').addEventListener('click', closePopup);

    // Click outside to close
    overlay.addEventListener('click', (e) => {
        if (e.target === overlay) {
            closePopup();
        }
    });

    // Submit button
    document.getElementById('ai-query-submit').addEventListener('click', async () => {
        const userQuery = input.value.trim();

        if (!userQuery) {
            alert('Please enter a query');
            return;
        }

        const emailContent = getEmailContent();
        if (!emailContent) {
            alert('No email content found');
            closePopup();
            return;
        }

        // Show loading state
        const submitBtn = document.getElementById('ai-query-submit');
        submitBtn.textContent = 'Generating...';
        submitBtn.disabled = true;

        try {
            const response = await fetch('http://localhost:8080/api/email/generate-with-query', {
                method: 'POST',
                headers: { 'Content-Type': 'application/json' },
                body: JSON.stringify({
                    emailContent: emailContent,
                    userQuery: userQuery,
                    tone: 'professional'
                })
            });

            if (!response.ok) throw new Error('API Request Failed');

            const generatedReply = await response.text();
            const composeBox = document.querySelector('[role="textbox"][g_editable="true"]');

            if (composeBox) {
                composeBox.focus();
                document.execCommand('selectAll', false, null);
                document.execCommand('insertText', false, generatedReply);
                closePopup();
            } else {
                alert('Compose box not found');
            }
        } catch (error) {
            console.error(error);
            alert('⚠️ Failed to generate reply');
            submitBtn.textContent = 'Generate';
            submitBtn.disabled = false;
        }
    });

    // Enter key to submit
    input.addEventListener('keydown', (e) => {
        if (e.key === 'Enter' && !e.shiftKey) {
            e.preventDefault();
            document.getElementById('ai-query-submit').click();
        }
    });
}

// --- Inject Buttons ---
function injectButtons() {
    const existingAI = document.querySelector('.ai-reply-button');
    const existingRegen = document.querySelector('.ai-regenerate-button');
    if (existingAI) existingAI.remove();
    if (existingRegen) existingRegen.remove();

    const toolbar = findComposeToolbar();
    if (!toolbar) {
        console.log("❌ Toolbar not found");
        return;
    }

    console.log("✅ Toolbar found, injecting buttons");

    // Create both buttons
    const aiButton = createAIButton('AI Reply', 'Generate AI Reply', 'ai-reply-button');
    const regenButton = createAIButton('Regenerate', 'Regenerate AI Reply', 'ai-regenerate-button');

    // Add listeners
    aiButton.addEventListener('click', () => generateReply(aiButton, "normal"));

    // Regenerate button opens a popup for user query
    regenButton.addEventListener('click', () => {
        showQueryPopup();
    });

    // Insert buttons beside Send
    toolbar.insertBefore(regenButton, toolbar.firstChild);
    toolbar.insertBefore(aiButton, toolbar.firstChild);
}

// --- Observe Compose Windows ---
const observer = new MutationObserver((mutations) => {
    for (const mutation of mutations) {
        const addedNodes = Array.from(mutation.addedNodes);
        const hasComposeElements = addedNodes.some(node =>
            node.nodeType === Node.ELEMENT_NODE &&
            (node.matches('.aDh, .btC, [role="dialog"]') ||
                node.querySelector('.aDh, .btC, [role="dialog"]'))
        );

        if (hasComposeElements) {
            console.log("🪄 Compose Window Detected — Injecting Buttons");
            setTimeout(injectButtons, 600);
        }
    }
});

observer.observe(document.body, { childList: true, subtree: true });
console.log("🔍 Observing for compose windows");

// --- Message Listener for Popup Communication ---
chrome.runtime.onMessage.addListener((request, sender, sendResponse) => {
    console.log("📨 Message received:", request);

    if (request.action === 'getEmailContent') {
        // Get email content from Gmail
        const emailContent = getEmailContent();
        console.log("📧 Email content:", emailContent ? "Found" : "Not found");
        sendResponse({ emailContent: emailContent });
        return true;
    }

    if (request.action === 'insertEmail') {
        // Insert generated email into compose box
        console.log("📨 insertEmail action received");
        console.log("📧 Email content type:", typeof request.emailContent);
        console.log("📧 Email content:", request.emailContent);

        const composeBox = document.querySelector('[role="textbox"][g_editable="true"]');

        if (composeBox && request.emailContent) {
            composeBox.focus();
            document.execCommand('selectAll', false, null);
            document.execCommand('insertText', false, request.emailContent);
            console.log("✅ Email inserted into compose box");
            sendResponse({ success: true });
        } else {
            console.error("❌ Compose box not found or no email content");
            console.error("❌ Compose box exists:", !!composeBox);
            console.error("❌ Email content exists:", !!request.emailContent);
            sendResponse({ success: false, error: 'Compose box not found' });
        }
        return true;
    }

    return false;
});