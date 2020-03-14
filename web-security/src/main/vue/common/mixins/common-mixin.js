export default {
    mixins: [],
    data() {
        // this.getState()

        return {
            state: window.state
        }
    },
    methods: {
        getState() {
            async function state() {
                let response = await $http.get('/api/state')
                return response
            }

            state().then((res) => {
                this.state = res.data
            })
        }
    }
}