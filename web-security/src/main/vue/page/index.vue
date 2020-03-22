<template>
    <div>
        <b-navbar toggleable="lg" variant="dark" type="dark">
            <b-navbar-brand>Spring Security</b-navbar-brand>
            <b-navbar-toggle target="nav-collapse"></b-navbar-toggle>

            <b-collapse id="nav-collapse" is-nav>
                <b-navbar-nav class="ml-auto">
                    <b-nav-item-dropdown right v-if="state.name">
                        <!-- Using 'button-content' slot -->
                        <template v-slot:button-content>
                            <em>{{state.name}}</em>
                        </template>
                        <b-dropdown-item href="#">Profile</b-dropdown-item>
                        <b-dropdown-item href="/logout">Logout</b-dropdown-item>
                    </b-nav-item-dropdown>
                    <b-nav-item href="/login" right v-else>
                        <span>Login</span>
                    </b-nav-item>
                </b-navbar-nav>
            </b-collapse>
        </b-navbar>
        <b-container fluid="lg">
            <!-- Content here -->
            <b-row align-v="center">
                <b-col align-self="center">
                    <section class="p-5">
                        <b-card-group v-if="authentication.username">
                            <b-card border-variant="dark" header="Information" align="center">
                                <b-form-group label-cols="5" label="principal" class="text-value" horizontal>
                                    {{authentication.username}}
                                </b-form-group>
                                <b-form-group label-cols="5" label="authorities" class="text-value" horizontal>
                                    <div>
                                        <b-badge class="mr-1" v-for="(authority, index) in authentication.authorities" :key="'authority_' + index">
                                            {{authority.authority}}
                                        </b-badge>
                                    </div>
                                </b-form-group>
                            </b-card>
                        </b-card-group>
                        <div v-else>
                            <p>You are still an anonymous user.</p>
                            <p>Try login to verify authenticated user.</p>
                        </div>
                    </section>
                </b-col>
            </b-row>

        </b-container>
        <footer id="sticky-footer" class="py-4 bg-dark text-white-50">
            <div class="container text-center">
                <p class="text-center mb-0">Your current session is {{state.sessionId}}.</p>
            </div>
        </footer>
    </div>
</template>
<script>
    import CommonMixin from '~/vue/common/mixins/common-mixin'

    export default {
        mixins: [CommonMixin],
        data() {
            return {
                authentication: {}
            }
        },
        methods: {
            getState() {
                $http.get("/api/users/me").then(res => {
                    this.authentication = res.data
                })
            }
        },
        mounted() {
            if(window.state.name) {
                this.getState()
            }
        }
    }
</script>