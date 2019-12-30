<template>
    <aside>
        <SidebarPost v-for="post in viewPosts" :post="post" :users="users" :key="post.creationTime"/>
    </aside>
</template>

<script>
    import SidebarPost from './entity/SidebarPost';

    export default {
        name: "Sidebar",
        props: ['users', 'posts'],
        components: {
            SidebarPost
        },
        computed: {
            viewPosts: function () {
                return Object.values(this.posts).sort((a, b) => b.creationTime - a.creationTime).slice(0, 2);
            }
        }
    }
</script>

<style scoped>

</style>