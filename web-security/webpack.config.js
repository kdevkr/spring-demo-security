const webpack = require('webpack')
const path = require('path')
const FriendlyErrorsWebpackPlugin = require('friendly-errors-webpack-plugin')
const TerserWebpackPlugin = require('terser-webpack-plugin')
const MiniCssExtractPlugin = require("mini-css-extract-plugin")
const CompressionWebpackPlugin = require('compression-webpack-plugin')
const VueLoaderPlugin = require('vue-loader/lib/plugin')

const development = process.env.NODE_ENV !== 'production'

module.exports = {
    mode: development ? 'development' : 'production',
    entry: {
        index: ['babel-polyfill', path.resolve(__dirname, 'src/main/vue/index.js')]
    },
    output: {
        filename: '[name].js',
        path: path.resolve(__dirname, 'src/main/resources/dist'),
        publicPath: '/',
    },
    resolve: {
        extensions: ['.js', '.vue', '.css', '.scss'],
        alias: {
            vue$: 'vue/dist/vue.esm.js',
            images: path.resolve(__dirname, './src/main/resources/static/images'),
            '~': path.resolve(__dirname, './src/main'),
        },
        modules: [
            path.resolve(__dirname, './src/main/resources/static/css'),
            "node_modules",
        ]
    },
    module: {
        rules: [
            {
                test: /\.(sa|sc|c)ss$/,
                use: [
                    {
                        loader: development ? 'vue-style-loader' : MiniCssExtractPlugin.loader
                    },
                    {
                        loader: 'css-loader',
                        options: {
                            importLoaders: 1,
                            sourceMap: true
                        }
                    },
                    {
                        loader: 'postcss-loader'
                    },
                    {
                        loader: 'sass-loader'
                    },
                    {
                        loader: 'style-resources-loader',
                        options: {
                            patterns: [
                                // './path/from/context/to/scss/variables/*.scss',
                                // './path/from/context/to/scss/mixins/*.scss',
                            ]
                        }
                    }
                ]
            },
            {
                test: /\.vue$/,
                loader: 'vue-loader'
            },
            {
                test: /\.js$/,
                loader: 'babel-loader',
                exclude: /node_modules/,
                include: [path.resolve(__dirname, 'src')],
            },
            {
                enforce: 'pre',
                test: /\.js$/,
                loader: 'eslint-loader',
                exclude: /node_modules/
            }
        ]
    },
    plugins: [
        new MiniCssExtractPlugin({
            // Options similar to the same options in webpackOptions.output
            // both options are optional
            filename: "[name].css"
        }),
        new FriendlyErrorsWebpackPlugin(),
        new CompressionWebpackPlugin(),
        new TerserWebpackPlugin({
            // Enable/disable multi-process parallel running.
            parallel: true
        }),
        new VueLoaderPlugin()
    ],
    optimization: {
        splitChunks: {
            cacheGroups: {
                vendors: {
                    priority: -10,
                    test: /[\\/]node_modules[\\/]/
                }
            },

            chunks: 'async',
            minChunks: 1,
            minSize: 30000,
            name: true
        },
        minimizer: []
    },
    devServer: {
        inline: true,
        hot: true,
        contentBase: path.resolve(__dirname, 'src/main/resources/dist'),
        publicPath: '/dist/',
        filename: '[name].js',
        host:  'localhost',
        port: 8081,
        proxy: {
            '**': 'http://localhost:8080/'
        },
    }
}