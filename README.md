BeastXFitness AI App
AI-Powered Fitness & Wellness Platform
BeastXFitness is a comprehensive AI-powered fitness and wellness application designed to help users achieve their health goals. It provides tools for tracking personal records, generating personalized recommendations, and leveraging AI for insights.
The application features a robust Spring Boot Java backend and a dynamic React/TypeScript frontend, with the AI Chatbot powered by Botpress.
Features
Core Features:
User Authentication & Authorization: Secure user registration, login, and protected routes using JWT (JSON Web Tokens).

Personal Records Tracking: Log and view personal bests and workout progress.

Progress Entries: Track fitness journey progress over time.

Workout Management: Create and manage custom workout routines.

AI & Recommendation Features:
AI Chatbot: An integrated chatbot for fitness-related queries or personalized advice, powered by Botpress.

BMI/BMR Calculators: Tools to calculate Body Mass Index and Basal Metabolic Rate.

Calorie Recommendations: Generate personalized daily calorie intake recommendations.

Fitness Recommendations: AI-driven suggestions for workouts, nutrition, or general fitness tips.

Technologies Used
Backend Technologies:
Java: Core programming language.

Spring Boot: Framework for building the RESTful API and managing application logic.

Spring Security: For robust authentication and authorization, integrated with JWT.

JWT (JSON Web Tokens): For secure stateless authentication between frontend and backend.

Maven: Project management and build automation tool.

MongoDB Atlas: Cloud-based NoSQL database for flexible and scalable data storage.

Spring Data MongoDB: Spring's module for seamless integration with MongoDB.

Botpress Integration: Backend potentially interacts with Botpress for advanced bot logic or webhook handling.

LLM Integration (Optional/Implied): If the AI Chatbot or Recommendations use an external Large Language Model through Botpress or directly (e.g., Google Gemini API, OpenAI API).

Frontend Technologies:
React: JavaScript library for building user interfaces.

TypeScript: Typed superset of JavaScript that compiles to plain JavaScript, providing better tooling and code quality.

Axios: Promise-based HTTP client for making requests to the backend API.

React Router: For declarative routing in the single-page application.

Botpress Webchat Client: For embedding the Botpress chatbot interface.

State Management: (e.g., React Context API, Redux, Zustand, etc. - specify if used)

UI Library (Optional): (e.g., Tailwind CSS, Material UI, Ant Design, Shadcn UI - specify if used)

Getting Started
Follow these instructions to set up and run the BeastXFitness AI App on your local machine.

Prerequisites
Ensure you have the following installed:

Java Development Kit (JDK) 11 or higher

Maven

Node.js (LTS version recommended)

npm (Node Package Manager, comes with Node.js) or Yarn

Git

MongoDB Atlas Account: A free-tier (M0 Sandbox) cluster is sufficient for development.

Botpress Account/Instance: Access to a Botpress Cloud instance or a locally running Botpress server.

IDE (Recommended): IntelliJ IDEA (for backend) and VS Code (for frontend)

Backend Setup (backend folder)
Navigate to the backend directory:

cd backend

MongoDB Atlas Configuration:

Log in to your MongoDB Atlas account.

Create a new Free Tier (M0 Sandbox) cluster.

Create a Database User with read and write to any database privileges. Note down the username and password.

Add your current IP address to the IP Access List (or allow access from anywhere for development purposes - 0.0.0.0/0).

Get your Connection String: Go to Connect -> Connect your application -> Java Driver and copy the URI.

Update application.properties:
Open backend/src/main/resources/application.properties and configure your MongoDB Atlas URI and JWT secret:

spring.data.mongodb.uri=mongodb+srv://<your_atlas_user>:<your_atlas_password>@<your_cluster_url>/beastx_db?retryWrites=true&w=majority

# JWT Configuration
jwt.secret=YOUR_SUPER_SECRET_KEY_GOES_HERE_MAKE_IT_LONG_AND_RANDOM # !! IMPORTANT: CHANGE THIS IN PRODUCTION !!
jwt.expiration=86400000 # 24 hours in milliseconds

# Botpress Configuration (if applicable, e.g., for API keys or webhook URLs)
# botpress.api.token=YOUR_BOTPRESS_API_TOKEN
# botpress.webhook.url=YOUR_BOTPRESS_WEBHOOK_URL

Replace <your_atlas_user>, <your_atlas_password>, and <your_cluster_url> with your actual MongoDB Atlas credentials.

Replace YOUR_SUPER_SECRET_KEY_GOES_HERE_MAKE_IT_LONG_AND_RANDOM with a strong, random key.

Build the Backend:
From the backend directory, run:

mvn clean install

Frontend Setup (frontend folder)
Navigate to the frontend directory:

cd ../frontend # To go from 'backend' back to 'hey' then into 'frontend'
# OR if you are in 'hey':
# cd frontend

Install Node.js dependencies:

npm install
# OR if you use Yarn:
# yarn install

Configure API URL and Botpress Webchat:
If your frontend needs to know the backend's URL (e.g., http://localhost:8080), ensure it's configured correctly. Also, include the Botpress Webchat script.
Example .env file in frontend directory:

REACT_APP_API_URL=http://localhost:8080/api
REACT_APP_BOTPRESS_SERVER_URL=https://cdn.botpress.cloud/webchat/v1/inject.js # Or your custom Botpress server URL
REACT_APP_BOTPRESS_BOT_ID=YOUR_BOTPRESS_BOT_ID
REACT_APP_BOTPRESS_CLIENT_ID=YOUR_BOTPRESS_CLIENT_ID

You will typically embed the Botpress webchat script in your public/index.html or in a top-level React component. Refer to Botpress documentation for the exact embedding code. It often looks like this in index.html (after <body> tag):

