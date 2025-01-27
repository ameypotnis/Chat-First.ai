# Chat-First.ai
##### I ❤️ questions

## System Requirements
To run AI (Mistral 7B Mozilla LlamaFile) locally on your office machine, the minimal hardware requirements are:
CPU: Modern multi-core processor (e.g., Intel i7 or AMD Ryzen 7)
RAM: At least 16 GB
GPU: Good to have NVIDIA GPU with at least 16 GB VRAM (e.g., A100 or RTX 3090)
Storage: 100 GB+ SSD for model and data storage
These specs are for running the model efficiently; less powerful setups might work but with limited performance.

## Installation

### Prerequisites
- Ensure you have Docker installed on your common machine on intranet.
- Follow the steps below based on your operating system.

#### Windows
1. Download Docker Desktop from the [Docker website](https://www.docker.com/products/docker-desktop).
2. Run the installer and follow the on-screen instructions.
3. Once installed, open Docker Desktop to start Docker services.

#### Linux
1. Open your terminal.
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

### Running local AI (Windows)
This guide will walk you through setting up and running AI systems on your Windows computer.

1. Downloading the AI Systems
Click on the following link to download the first AI system:
[mistral-7b-instruct-v0.2.Q4_0](https://huggingface.co/Mozilla/Mistral-7B-Instruct-v0.2-llamafile/resolve/main/mistral-7b-instruct-v0.2.Q4_0.llamafile?download=true)

2. Click on the following link to download the second AI system:
[mxbai-embed-large-v1-f16.llamafile](https://huggingface.co/Mozilla/mxbai-embed-large-v1-llamafile/resolve/main/mxbai-embed-large-v1-f16.llamafile?download=true)

Note: These links will download files. By default, your web browser will ask where to save the files. Choose a location that you can easily remember, such as your Downloads folder.

3. Renaming the Downloaded Files
Once you have downloaded both files, you'll need to rename them slightly. Find the downloaded files and add ".exe" to the end of each filename. For example, if a file is named "mistral-7b-instruct-v0.2.Q4_0.llamafile", rename it to "mistral-7b-instruct-v0.2.Q4_0.llamafile.exe".

4. Running the AI Systems
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
Congratulations! You have successfully run the AI systems on your computer, keep them in running mode.

### Running Chat-First.ai with Docker Compose
1. TODO
