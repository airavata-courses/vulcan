<template>
  <v-container fill-height>
    <v-layout align-center>
      <v-flex pb-16>
        <v-card width="500" class="mx-auto" outlined>
          <v-card-title>Login</v-card-title>
          <v-card-text>
            <v-text-field
            label="Username"
            v-model="username"
            prepend-icon="mdi-account-circle"
            filled
            rounded
            @keydown.enter="login" />
            <v-text-field
            label="Password"
            v-model="password"
            :type="showPassword ? 'text' : 'password'"
            prepend-icon="mdi-key"
            :append-icon="showPassword ? 'mdi-eye' : 'mdi-eye-off'"
            filled
            rounded
            @keydown.enter="login"
            @click:append="showPassword = !showPassword"/>
          </v-card-text>
          <v-card-actions class="pa-4">
            <v-row class="justify-center">
              <v-col class="shrink">
                <v-btn depressed :to="{ name: 'Registration' }">Register</v-btn>
              </v-col>
              <v-col class="shrink">
                <v-btn depressed color="primary" @click="login">Login</v-btn>
              </v-col>
            </v-row>
          </v-card-actions>
        </v-card>
      </v-flex>
    </v-layout>
  </v-container>
</template>
<script>

import { mapActions } from 'vuex';

export default {
  data() {
    return {
      showPassword: false,
      username: '',
      password: '',
    };
  },
  methods: {
    ...mapActions(['authenticate']),
    async login() {
      try {
        await this.authenticate({
          username: this.username,
          password: this.password,
        });
        this.$router.push({ name: 'Home' });
      } catch (err) {
        console.error(err);
      }
    },
  },
};
</script>
