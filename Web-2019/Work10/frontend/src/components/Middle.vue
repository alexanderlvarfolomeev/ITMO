<template>
    <div class="middle">
        <aside>
            <Sidebar :posts="posts" :users="users"/>
        </aside>
        <main>
            <Index v-if="page === 'Index'" :posts="posts" :users="users"/>
            <Enter v-if="page === 'Enter'"/>
            <Register v-if="page === 'Register'"/>
            <Users v-if="page === 'Users'" :users="users"/>
        </main>
    </div>
</template>
<script>
    import Index from './middle/Index';
    import Enter from './middle/Enter';
    import Register from './middle/Register';
    import Users from "./middle/Users";
    import Sidebar from './Sidebar';

    export default {
        name: "Middle",
        props: ['posts', 'users'],
        data: function () {
            return {
                page: "Index"
            }
        },
        components: {
            Index,
            Enter,
            Register,
            Users,
            Sidebar,
        }, beforeCreate() {
            this.$root.$on("onChangePage", (page) => {
                this.page = page;
            });
        }
    }
</script>

<style scoped>

</style>
