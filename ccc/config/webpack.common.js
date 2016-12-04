const path = require('path');
const webpack = require('webpack');
const helpers = require('./helpers');
const metadata = require('./app.metadata.conf.js');

// Plugins
const CopyWebpackPlugin = require('copy-webpack-plugin');
const HtmlWebpackPlugin = require('html-webpack-plugin');
const ExtractTextPlugin = require('extract-text-webpack-plugin');

const CSSExtractPlugin = new ExtractTextPlugin(`[name].[hash].css`);
module.exports = {
  metadata: metadata[moduleName],
  cache: false,
  entry: {
    'main': `./src/${moduleName}/${moduleName}.js`
  },

  resolve: {
    extensions: ['', '.js'],
    root: helpers.root('src'),
    modulesDirectories: ['node_modules']
  },

  module: {
    preLoaders: [],
    loaders: [
      {test: /\.js$/, loader: 'ng-annotate!babel', exclude: /node_modules/},
      {test: /\.css$/, loader: CSSExtractPlugin.extract(['css'])},
      {test: /\.scss$/, loader: CSSExtractPlugin.extract(['css', 'sass'], {publicPath: 'auhikari'})},
      {test: /jquery\.js$/, loader: 'expose?jQuery!expose?$'},
      {test: /\.html$/, loader: 'raw', exclude: [helpers.root('src/search.html')]},
      {test: /\.(png|gif|woff|woff2|eot|ttf|svg)$/, loader: 'url-loader?limit=100000'}
    ]
  },

  plugins: [
    new webpack.optimize.OccurenceOrderPlugin(true),

    new webpack.optimize.CommonsChunkPlugin({
      name: ['vendor'].reverse(),
      minChunks: function (module) {
        return module.resource && module.resource.indexOf(path.resolve(__dirname, '../src')) === -1;
      }
    }),

    new CopyWebpackPlugin([{
      from: 'src/assets',
      to: 'assets',
      ignore: '*.css'
    }]),

    new HtmlWebpackPlugin({
      filename: 'search.html',
      template: 'src/search.html',
      chunksSortMode: 'dependency',
      inject: 'body',
      hash: true
    }),

    CSSExtractPlugin
  ],

  /*
   * Include polyfills or mocks for various node stuff
   *
   * See: https://webpack.github.io/docs/configuration.html#node
   */
  node: {
    global: 'window',
    crypto: 'empty',
    module: false,
    clearImmediate: false,
    setImmediate: false
  }
};
