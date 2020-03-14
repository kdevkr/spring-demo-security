<!-- prettier-ignore -->
<template>
    <router-view :key="$route.path"/>
</template>
<!-- prettier-ignore -->
<script>
    import CommonMixin from '~/vue/common/mixins/common-mixin'
    export default {
        mixins: [CommonMixin],
        data() {
            return {
            };
        },
        mounted() {

        }
    }
</script>
<style lang="scss">

</style>