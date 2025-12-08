// vue.config.js，如没有此文件则手动创建
// module.exports = {};

//npm install uni-read-pages
// vue.config.js
const TransformPages = require("uni-read-pages");
const { webpack } = new TransformPages();
module.exports = {
    configureWebpack: {
        plugins: [
            new webpack.DefinePlugin({
                ROUTES: webpack.DefinePlugin.runtimeValue(() => {
                    const tfPages = new TransformPages({
                        includes: ["path", "name", "aliasPath"],
                    });
                    return JSON.stringify(tfPages.routes);
                }, true),
            }),
        ],
    },

    transpileDependencies: ["uview-ui"],
};
