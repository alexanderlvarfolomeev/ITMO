<!--suppress HtmlUnknownAnchorTarget -->
<template>
    <article>
        <div class="title">
            <a :href="'#page=Post/' + post.id" @click="changePage('Post/' + post.id)">{{post.title}}</a>
        </div>
        <div class="information">By {{post.user.login}}</div>
        <div class="body">
            {{post.text}}
        </div>
        <div class="footer">
            <div class="right">
                <img src="../../assets/img/date_16x16.png" title="Publish Time" alt="Publish Time"/>
                2 days ago
                <img src="../../assets/img/comments_16x16.png" title="Comments" alt="Comments"/>
                <a href="#">0</a>
<!--                <a href="#">{{viewComments().length}}</a>-->
            </div>
        </div>
    </article>
</template>

<script>
    export default {
        props: ['users', 'post'],
        name: "FullPost",
        methods: {
            changePage: function (page) {
                this.$root.$emit("onChangePage", page);
            }
            // },
            // viewComments: function () {
            //     const post=this.post;
            //     return Object.values(this.comments)
            //         .filter((comm) => (comm.postId === post.id)).sort((a, b) => (a.id - b.id));
            // }
        }
    }
</script>

<style scoped>

</style>
