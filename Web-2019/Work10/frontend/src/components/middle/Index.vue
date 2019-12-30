<template>
    <div class="articles">
        <FullPost v-for="post in viewPosts" :post="post" :users="users" :key="post.creationTime"/>
    </div>
</template>

<script>
    import FullPost from '../entity/FullPost';

    export default {
        name: "Index",
        props: ['users', 'posts'],
        components: {
            FullPost
        },
        computed: {
            viewPosts: function () {
                return Object.values(this.posts).sort((a, b) => b.creationTime - a.creationTime);
            }
        }
    }
</script>

<style scoped>

</style>
