const HtmlWebpackPlugin = require('html-webpack-plugin');
const ModuleFederationPlugin = require('webpack/lib/container/ModuleFederationPlugin');
const path = require('path');
const deps = require("./package.json").dependencies;
module.exports = {
    devServer: {
        port: 3003,
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
        publicPath: 'http://localhost:3003/', // or auto
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
            name: 'card',
            filename: 'remoteEntry.js',
            remotes: {
                main_app: 'main_app@http://localhost:3000/remoteEntry.js',
            },
            exposes: {
                './Card': './src/components/Card',
                './AddPlacePopup': './src/components/AddPlacePopup',
                './ImagePopup': './src/components/ImagePopup',

            },
            shared: {
                ...deps,
                react: {
                    singleton: true,
                    requiredVersion: deps.react,
                },
                "react-dom": {
                    singleton: true,
                    requiredVersion: deps["react-dom"],
                },
            },
        }),
        new HtmlWebpackPlugin({
            template: './public/index.html',
        }),
    ],
};