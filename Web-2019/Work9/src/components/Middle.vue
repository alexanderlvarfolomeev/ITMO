<template>
    <div class="middle">
        <Sidebar :posts="posts" :users="users"/>
        <main>
            <Index v-if="page === 'Index'"/>
            <Enter v-if="page === 'Enter'"/>
            <Register v-if="page === 'Register'"/>
            <AddPost v-if="page === 'AddPost'"/>
            <EditPost v-if="page === 'EditPost'"/>
        </main>
    </div>
</template>
<script>
    import Index from './middle/Index';
    import Enter from './middle/Enter';
    import Register from './middle/Register';
    import AddPost from './middle/AddPost';
    import EditPost from './middle/EditPost';
    import Sidebar from './Sidebar';

    export default {
        name: "Middle",
        props: ['users', 'posts'],
        data: function () {
            return {
                page: "Index"
            }
        },
        components: {
            EditPost,
            Index,
            Enter,
            Register,
            Sidebar,
            AddPost
        },
        beforeCreate() {
            this.$root.$on("onChangePage", (page) => {
                this.page = page;
            });
        }
    }
</script>

<style scoped>

</style>
