import { initializeApp } from "https://www.gstatic.com/firebasejs/11.0.2/firebase-app.js";
import { getAuth, signOut } from "https://www.gstatic.com/firebasejs/11.0.2/firebase-auth.js";
import { onAuthStateChanged } from "https://www.gstatic.com/firebasejs/11.0.2/firebase-auth.js";


// Your Firebase configuration
const firebaseConfig = {
    apiKey: "YOUR_API_KEY",
    authDomain: "YOUR_AUTH_DOMAIN",
    projectId: "YOUR_PROJECT_ID",
    storageBucket: "YOUR_STORAGE_BUCKET",
    messagingSenderId: "YOUR_MESSAGING_SENDER_ID",
    appId: "YOUR_APP_ID",
};

// Initialize Firebase
const app = initializeApp(firebaseConfig);

// Get Firebase Auth instance
const auth = getAuth(app);

document.getElementById("LogoutButton").addEventListener("click", () => {
    signOut(auth)
      .then(() => {
        console.log("User logged out");
        // Clear email from localStorage
        localStorage.removeItem("userEmail");
        // Optionally update the UI or redirect after logout
        document.getElementById("emailInput").value = "Perhaps log in?";
        setTimeout(() => {
          window.location.href = "index.html"; // Redirect to login page
        }, 500);
      })
      .catch((error) => {
        console.error("Error logging out:", error.message);
      });
  });
  
