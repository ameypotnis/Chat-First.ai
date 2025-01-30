# chat-first.ai
##### Open source wiki based chatbot 
##### I ❤️ questions.

# Index
1. [Overview](#overview)
2. [How It Works](#how-it-works)
3. [Who Is It For?](#who-is-it-for)
4. [Mode Of Use](#mode-of-use)
5. [We Recommend Using Online Mode for Evaluation](#we-recommend-using-online-mode-for-evaluation)
6. [AI Usage is an Iterative Journey](#ai-usage-is-also-iterativetrail-to-perfection-journey)
7. [Minimum System Requirements and Recommendations for Local LLM (AI)](#minimum-system-requirements-and-recommendations-for-local-llm-ai)
8. [Minimum System Requirements and Recommendations for Online LLM (AI)](#minimum-system-requirements-and-recommendations-for-online-llm-ai)
9. [Acknowledgements](#acknowledgements)
10. [How To Install](#how-to-install)
11. [License](#license)
12. [Technical Issues/Support](#technical-issuessupport)

### [Overview](#overview)
Welcome to chat-first.ai! This open-source(free to use) project aims to enhance workplace efficiency by creating an easily accessible knowledge base for common employee questions. The goal is to minimize repetitive inquiries to you by creating knowledge base for frequently asked questions and their answers in a centralized office machine in the form of wiki page sections, which can be accessed via a **simple chatbot interface for answering asked questions**.

### [How It Works](#how-it-works)
1. **Creating Wiki Pages**:
- Develop comprehensive wiki pages on the various topics and store them as a wiki pages on an office machine (server).
2. **AI Reading and Storing**:
- The AI continuously reads changes in these wiki pages after every 10 minutes.
- It stores relevant information in a database, organizing it for easy retrieval for asked question.
3. **Chatbot Interaction**:
- Employees ask questions through a chatbot application.
- The chatbot analyzes the question and searches the database for related documents or excerpts.
4. **AI Response**:
- The chatbot sends the question along with the found excerpt to a Large Language Model (AI).
- The AI uses this information to generate an accurate and helpful response.
- The response is sent back to the employee via the chatbot.

### [Who Is It For?](#who-is-it-for)
The chat-first.ai is designed specifically for small offices and businesses. This set of tools is perfect for organizations where team members regularly refer to established guidelines, checklists, and procedures. By centralizing this information and integrating it with an AI-driven chatbot, you can ask employees to ask questions to chatbot first to save your time and ensure your team always has the information they need.

It's useful for Chartered Accountant, Company Secretary, Small Business etc.

### [Mode Of Use](#mode-of-use)
1. **Local LLM(AI) (Local Mode)**:
- All applications wiki pages, databases, and LLM(AI) runs locally.
- You may run it by keeping your server machine disconnected from internet, once basic setup is done
- This setup prioritizes privacy and security, safeguarding your sensitive information within your own network.
2. **Use Online LLM(AI) Service (Online Mode)**:
- To provide accurate and contextual answers, the application utilizes an online Large Language Model (AI) service.
- Requires internet to run
- When a question is asked, both the query and relevant information excerpts from your local database are securely sent to the online LLM service for processing.
- Only the necessary information related to asked question is shared, ensuring efficient and focused use of the online service to provide quick and precise responses with cost savings.

### [We Recommend Using Online Mode for Evaluation](#we-recommend-using-online-mode-for-evaluation)
We recommend starting with the online LLM service approach to assess the fittment of the application for your needs. Use the pay-as-you-go online LLM services, such as Mistral or OpenAI, with feeding non-sensitive data. This allows you to evaluate the application's effectiveness without the upfront cost of investing in hardware. (Local LLM needs hardware investment to run smoothly for multiple users).

Online LLM services are cost-effective, requiring no initial setup investments. If you find that the application meets your requirements, you can consider investing in the necessary hardware to run the LLM locally later on.

If you have Mac or Linux with minimum 24GB RAM Deepseek r1 distilled model or Mistral LLM works locally

### [AI Usage is an Iterative Journey](#ai-usage-is-also-iterativetrail-to-perfection-journey)
Like any other field, creating a comprehensive and useful office wiki is an ongoing and iterative process. Here’s how you can refine your wiki to best meet the needs of you and employees:
1. **Trial and Error in Writing Pages**:
- Begin by drafting wiki pages based on common questions and essential information with the help of chatgpt or your preffered online gpt.
- Don’t worry about perfection in the initial stages; initial drafts can always be refined later.
- Encourage feedback and incorporate improvements iteratively to enhance clarity and comprehensiveness.
2. **Trial and Error in Questioning**:
- As employees interact with the chatbot, they will also observe and learn to give precise prompts(questions).
- Adjust and expand wiki content to better address queries covering office-specific vocabulary and commonly used shortcuts.
3. **Continuous Improvement**:
- Regularly review and update wiki pages to reflect changes in office procedures or vocabulary.

### [Minimum System Requirements and Recommendations for Local LLM (AI)](#minimum-system-requirements-and-recommendations-for-local-llm-ai)
To successfully run and leverage AI capabilities locally, it's important to have a system that meets the following **minimum requirements**:
1. **Minimum System Requirements**:
- To run a local Large Language Model (LLM), ensure your system has **at least**
    - 16GB of RAM.
    - i5/i7 or AMD Ryzen 5/7 processor
    - 256GB or more SSD hard disk
2. **Performance Considerations**:
- Be aware that as more users access the system concurrently, you may experience a decrease in performance. The system's ability to handle multiple requests efficiently will depend on the hardware's capacity and machine processing power.
3. **Our Recommendations**:
- For optimal performance, consider using the latest Mac Mini with *at least* 16GB of RAM.
- Alternatively, a Linux-based server is recommended over Windows, as Linux is often more efficient for handling server-based applications and processes.
  By aligning your setup with these recommendations, you can ensure a smoother operation of the AI features and accommodate multiple users as effectively as possible.

### [Minimum System Requirements and Recommendations for Online LLM (AI)](#minimum-system-requirements-and-recommendations-for-online-llm-ai)
1. **Minimum System Requirements**:
- To use a online Large Language Model (LLM), ensure your system has **at least**
    - 8GB of RAM.
    - i5 or more processor
    - 256GB or more SSD hard disk

## [Acknowledgements](#acknowledgements)
This project depends on/uses following open-source projects
chat-first.ai builds on top of other open-source projects:
1. [Wiki.js](https://wiki.js.org/) - Wiki.js is a modern and powerful wiki app built on Node.js, Git, and Markdown.
2. [Mozilla llamafile](https://github.com/Mozilla-Ocho/llamafile) - Run LLMs AI system run with a single file, no dependencies, and no setup. no internet connection required.
3. [Mistral API](https://mistral-api.com/) - Mistral Open weight local LLM is a powerful AI system that can be used locally.
4. [Docker](https://www.docker.com/) - Docker is a set of platform as a service products that use OS-level virtualization to deliver software in packages called containers.
5. [Spring AI](https://spring.io) - Spring AI is a powerful AI system interaction API.
6. [chat-first.ai](https://spring.io) - Simple chatbot over wiki.

## [How To Install](#how-to-install)
[Installation Guide](docs/installation.md)

## [License](#license)
This project is licensed under the AGPL License as stated in the [LICENSE file](https://github.com/ameypotnis/chat-first.ai/blob/main/LICENSE).
By using this project, you agree to the terms outlined in the license. and dependent projects licenses

## [Application Issues/Bug](#technical-issuessupport)
- For issues, kindly open a GitHub issue in the repository [chat-first.ai Bugs/Issues](https://github.com/ameypotnis/chat-first.ai/issues).
