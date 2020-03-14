<!-- prettier-ignore -->
<template>
    <div id="app">
        <router-view :key="$route.path"></router-view>
    </div>
</template>
<!-- prettier-ignore -->
<script>
    export default {
        data() {
            return {
                production: process.env.NODE_ENV == 'production',
                state: window.state
            }
        }
    }
</script>