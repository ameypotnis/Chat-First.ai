<template>
  <v-container>
    <v-responsive
      class="align-center fill-height mx-auto"
      max-width="800"
    >
      <div class="text-center mb-6">
        <h1 class="text-h4 font-weight-medium">{{modelHealth.name || ''}}&nbsp;Chat-First.ai</h1>
        <p class="text-subtitle-1 text-grey-darken-1 font-weight-regular mt-2">
          <span class="">I ❤️ questions.</span>
        </p>
        <div class="d-flex justify-center align-center gap-4 mt-2">
          <v-btn
            :href="selectedWiki || ''"
            target="_blank"
            variant="text"
            class="text-body-2"
            prepend-icon="mdi-book-open-variant"
            color="blue-darken-1"
          >
            Your Office Wiki
          </v-btn>

          <v-btn
            href="https://github.com/ameypotnis/Chat-First.ai/issues"
            target="_blank"
            variant="text"
            class="text-body-2"
            prepend-icon="mdi-alert-circle-outline"
            color="red-darken-1"
          >
            Report Issue
          </v-btn>
          <v-btn
            variant="text"
            class="text-body-2"
            prepend-icon="mdi-instagram"
            color="grey-darken-1"
            @click="dialogVisible = true"
          >
            Selfie with me
          </v-btn>
          <v-btn
            href="https://github.com/ameypotnis/Chat-First.ai"
            target="_blank"
            variant="text"
            class="text-body-2"
            prepend-icon="mdi-github"
            color="grey-darken-3"
          >
            Open Source Code
          </v-btn>
          <v-btn
            href="https://whatsapp.com/channel/0029Vb2y0wBLNSa2YvyHEM1n"
            target="_blank"
            variant="text"
            class="text-body-2"
            prepend-icon="mdi-whatsapp"
            color="green-darken-1"
          >
            WhatsApp Channel
          </v-btn>

        </div>
        <div class="d-flex justify-center align-center gap-4 mt-2">
          <div class="d-flex align-center">
            <span class="text-caption mr-1 ml-3"><b>AI:</b></span>
            <v-icon
              :color="modelHealth.status === 'true' ? 'green' : 'red'"
              size="small"
            >
              mdi-circle
            </v-icon>
            <span class="text-caption mr-1 mr-3">{{ modelHealth.status === 'true' ? 'Up' : 'Down' }}</span>
          </div>
          <div class="d-flex align-center">
            <span class="text-caption mr-1 ml-3"><b>Service:</b></span>
            <span class="text-caption mr-1 mr-3">{{ modelHealth.url }}</span>
          </div>
          <div class="d-flex align-center">
            <span class="text-caption mr-1 ml-3"><b>Token:</b></span>
            <span class="text-caption mr-1 mr-3">{{ modelHealth.key }}</span>
          </div>
          <span class="text-caption mr-1 mr-3">as of {{modelHealth.timestamp}}</span>
        </div>
      </div>

      <v-card
        class="mx-auto chat-card"
        elevation="2"
        rounded="lg"
      >
        <v-card-text
          class="chat-messages pa-4"
          style="height: 500px; overflow-y: auto; scroll-behavior: smooth;"
        >
          <v-list class="chat-list">
              <v-list-item
                v-for="(message, index) in messages"
                :key="index"
                :class="[
                  'message-item mb-3 rounded-lg',
                  message.sender === 'bot' ? 'bot-message bg-grey-lighten-4' : 'user-message bg-primary-lighten-4'
                ]"
                density="comfortable"
              >
                <v-list-item-title class="message-content">
                  <span class="text-subtitle-2 font-weight-medium text-grey-darken-2 mb-2 d-block">
                    {{ message.sender === 'bot' ? 'Assistant' : 'You' }}
                  </span>
                  <span class="text-body-1 text-pre-wrap" v-html="message.text"></span>
                </v-list-item-title>
              </v-list-item>
          </v-list>
          <div v-if="isLoading" class="d-flex justify-center align-center mt-4">
            <v-progress-circular
              indeterminate
              color="primary"
            ></v-progress-circular>
          </div>
        </v-card-text>

        <v-divider></v-divider>
        <v-card-actions class="pa-4">
          <div class="model-select mr-4" style="max-width: 200px">
            <span class="text-subtitle-2 font-weight-medium text-grey-darken-2">AI Mode:</span>
            <span class="text-body-1 ml-2">{{ selectedModel.charAt(0).toUpperCase() + selectedModel.slice(1).toLowerCase() }}</span>
          </div>
          <v-text-field
            v-model="userInput"
            :disabled="modelHealth.status === 'false'"
            placeholder="Type your message..."
            variant="outlined"
            density="comfortable"
            hide-details
            class="input-field font-weight-regular"
            bg-color="grey-lighten-4"
            @keyup.enter="sendMessage"
          ></v-text-field>
          <v-btn
            color="primary"
            :disabled="modelHealth.status === 'false'"
            class="ml-4 px-6 text-body-2 font-weight-medium"
            elevation="1"
            :loading="isLoading"
            @click="sendMessage"
          >
            <v-icon size="small" class="mr-2">mdi-send</v-icon>
            Send
          </v-btn>
        </v-card-actions>
      </v-card>
      <v-row>
        <v-col cols="12">
          AI can make mistakes, check important information. <b>Please do not share any sensitive information.</b>
          <br>
          <b>Online</b> mode runs in the cloud AI model.&nbsp;<b>Local</b> mode runs entirely in your office no data is sent to the internet.
        </v-col>
      </v-row>
    </v-responsive>

    <v-dialog
      v-model="dialogVisible"
      fullscreen
      transition="dialog-bottom-transition"
    >
      <v-card class="d-flex flex-column align-center justify-center" style="min-height: 100vh; background: linear-gradient(135deg, #f5f7fa 0%, #c3cfe2 100%);">
        <h1 class="text-h2 font-weight-bold mb-6" style="color: #1a237e;">Chat-First.ai</h1>
        <p class="text-h4 mb-4" style="color: #303f9f;">I ❤️ questions.</p>
        <p class="text-h5 mb-8" style="color: #3949ab;">It's open-source and free to use.</p>
        <p class="mb-8" style="color: #3949ab;">Take a selfie with me and share it on instagram.</p>
        <v-btn
          color="primary"
          size="large"
          @click="dialogVisible = false"
          class="px-8"
          elevation="2"
        >
          OK
        </v-btn>
      </v-card>
    </v-dialog>
  </v-container>
