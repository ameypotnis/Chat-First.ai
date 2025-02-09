# chat-first.ai

## Installation

### Prerequisites
- Please check the minimum system requirements before starting installation.
- Recommended to use spare machine as a server without important data on it before starting installation. 
- Follow the steps below based on your operating system to install docker.

#### Windows
1. Download Docker Desktop from the [Docker website](https://www.docker.com/products/docker-desktop).
2. Run the installer and follow the on-screen instructions.
3. Once installed, open Docker Desktop to start Docker services.

#### Linux
1. Open your terminal.[demo.md](demo.md)
2. Run the following commands:
   ```
   sudo apt-get update
   sudo apt-get install docker-ce docker-ce-cli containerd.io
   ```
3. After installation, start Docker using:
   ```
   sudo systemctl start docker
   ```

#### macOS
1. Download Docker Desktop from the [Docker website](https://www.docker.com/products/docker-desktop).
2. Open the downloaded `.dmg` file and drag Docker to your Applications folder.
3. Launch Docker Desktop to initiate Docker services.

### Running local AI
This guide will walk you through setting up and running AI systems on your Windows computer.

1. Downloading the AI Systems
Click on the following link to download the first AI system:
[mistral-7b-instruct-v0.2.Q4_0](https://huggingface.co/Mozilla/Mistral-7B-Instruct-v0.2-llamafile/resolve/main/mistral-7b-instruct-v0.2.Q4_0.llamafile?download=true)

2. Click on the following link to download the second AI system:
[mxbai-embed-large-v1-f16.llamafile](https://huggingface.co/Mozilla/mxbai-embed-large-v1-llamafile/resolve/main/mxbai-embed-large-v1-f16.llamafile?download=true)

Note: These links will download files. By default, your web browser will ask where to save the files. Choose a location that you can easily remember, such as your Downloads folder.

#### On Windows
1. Renaming the Downloaded Files
Once you have downloaded both files, you'll need to rename them slightly. Find the downloaded files and add ".exe" to the end of each filename. For example, if a file is named "mistral-7b-instruct-v0.2.Q4_0.llamafile", rename it to "mistral-7b-instruct-v0.2.Q4_0.llamafile.exe".

2. Running the AI Systems
Open a program called "Command Prompt". You can search for it using the Start menu search bar.
In the command prompt window, type the following command and press Enter:
```
mistral-7b-instruct-v0.2.Q4_0.llamafile.exe --server --nobrowser --port 8080
```
keep it running as is.

5. Then, open new command prompt and type the following command in the command prompt window and press Enter:
```
mxbai-embed-large-v1-f16.llamafile.exe --server --nobrowser --port 8081
```

#### On Mac/Linux
1. Running the AI Systems
   Open a Terminal and make downloaded file executable:
```
chmod u=rwx mistral-7b-instruct-v0.2.Q4_0.llamafile 
./mistral-7b-instruct-v0.2.Q4_0.llamafile --server --nobrowser --port 8080
```
keep it running as is.

5. Then, open new terminal and execute the following commands:
```
chmod u=rwx mxbai-embed-large-v1-f16.llamafile
mxbai-embed-large-v1-f16.llamafile --server --nobrowser --port 8081
```

Congratulations! You have successfully run the LLM (AI) systems on the server, keep them in running mode.

### Running chat-first.ai, Wiki.js and database with Docker
Follow these simple steps to set up the application using Docker Compose:
1. **Copy the `local-compose.yml` File from GitHub**:
- create `compose.yml` file on local machine and copy contains from [local-compose.yaml](https://raw.githubusercontent.com/ameypotnis/Chat-First.ai/refs/heads/develop/docs/local-compose.yaml) file
- change chat-first-ai-app version to latest version `chat-first-ai-app:{version}` from 7 to latest version number. you can find latest version number [here](https://github.com/ameypotnis/Chat-First.ai/actions?query=branch%3Amain)
2. **Run Docker Compose**:
- Open a terminal or command prompt on your computer.
- Navigate to the folder where you saved the `compose.yml` file. You can do this by typing commands like `cd foldername` to change directories.
- Once you're in the correct folder, type `docker-compose -f compose.yaml up` and press Enter.
  These steps will download required software and start the application using Docker Compose, getting everything set up according to the instructions in the `compose.yml` file. 

First time it will take some time to download all applications and getting ready (Internet required till this step).
Now you can disconnect from internet and if everything set up correctly you access local [wiki](http://localhost:11478/) and [chat-first.ai](http://localhost:11480)

## Environment Variables Setup for Online Service Usage

To properly configure the application for using an online AI system, you need to set the following environment variables. These variables will allow the application to connect with the AI API services (such as OpenAI or Mistral) securely.

### Required Environment Variables

| Variable Name                 | Description                                                       | Example Value                          |
|-------------------------------|-------------------------------------------------------------------|----------------------------------------|
| `OPENAI_API_URL`              | Base URL for the OpenAI API                                        | `https://api.openai.com/v1`            |
| `OPENAI_API_TOKEN`            | Authorization token for accessing the OpenAI API                  | `your-openai-api-token`                |
| `OPENAI_EMBEDDING_API_URL`    | URL endpoint for OpenAI embedding generation API                  | `https://api.openai.com/v1/embeddings` |
| `MISTRAL_API_TOKEN`           | Authorization token for accessing the Mistral API                 | `your-mistral-api-token`               |

### Steps to Set Environment Variables in docker compose.yml file 
```
services:
    app:
        environment:
        - OPENAI_API_URL=https://api.openai.com/v1
        - OPENAI_API_TOKEN=your-openai-api-token
        - OPENAI_EMBEDDING_API_URL=https://api.openai.com/v1/embeddings
        - MISTRAL_API_TOKEN=your-mistral-api-token
```
