# Chat-First.ai
##### I ❤️ questions


## Introduction
Chat-First.ai is an open-source in office question-answer system designed for small offices such as chartered accountants, company secretaries, and similar office setups.
It enhances company specific knowledge by transforming documentation into an interactive question-answer system.

Here's how it works:
1. **Admin Task**: As an information administrator, you'll use the Wiki.js platform—part of the Chat-First.ai setup—to create pages. These pages should include knowledge documentation such as standard operating procedures, form-filling guidelines, company rules etc. anything which you want to knowledge share.
    - To start, open the Wiki.js application link installed via Docker Compose.
    - Create a new page and input the necessary content and guidelines.
2. **AI Functionality**: The AI system will automatically read and update its knowledge base from these pages every 10 minutes of create or update, incorporating any edits or new information.
3. **User Interaction**: Your company staff can ask questions to the system. The AI will respond using the latest information from the documented Wiki pages, providing answers and assistance.

This setup streamlines access to company knowledge information, ensuring responses across your organization for repetitive questions.

## Features
1. Chat-First.ai is open-source and free to use. You can use it for your organization without any fees.
2. The system is designed to be a simple interface.
3. Designed to be privacy first to work locally by default, Chat-First.ai does not require an internet connection to function by default.
4. The system is designed to be secure, with user data stored locally and won't leave your office.
5. Can be extensible to be used with minimal configuration changes with cloud (internet) AI systems like Mistral API for quick responses.

## Acknowledgements
Chat-First.ai builds on top of other open-source projects:
1. [Wiki.js](https://wiki.js.org/) - Wiki.js is a modern and powerful wiki app built on Node.js, Git, and Markdown.
2. [Mozilla llamafile](https://github.com/Mozilla-Ocho/llamafile) - Run LLMs AI system run with a single file, no dependencies, and no setup. no internet connection required.
3. [Mistral API](https://mistral-api.com/) - Mistral Open weight local LLM is a powerful AI system that can be used locally.
4. [Docker](https://www.docker.com/) - Docker is a set of platform as a service products that use OS-level virtualization to deliver software in packages called containers.
5. [Spring AI](https://spring.io) - Spring AI is a powerful AI system interaction API.

## Installation
[Installation Guide](docs/installation.md)

## License
This project is licensed under the AGPL License as stated in the [LICENSE file](https://github.com/ameypotnis/Chat-First.ai/blob/main/LICENSE).
By using this project, you agree to the terms outlined in the license.

## Technical Issues/Support
- For issues, kindly open a GitHub issue in the repository [Chat-First.ai Issues](https://github.com/ameypotnis/Chat-First.ai/issues).
