const authContainer = document.getElementById('auth-container');
const dashboardContainer = document.getElementById('dashboard-container');
const messageArea = document.getElementById('message-area');
const loginForm = document.getElementById('login-form');
const signupForm = document.getElementById('signup-form');
const loginTabBtn = document.getElementById('login-tab-btn');
const signupTabBtn = document.getElementById('signup-tab-btn');

// Switch tabs
function switchTab(tab) {
    hideMessage();
    if (tab === 'login') {
        loginForm.classList.remove('hidden');
        signupForm.classList.add('hidden');
        loginTabBtn.classList.replace('bg-slate-200', 'bg-sky-600');
        loginTabBtn.classList.replace('text-slate-600', 'text-white');
        signupTabBtn.classList.replace('bg-sky-600', 'bg-slate-200');
        signupTabBtn.classList.replace('text-white', 'text-slate-600');
    } else {
        loginForm.classList.add('hidden');
        signupForm.classList.remove('hidden');
        signupTabBtn.classList.replace('bg-slate-200', 'bg-sky-600');
        signupTabBtn.classList.replace('text-slate-600', 'text-white');
        loginTabBtn.classList.replace('bg-sky-600', 'bg-slate-200');
        loginTabBtn.classList.replace('text-white', 'text-slate-600');
    }
}

// Show message
function showMessage(message, isError = false) {
    messageArea.textContent = message;
    messageArea.className = `mb-4 text-sm font-medium p-3 rounded-md ${isError ? 'bg-rose-100 text-rose-700' : 'bg-green-100 text-green-700'}`;
    messageArea.classList.remove('hidden');
}

// Hide message
function hideMessage() {
    messageArea.classList.add('hidden');
}

// Show dashboard
function showDashboard(email, token) {
    authContainer.classList.add('hidden');
    dashboardContainer.classList.remove('hidden');
    document.getElementById('user-email').textContent = email;
    document.getElementById('jwt-token').textContent = token;
}

// Reset UI
function resetUI() {
    dashboardContainer.classList.add('hidden');
    authContainer.classList.remove('hidden');
    loginForm.reset();
    signupForm.reset();
    hideMessage();
    document.getElementById('secret-message-area').classList.add('hidden');
}

// Initialize UI
export function initUI() {
    loginTabBtn.addEventListener('click', () => switchTab('login'));
    signupTabBtn.addEventListener('click', () => switchTab('signup'));
}

export { showMessage, showDashboard, resetUI };