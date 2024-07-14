const HtmlWebpackPlugin = require('html-webpack-plugin');
const ModuleFederationPlugin = require('webpack/lib/container/ModuleFederationPlugin');
const path = require('path');
const deps = require("./package.json").dependencies;
module.exports = {
    devServer: {
        port: 3002,
        headers: {
            'Access-Control-Allow-Origin': '*',
            'Access-Control-Allow-Methods': 'GET, POST, PUT, DELETE, PATCH, OPTIONS',
            'Access-Control-Allow-Headers': 'X-Requested-With, content-type, Authorization',
        },
    },
    entry: "./src/index",
    cache: false,

    mode: "development",
    devtool: "source-map",

    optimization: {
        minimize: false,
    },

    output: {
        path: path.resolve(__dirname, 'dist'),
        publicPath: 'http://localhost:3002/', // or auto
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
                test: /\.css$/i,
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
            name: 'profile',
            filename: 'remoteEntry.js',
            remotes: {
                main_app: 'main_app@http://localhost:3000/remoteEntry.js',
            },
            exposes: {
                './EditAvatarPopup': './src/components/EditAvatarPopup',
                './EditProfilePopup': './src/components/EditProfilePopup',
            },
            shared: {
                react: {
                    singleton: true,
                    requiredVersion: deps.react,
                },
                'react-dom': {
                    singleton: true,
                    requiredVersion: deps['react-dom'],
                },
            },
        }),
        new HtmlWebpackPlugin({
            template: './public/index.html',
        }),
    ],
};
