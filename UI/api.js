import { showMessage, showDashboard, resetUI } from './ui.js';

const API_URL = 'http://localhost:8080/api';
let jwtToken = null;


//  Signup API

async function handleSignUp(event) {
    event.preventDefault();

    // step1: collect input
    const name = document.getElementById('signup-name').value;
    const email = document.getElementById('signup-email').value;
    const password = document.getElementById('signup-password').value;

    try {
        // step2: call signup API
        const response = await fetch(`${API_URL}/signup`, {
            method: 'POST',
            headers: { 'Content-Type': 'application/json' },
            body: JSON.stringify({ name, email, password })
        });

        // step3: handle response
        const resultText = await response.text();
        if (!response.ok) throw new Error(resultText);

        // step4: success UI
        showMessage('Registration successful! Please log in.', false);
        document.getElementById('signup-form').reset();
        document.getElementById('login-tab-btn').click();
    } catch (error) {
        // step5: error UI
        showMessage(error.message, true);
    }
}


//  Login API

async function handleLogin(event) {
    event.preventDefault();

    // step1: collect input
    const email = document.getElementById('login-email').value;
    const password = document.getElementById('login-password').value;

    try {
        // step2: call login API
        const response = await fetch(`${API_URL}/login`, {
            method: 'POST',
            headers: { 'Content-Type': 'application/json' },
            body: JSON.stringify({ email, password })
        });

        // step3: handle response
        const result = await response.json();
        if (!response.ok) throw new Error(result.error || 'Login failed');

        // step4: save jwt + show dashboard
        jwtToken = result.jwt;
        showDashboard(email, result.jwt);
    } catch (error) {
        // step5: error UI
        showMessage(error.message, true);
    }
}


//  Secure API

async function handleFetchSecretMessage() {
    const secretArea = document.getElementById('secret-message-area');
    secretArea.classList.remove('hidden');
    secretArea.textContent = 'Fetching...';

    // step1: check token
    if (!jwtToken) {
        secretArea.textContent = 'Error: You are not logged in.';
        return;
    }

    try {
        // step2: call protected API
        const response = await fetch(`${API_URL}/secure/message`, {
            method: 'GET',
            headers: { 'Authorization': `Bearer ${jwtToken}` }
        });

        // step3: handle response
        const resultText = await response.text();
        if (!response.ok) throw new Error(resultText);

        // step4: show secret
        secretArea.textContent = `Server says: "${resultText}"`;
    } catch (error) {
        // step5: error UI
        secretArea.textContent = `Error: ${error.message}`;
    }
}


//  Logout

function handleLogout() {
    // step1: clear token
    jwtToken = null;

    // step2: reset UI
    resetUI();
}


//  Initialize API

export function initAPI() {
    // step1: signup form
    document.getElementById('signup-form').addEventListener('submit', handleSignUp);

    // step2: login form
    document.getElementById('login-form').addEventListener('submit', handleLogin);

    // step3: fetch secret btn
    document.getElementById('fetch-secret-btn').addEventListener('click', handleFetchSecretMessage);

    // step4: logout btn
    document.getElementById('logout-btn').addEventListener('click', handleLogout);
}
