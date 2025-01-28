<template>
  <v-dialog v-model="showDialog" persistent max-width="600px">
    <v-card>
      <v-card-title>Terms and Conditions</v-card-title>
      <v-card-text>
        By using this app, you agree to
        <a :href="licenseLink" target="_blank" rel="noopener noreferrer">Terms of use</a>.
      </v-card-text>
      <v-card-actions>
        <v-spacer></v-spacer>
        <v-btn color="primary" @click="agreeToTerms">I Agree</v-btn>
      </v-card-actions>
    </v-card>
  </v-dialog>
</template>

<script lang="ts" setup>
import { ref, onMounted } from "vue";

const showDialog = ref(false);
const licenseLink = "https://github.com/ameypotnis/Chat-First.ai/blob/main/LICENSE";

const agreeToTerms = () => {
  localStorage.setItem("termsAgreed", "true");
  showDialog.value = false;
};

onMounted(() => {
  if (!localStorage.getItem("termsAgreed")) {
    showDialog.value = true;
  }
});
</script>

<style scoped>
a {
  color: #1976d2;
  text-decoration: none;
}
</style>
