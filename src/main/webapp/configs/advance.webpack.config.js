const webpack = require('webpack');
const path = require('path');
var CleanWebpackPlugin = require('clean-webpack-plugin');
var ExtractTextPlugin = require('extract-text-webpack-plugin');
var extractCSS = new ExtractTextPlugin({
  filename: 'styles.css', // change as per your convinience
  allChunks: true
});
// path declarations which you can customize as per your needs.
var PATHS = {
  APP_DIR: path.resolve('./src/main/webapp'),
  CLIENT_DIR: path.resolve('./src/main/webapp/app/src'),
  BUILD_DIR: path.resolve('./src/main/webapp/app/dist'),
  NODE_MODULE: path.resolve('./node_modules')
};
module.exports = {
  devtool: 'cheap-module-eval-source-map', // best for development purpose

  stats: {
    children: false
  },

  entry: PATHS.CLIENT_DIR + '/YourIndex.js',

  output: {
    path: PATHS.BUILD_DIR,
    filename: 'bundle.js'
  },

  resolve: {
    extensions: ['*', '.js', '.jsx', '.json']
  },

  externals: {
    'cheerio': 'window',
    'react/lib/ExecutionEnvironment': true,
    'react/lib/ReactContext': true
  },

  module: {
    rules: [
      {
        enforce: "pre",
        test: /\.js(x)?/,
        exclude: /node_modules/,
        loader: "eslint-loader",
        options: {
          configFile: PATHS.APP_DIR + '/app/.eslintrc',
          failOnWarning: false,
          failOnError: true
        }
      }, {
        test: /\.js(x)?/,
        exclude: /node_modules/,
        loader: "babel-loader"
      }, {
        test: /\.css$/,
        loader: extractCSS.extract({
          fallback: 'style-loader',
          use: [
            {
              loader: 'css-loader'
            }
          ]
        })
      }, {
        test: /\.scss$/,
        loader: extractCSS.extract({
          fallback: 'style-loader',
          use: [
            {
              loader: 'css-loader'
            }, {
              loader: 'sass-loader'
            }
          ]
        })
      },
      // image loaders
      {
        test: /\.(gif|png|jpe?g|svg)$/i,
        loaders: [
          'file-loader',
          {
            loader: 'image-webpack-loader',
            query: {
              progressive: true,
              optimizationLevel: 7,
              interlaced: false,
              pngquant: {
                quality: '65-90',
                speed: 4
              }
            }
          }
        ]
      },
      // Font Definitions
      {
        test: /\.svg$/,
        loader: 'url-loader?limit=65000&mimetype=image/svg+xml&name=fonts/[name].[ext]'
      }, {
        test: /\.woff$/,
        loader: 'url-loader?limit=65000&mimetype=application/font-woff&name=fonts/[name].[ext]'
      }, {
        test: /\.woff2$/,
        loader: 'url-loader?limit=65000&mimetype=application/font-woff2&name=fonts/[name].[ext]'
      }, {
        test: /\.[ot]tf$/,
        loader: 'url-loader?limit=65000&mimetype=application/octet-stream&name=fonts/[name].[ext]'
      }, {
        test: /\.eot$/,
        loader: 'url-loader?limit=65000&mimetype=application/vnd.ms-fontobject&name=fonts/[name].[ext]'
      }
    ]
  },

  plugins: [
    new CleanWebpackPlugin([PATHS.BUILD_DIR], {
      root: process.cwd(),
      verbose: true,
      dry: false
    }),
    new webpack.ProvidePlugin({'$': 'jquery', 'jQuery': 'jquery', 'window.jQuery': 'jquery'}),
    new webpack.optimize.UglifyJsPlugin({
      minimize: false,
      compress: {
        warnings: false
      },
      output: {
        comments: false,
      },
      exclude: [/\.min\.js$/gi] // skip pre-minified libs
    }),
    new webpack.DefinePlugin({
      'process.env': {
        'NODE_ENV': JSON.stringify('development')
      }
    }),
    new webpack.IgnorePlugin(/^\.\/locale$/, /moment$/),
    extractCSS
  ]
};