</template>

<script setup lang="ts">
import { ref, onMounted } from 'vue'

interface Message {
  sender: 'user' | 'bot'
  text: string
}

interface Health {
  name: string
  timestamp: string
  key: string
  status: string
  type: string
  url: string
  wiki: string
}

const messages = ref<Message[]>([])
const userInput = ref('')
const isLoading = ref(false)
const selectedWiki = ref('')
const selectedModel = ref('')
const models = ref<string[]>([])
const dialogVisible = ref(false)
const modelHealth = ref<Health>({} as Health)

onMounted(() => {
  fetch('/api/connectors/env', {
    method: 'GET'
  }).then(response => {
    if (!response.ok) {
      throw new Error('Network response was not ok')
    }
    return response.json()
  })
  .then(data => {
    console.log(data)
    modelHealth.value = data
    selectedWiki.value = data.wiki
    if (data.status === 'true') {
      models.value.push(data.type)
      selectedModel.value = data.type
    }
  })
  .catch(error => {
    console.error('Error checking model health:', error)
  })
})

const sendMessage = () => {
  if (!userInput.value.trim()) return

  // Add user message
  messages.value.push({
    sender: 'user',
    text: userInput.value
  })
  isLoading.value = true
  fetch('/api/connectors/wiki/ask', {
    method: 'POST',
    headers: {
      'Content-Type': 'application/json'
    },
    body: JSON.stringify({
      message: userInput.value,
      model: selectedModel.value
    })
  })
  .then(response => response.json())
  .then(data => {
    if (data.error) {
      messages.value.push({
        sender: 'bot',
        text: data.error
      })
    } else {
      messages.value.push({
        sender: 'bot',
        text: data.message.replace(/<think>[\s\S]*?<\/think>/g, '').replace("\n\n", "")
      })
    }
    setTimeout(() => {
      const container = document.querySelector('.chat-messages')
      if (container) {
        container.scrollBy(0, container.scrollHeight + 100)
      }
    }, 100)
  })
  .catch(error => {
    console.error('Error:', error)
    messages.value.push({
      sender: 'bot',
      text: 'Sorry, there was an error processing your message.'
    })
  })
  .finally(() => {
    isLoading.value = false
  })
  userInput.value = ''
}
</script>

