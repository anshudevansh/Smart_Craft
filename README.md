# SmartCraft - AI-Driven Smart Reply System for Emails

An intelligent browser extension that integrates seamlessly with Gmail to generate AI-powered email replies using Google's Gemini API. SmartCraft streamlines email communication by providing contextual, customizable responses directly within your Gmail interface.

## 🌟 Features

- **AI-Powered Email Replies**: Generate intelligent email responses with a single click
- **Custom Query Regeneration**: Refine responses with specific instructions or requirements
- **Tone Customization**: Adjust reply tone (professional, casual, formal, etc.)
- **Seamless Gmail Integration**: Buttons injected directly into Gmail's compose toolbar
- **Real-time Generation**: Instant AI-powered suggestions using Gemini API
- **Multiple Generation Options**: Standard replies, alternate suggestions, and query-based responses

## 🏗️ Architecture

The project consists of three main components:

1. **Chrome Extension** (Frontend)
   - Content script that injects UI elements into Gmail
   - Extracts email content and manages user interactions
   - Communicates with the backend API

2. **Spring Boot Backend** (API Server)
   - RESTful API endpoints for email generation
   - Integrates with Google Gemini API
   - Handles request processing and response formatting

3. **Google Gemini API** (AI Engine)
   - Powers the natural language generation
   - Provides contextual email responses

## 🚀 Getting Started

### Prerequisites

- Java 17 or higher
- Maven 3.6+
- Google Chrome browser
- Google Gemini API key ([Get one here](https://makersuite.google.com/app/apikey))

### Backend Setup

1. **Clone the repository**
   ```bash
   git clone https://github.com/yourusername/smart_craft.git
   cd smart_craft/backend
   ```

2. **Configure API credentials**
   ```bash
   cd src/main/resources
   cp application.properties.example application.properties
   ```
   
   Edit `application.properties` and add your Gemini API key:
   ```properties
   api.key=YOUR_ACTUAL_GEMINI_API_KEY
   ```

3. **Build and run the backend**
   ```bash
   mvn clean install
   mvn spring-boot:run
   ```

   The server will start on `http://localhost:8080`

### Chrome Extension Setup

1. **Open Chrome Extensions page**
   - Navigate to `chrome://extensions/`
   - Enable "Developer mode" (toggle in top-right corner)

2. **Load the extension**
   - Click "Load unpacked"
   - Select the `extension` folder from the project directory

3. **Verify installation**
   - The SmartCraft icon should appear in your Chrome toolbar
   - Open Gmail and compose a new email to see the AI Reply buttons

## 📖 Usage

### Generating AI Replies

1. **Open an email** in Gmail that you want to reply to
2. **Click "Reply"** to open the compose window
3. **Click "AI Reply"** button in the toolbar
4. The AI-generated response will be inserted into the compose box
5. Review and edit as needed before sending

### Regenerating with Custom Instructions

1. After generating a reply, click the **"Regenerate"** button
2. Enter specific instructions in the popup (e.g., "Make it more concise" or "Add meeting availability")
3. Click **"Generate"** to get a customized response

### Customizing Tone and Length

The backend API supports various parameters:
- `tone`: professional, casual, formal, friendly
- `lengthPreference`: short, concise, one-word
- `maxWords`: Maximum word count for the response

## 🔌 API Endpoints

### POST `/api/email/generate`
Generate a standard email reply

**Request Body:**
```json
{
  "emailContent": "Original email text...",
  "tone": "professional",
  "variant": false
}
```

**Response:** Plain text email reply

### POST `/api/email/generate-with-query`
Generate email with custom user instructions

**Request Body:**
```json
{
  "emailContent": "Original email text...",
  "userQuery": "Make it brief and suggest a meeting",
  "tone": "professional"
}
```

**Response:** Plain text email reply based on query

## 🛠️ Technologies Used

### Backend
- **Java 17** - Programming language
- **Spring Boot 3.x** - Application framework
- **Spring WebFlux** - Reactive web client for API calls
- **Lombok** - Boilerplate code reduction
- **Jackson** - JSON processing
- **Maven** - Dependency management

### Frontend
- **JavaScript (ES6+)** - Extension logic
- **HTML5 & CSS3** - UI components
- **Chrome Extension APIs** - Browser integration
- **DOM Manipulation** - Gmail interface injection

### External Services
- **Google Gemini API** - AI language model
- **Gmail Web Interface** - Email platform

## 📁 Project Structure

```
smart_craft/
├── backend/
│   ├── src/main/java/com/email/SmartCraft/
│   │   ├── Controller/
│   │   │   └── EmailGeneratorController.java
│   │   ├── Model/
│   │   │   ├── EmailRequest.java
│   │   │   └── EmailResponse.java
│   │   └── service/
│   │       └── emailGeneratorService.java
│   └── src/main/resources/
│       ├── application.properties.example
│       └── application.properties (gitignored)
├── extension/
│   ├── manifest.json
│   ├── content.js
│   ├── content.css
│   ├── index.html
│   └── icons/
└── README.md
```

## 🔒 Security Notes

- **Never commit** your `application.properties` file with actual API keys
- The `.gitignore` file is configured to exclude sensitive files
- Use `application.properties.example` as a template
- Keep your Gemini API key secure and rotate it if exposed

## 🤝 Contributing

Contributions are welcome! Please feel free to submit a Pull Request.

1. Fork the repository
2. Create your feature branch (`git checkout -b feature/AmazingFeature`)
3. Commit your changes (`git commit -m 'Add some AmazingFeature'`)
4. Push to the branch (`git push origin feature/AmazingFeature`)
5. Open a Pull Request

## 📝 License

This project is licensed under the MIT License - see the [LICENSE](LICENSE) file for details.

## 🎯 Future Enhancements

- [ ] Support for multiple email providers (Outlook, Yahoo)
- [ ] Email templates and saved responses
- [ ] Multi-language support
- [ ] Sentiment analysis for incoming emails
- [ ] Email categorization and priority detection
- [ ] Scheduled email suggestions
- [ ] Analytics dashboard for email patterns

## 👨‍💻 Author

Your Name - [Your GitHub Profile](https://github.com/yourusername)

## 🙏 Acknowledgments

- Google Gemini API for powering the AI responses
- Spring Boot community for excellent documentation
- Chrome Extensions documentation and examples

---

**Note**: This project is for educational and productivity purposes. Always review AI-generated content before sending emails.
