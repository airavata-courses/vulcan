<template>
  <v-container fill-height>
    <v-layout align-center>
      <v-flex pb-16>
        <v-card width="600" class="mx-auto" outlined>
          <v-card-title>Registration</v-card-title>
          <v-card-text>
            <v-row>
              <v-col>
                <v-text-field
                label="First Name"
                v-model="firstName"
                filled
                rounded
                />
              </v-col>
              <v-col>
                <v-text-field
                label="Last Name"
                v-model="lastName"
                filled
                rounded
                />
              </v-col>
            </v-row>
            <v-row>
              <v-col>
                <v-text-field
                label="Email / Username"
                v-model="username"
                persistent-hint
                filled
                rounded
                />
              </v-col>
            </v-row>
            <v-row>
              <v-col>
                <v-text-field
                label="Password"
                :type="showPassword ? 'text' : 'password'"
                v-model="password"
                :append-icon="showPassword ? 'mdi-eye' : 'mdi-eye-off'"
                filled
                rounded
                @click:append="showPassword = !showPassword"/>
              </v-col>
              <v-col>
                <v-text-field
                label="Confirm Password"
                :type="showPassword ? 'text' : 'password'"
                v-model="confirmPassword"
                :append-icon="showPassword ? 'mdi-eye' : 'mdi-eye-off'"
                filled
                rounded
                @click:append="showPassword = !showPassword"/>
              </v-col>
            </v-row>
          </v-card-text>
          <v-card-actions class="pa-4">
            <v-row class="justify-center">
              <v-col class="shrink">
                <v-btn depressed :to="{ name: 'Login' }">Return to Login</v-btn>
              </v-col>
              <v-col class="shrink">
                <v-btn depressed color="primary" @click="register">Create Account</v-btn>
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
      firstName: '',
      lastName: '',
      username: '',
      password: '',
      confirmPassword: '',
      showPassword: false,
    };
  },
  methods: {
    ...mapActions(['createAccount']),
    async register() {
      try {
        await this.createAccount({
          firstName: this.firstName,
          lastName: this.lastName,
          username: this.username,
          password: this.password,
        });
        this.$router.push({ name: 'Login' });
      // eslint-disable-next-line no-empty
      } catch {}
    },
  },
};
</script>
