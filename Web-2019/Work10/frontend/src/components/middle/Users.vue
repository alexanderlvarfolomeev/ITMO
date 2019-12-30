<template>
    <div class="users datatable">
        <div class="caption">User</div>
        <table>
            <thead>
            <tr>
                <th>Id</th>
                <th>Login</th>
                <th>Name</th>
            </tr>
            </thead>
            <tbody>
                <TableUser v-for="user in users" :user="user" :key="user.id"/>
            </tbody>
        </table>
    </div>
</template>

<script>
    import TableUser from "../entity/TableUser";
    export default {
        name: "Index",
        props: ['users'],
        components: {TableUser}
    }
</script>

<style scoped>

</style>
