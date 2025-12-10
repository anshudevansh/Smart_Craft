# 🚀 Git Setup and GitHub Upload Guide

This guide will help you initialize your Git repository and upload your SmartCraft project to GitHub.

## Prerequisites

- Git installed on your system ([Download Git](https://git-scm.com/downloads))
- GitHub account ([Sign up](https://github.com/join))
- Your API key safely stored (it's already in `application.properties` which is gitignored)

## Step-by-Step Instructions

### 1. Initialize Git Repository

Open your terminal in the project root directory (`d:\work\smart_craft`) and run:

```bash
# Initialize git repository
git init

# Add all files (sensitive files are excluded by .gitignore)
git add .

# Verify that application.properties is NOT staged
git status
```

**Important**: Check the output of `git status`. You should **NOT** see `backend/src/main/resources/application.properties` in the list. If you do, stop and check your `.gitignore` file.

### 2. Create Initial Commit

```bash
# Create your first commit
git commit -m "Initial commit: SmartCraft AI Email Generator"
```

### 3. Create GitHub Repository

1. Go to [GitHub](https://github.com) and log in
2. Click the **"+"** icon in the top-right corner
3. Select **"New repository"**
4. Fill in the details:
   - **Repository name**: `smart_craft` or `smartcraft-ai-email-generator`
   - **Description**: "AI-driven smart reply system for Gmail using Gemini API"
   - **Visibility**: Choose Public or Private
   - **DO NOT** initialize with README, .gitignore, or license (we already have these)
5. Click **"Create repository"**

### 4. Connect Local Repository to GitHub

GitHub will show you commands. Use these (replace `yourusername` with your actual GitHub username):

```bash
# Add remote repository
git remote add origin https://github.com/yourusername/smart_craft.git

# Rename branch to main (if needed)
git branch -M main

# Push to GitHub
git push -u origin main
```

### 5. Verify Upload

1. Refresh your GitHub repository page
2. Verify all files are uploaded
3. **Double-check**: Open `backend/src/main/resources/` on GitHub and confirm that `application.properties` is **NOT** there
4. You should see `application.properties.example` instead

## ✅ Security Checklist

Before pushing, verify:

- [ ] `application.properties` is listed in `.gitignore`
- [ ] `application.properties` does NOT appear in `git status`
- [ ] `application.properties.example` exists with placeholder values
- [ ] No API keys visible in any committed files
- [ ] README.md has clear setup instructions

## 📝 Future Updates

When making changes to your project:

```bash
# Check what changed
git status

# Add specific files or all changes
git add .

# Commit with a descriptive message
git commit -m "Add new feature: email templates"

# Push to GitHub
git push
```

## 🔑 Sharing Your Project

When others clone your repository, they need to:

1. Clone the repository
2. Copy `application.properties.example` to `application.properties`
3. Add their own Gemini API key
4. Run the backend server

This is already documented in the README.md file.

## 🆘 Troubleshooting

### If you accidentally committed the API key:

```bash
# Remove the file from Git tracking
git rm --cached backend/src/main/resources/application.properties

# Commit the removal
git commit -m "Remove sensitive configuration file"

# Push the change
git push
```

**Important**: If the key was already pushed to GitHub, you should:
1. Rotate your Gemini API key immediately
2. Get a new key from [Google AI Studio](https://makersuite.google.com/app/apikey)
3. Update your local `application.properties`

### If .gitignore isn't working:

```bash
# Clear Git cache
git rm -r --cached .

# Re-add all files
git add .

# Commit
git commit -m "Fix .gitignore"
```

## 🎉 You're Done!

Your project is now safely uploaded to GitHub with your API key protected. Share the repository link on your resume or with collaborators!
