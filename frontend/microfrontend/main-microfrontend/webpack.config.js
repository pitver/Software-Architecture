const HtmlWebpackPlugin = require('html-webpack-plugin');
const ModuleFederationPlugin = require('webpack/lib/container/ModuleFederationPlugin');
const path = require('path');
const deps = require("./package.json").dependencies;
module.exports = {
    devServer: {
        headers: {
            'Access-Control-Allow-Origin': '*',
            'Access-Control-Allow-Methods': 'GET, POST, PUT, DELETE, PATCH, OPTIONS',
            'Access-Control-Allow-Headers': 'X-Requested-With, content-type, Authorization',
        },
        port: 3000,
        open: true,
    },
    entry: "./src/index",
    cache: false,

    mode: "development",
    devtool: "source-map",

    optimization: {
        minimize: false,
    },
    output: {

        chunkFilename: '[id].[contenthash].js',
        publicPath: 'http://localhost:3000/', // or auto
    },
    resolve: {
        extensions: [".jsx", ".js", ".json"],
    },
    module: {
        rules: [
            {
                test: /\.js$/,
                loader: 'babel-loader',
                exclude: /node_modules/,
            },
            {
                test: /\.css$/,
                use: ['style-loader', 'css-loader'],
            },
            {
                test: /\.(woff|woff2|eot|ttf|otf)$/,
                use: 'file-loader',
            },
            {
                test: /\.(png|svg|jpg|jpeg|gif)$/i,
                use: 'file-loader',
            },
        ],
    },
    plugins: [
        new ModuleFederationPlugin({
            name: 'main_app',
            filename: "remoteEntry.js",
            remotes: {
                auth: 'auth@http://localhost:3001/remoteEntry.js',
                profile: 'profile@http://localhost:3002/remoteEntry.js',
                card: 'card@http://localhost:3003/remoteEntry.js',
            },
            exposes: {
                './PopupWithForm': './src/components/PopupWithForm',
                './CurrentUserContext': './src/contexts/CurrentUserContext',
            },
            shared: [{
                ...deps,
                react: {
                    singleton: true,
                    requiredVersion: deps.react,
                },
                "react-dom": {
                    singleton: true,
                    requiredVersion: deps["react-dom"],
                },

            },'./src/contexts/CurrentUserContext',]
        }),
        new HtmlWebpackPlugin({
            template: './public/index.html',
            chunks: ["main"],
        }),
    ],
};