<template>

      <nav class="navbar navbar-expand-sm" role="navigation" aria-label="main navigation">
        <div class="navbar-nav">
            <div class="left-links">
                <a class="nav-item nav-link">
                    <img src="/img/logoTransparent.png" />
                </a>
                    
                    <router-link v-if="isLoggedIn" class ="nav-item nav-link" to="/">Home</router-link>
                    <router-link v-if="isLoggedIn" class ="nav-item nav-link" to="/newevent">Create New Event</router-link>
            </div>
                <p v-if="isLoggedIn" class ="nav-item nav-link logonoff" @click="logout()">Logoff</p>
        </div>
      </nav>

</template>

<script>
import auth from '../auth';

export default {
    name: "nav-header",
    data() {
        return {
            isLoggedIn: auth.getUser()!==null
        }
    },
    methods: {
        logout() {
            auth.logout();
            this.$router.push({ name: 'login' }).catch(()=> {});
        },
        isLoggedInFunc() {
            this.isLoggedIn = auth.getUser()!==null;
        }
    },
    created() {
        setInterval(this.isLoggedInFunc, 100);
    }
}
</script>

<style>
:root {
  --gxorange: #ff7f68;
  --gxyellow: #ffdb2b;
  --gxpink: #ef2871;
  --gxgreen: #71d861;
  --gxgreendark: #63bd55;
  --gxwhite: #effffb;
  --gxgreentransparent: rgb(113, 216, 97, 1)
}
nav {
    background-color: var(--gxgreentransparent);
    
}
.left-links {
    display: flex;
}

.navbar-nav {
    display: flex;
    justify-content: space-between;
    width: 100%;
    font-size: 1.5rem;
}
.navbar {
    height: 5rem;
    margin-bottom: 15px;
}

.logonoff {
    align-items: right;
}

.nav-link {
    margin: 0;
}

.nav-item.nav-link img {
    height: 250%;
    margin: -16px;
}

a.nav-item.nav-link {
    height: 100%;
}

.left-links {
    height: 100%;
}

.navbar-nav {
    height: 100%;
}

.logonoff {
    cursor: pointer;
}

</style>
