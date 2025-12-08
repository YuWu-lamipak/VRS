import Vue from "vue";
import Vuex from "vuex";

Vue.use(Vuex);

const state = {
    draftsObj: {},
};

const getters = {
    getDraftsObj: (state) => state.draftsObj,
};

const mutations = {
    setDeaftsObj(state, obj) {
        state.draftsObj = { ...obj };
     },
};

const actions = {};

export default new Vuex.Store({
    state,
    getters,
    mutations,
    actions,
});
