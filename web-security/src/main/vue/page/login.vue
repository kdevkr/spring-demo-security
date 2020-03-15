<template>
    <section class="p-5">
        <b-container>
            <b-row align-v="center" align-h="center">
                <b-col align-self="center">
                    <b-form ref="loginForm" @submit="onSubmit" class="login-form mx-auto">
                        <b-form-group label="Username">
                            <b-form-input v-model="loginForm.username" name="username" :state="$v.loginForm.$dirty ? !$v.loginForm.username.$invalid : null"></b-form-input>
                            <b-form-invalid-feedback v-if="!$v.loginForm.username.required">Required</b-form-invalid-feedback>
                        </b-form-group>
                        <b-form-group label="Password">
                            <b-form-input type="password" name="password" v-model="loginForm.password" :state="$v.loginForm.$dirty ? !$v.loginForm.password.$invalid : null"></b-form-input>
                            <b-form-invalid-feedback v-if="!$v.loginForm.password.required">Required</b-form-invalid-feedback>
                        </b-form-group>
                        <b-form-group class="text-right">
                            <b-button type="submit">
                                Login
                            </b-button>
                        </b-form-group>
                    </b-form>
                </b-col>
            </b-row>
        </b-container>
    </section>
</template>
<script>
    import CommonMixin from '~/vue/common/mixins/common-mixin'
    import { required } from 'vuelidate/lib/validators'

    export default {
        mixins: [CommonMixin],
        data() {
            return {
                loginForm: {}
            }
        },
        validations: {
            loginForm: {
                username: {
                    required
                },
                password: {
                    required
                }
            }
        },
        methods: {
            onSubmit(e) {
                e.preventDefault()

                this.$v.loginForm.$touch()
                if(!this.$v.loginForm.$invalid) {
                    this.$refs.loginForm.setAttribute("action", "/login")
                    this.$refs.loginForm.setAttribute("method", 'post')
                    this.$refs.loginForm.submit()
                }
            }
        },
        mounted() {

        }
    }
</script>