<script src="https://cdn.botpress.cloud/webchat/v1/inject.js"></script>
<script src="https://mediafiles.botpress.cloud/YOUR_BOT_ID/webchat/v1/inject.js"></script>
<script>
  window.botpressWebChat.init({
      "composerPlaceholder": "Chat with the AI Fitness Coach",
      "botConversationDescription": "Your personal AI fitness assistant.",
      "botId": "YOUR_BOTPRESS_BOT_ID",
      "hostUrl": "https://cdn.botpress.cloud/webchat/v1",
      "messagingUrl": "https://messaging.botpress.cloud",
      "clientId": "YOUR_BOTPRESS_CLIENT_ID",
      "webhookId": "YOUR_BOTPRESS_WEBHOOK_ID",
      "enableConversationDeletion": true,
      "chatId": "YOUR_UNIQUE_CHAT_ID", // Optional: For persistent chat sessions
      "lazySocket": true,
      "themeName": "prism",
      "botName": "BeastXFitness AI Coach",
      "avatarUrl": "https://placehold.co/40x40/000000/FFFFFF?text=AI",
      "stylesheet": "https://webchat-config.botpress.cloud/YOUR_BOT_ID/webchat.css",
      "appearance": {
          "leftBotNavText": "BeastXFitness AI Coach"
      },
      "startSilent": false,
      "showUserName": false,
      "layout": "standard",
      "theme": "light",
      "disableAnimations": false,
      "enableTranscriptDownload": false,
      "shadows": true,
      "frontendVersion": "v1",
      "showPoweredBy": true,
      "disableButton": false
  });
</script>

Running the Application
Start the Backend:
Open a new terminal, navigate to the backend directory (cd C:\hey\backend), and run:

mvn spring-boot:run

The backend should start on http://localhost:8080 (or configured port).

Start the Frontend:
Open another new terminal, navigate to the frontend directory (cd C:\hey\frontend), and run:

npm start
# OR
# yarn start

The frontend application will typically open in your web browser at http://localhost:3000 (or another available port).

Ensure Botpress Service is Running:
If you are running a local Botpress instance, make sure it is active. If using Botpress Cloud, ensure your bot is published and accessible.

You should now be able to interact with the BeastXFitness AI App, including the Botpress-powered chatbot!

API Endpoints (Backend - http://localhost:8080/api)
(This is a general list; adapt based on your actual controller definitions)

Authentication
POST /api/auth/register: Register a new user.

Body: { "username": "string", "password": "string" }

POST /api/auth/login: Authenticate a user and receive a JWT.

Body: { "username": "string", "password": "string" }

Response: { "token": "your.jwt.here", "username": "string" }

Journal Entries (Protected - Requires Authorization: Bearer <JWT>)
POST /api/journal: Create a new journal entry.

Body: { "title": "string", "content": "string" }

GET /api/journal: Get all journal entries for the authenticated user.

GET /api/journal/{id}: Get a specific journal entry by ID.

PUT /api/journal/{id}: Update a specific journal entry by ID.

Body: { "title": "string", "content": "string" }

DELETE /api/journal/{id}: Delete a specific journal entry by ID.

Fitness/AI Features (Protected - Requires Authorization: Bearer <JWT>)
POST /api/bmi/calculate: Calculate BMI.

Body: { "weightKg": double, "heightCm": double }

POST /api/bmr/calculate: Calculate BMR.

Body: { "gender": "MALE/FEMALE", "weightKg": double, "heightCm": double, "age": int }

GET /api/recommendations/calories: Get personalized calorie recommendations.

POST /api/chatbot/ask: Interact with the AI chatbot.

Body: { "question": "string" }

Project Structure
hey/
├── backend/                  # Spring Boot Java Backend
│   ├── src/
│   │   ├── main/
│   │   │   ├── java/
│   │   │   │   └── com/beastxfit/
│   │   │   │       ├── BeastXFitApplication.java # Main application class
│   │   │   │       ├── config/                   # Spring Security, JWT, and other configurations
│   │   │   │       ├── controller/               # REST API controllers
│   │   │   │       ├── dto/                      # Data Transfer Objects
│   │   │   │       ├── exception/                # Custom exceptions
│   │   │   │       ├── model/                    # MongoDB document models (e.g., User, JournalEntry, Workout)
│   │   │   │       ├── repository/               # Spring Data MongoDB repositories
│   │   │   │       └── service/                  # Business logic and AI integrations
│   │   │   └── resources/
│   │   │       └── application.properties        # Backend configuration
│   ├── pom.xml               # Maven build file
│   └── ...                   # Other backend files
├── frontend/                 # React/TypeScript Frontend
│   ├── public/               # Static assets (e.g., index.html where Botpress script might be embedded)
│   ├── src/
│   │   ├── components/       # Reusable UI components (e.g., Header, Footer)
│   │   ├── pages/            # Main application views (e.g., Homepage.tsx, Login.tsx, Dashboard.tsx)
│   │   ├── services/         # API interaction logic (e.g., authService.ts, journalService.ts)
│   │   ├── context/          # React Contexts (e.g., UserContext.tsx)
│   │   ├── App.tsx           # Main React component
│   │   └── index.tsx         # Entry point for React app
│   ├── package.json          # Node.js project dependencies
│   ├── tsconfig.json         # TypeScript configuration
│   └── .env                  # Environment variables (for backend API URL, Botpress IDs)
│   └── ...                   # Other frontend files
└── README.md                 # This Readme file

License
This project is open-sourced under the MIT License. See the LICENSE file in the root of the repository for more details.

Contact
If you have any questions, feedback, or need further assistance, feel free to reach out!

Your Name - your.email@example.com

Project Link: https://github.com/YOUR_GITHUB_USERNAME/HeyProject
