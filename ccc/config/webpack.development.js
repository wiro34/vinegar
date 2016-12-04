const helpers = require('./helpers');
const webpackMerge = require('webpack-merge');
const commonConfig = require('./webpack.common.js');
const metadata = require('./app.metadata.conf.js');
const appModules = require('./app.modules.conf');

// Plugins
const DefinePlugin = require('webpack/lib/DefinePlugin');
const HtmlWebpackPlugin = require('html-webpack-plugin');

// Constants
const ENV = process.env.ENV = process.env.NODE_ENV = 'development';

module.exports = appModules.map((name) => webpackMerge(commonConfig(name), {
  metadata: {
    isDevServer: true
  },
  debug: true,
  cache: true,
  devtool: 'cheap-module-source-map',

  output: {
    path: helpers.root(`dist/${name}`),
    filename: `${name}.[name].bundle.js`,
    sourceMapFilename: `${name}.[name].map`,
    chunkFilename: `${name}.[id].chunk.js`,
    library: 'ac_[name]',
    libraryTarget: 'var'
  },

  plugins: [
    new DefinePlugin({
      'ENV': JSON.stringify(ENV),
      'process.env': {
        'ENV': JSON.stringify(ENV),
        'NODE_ENV': JSON.stringify(ENV),
      }
    }),

    // for dev server
    new HtmlWebpackPlugin({
      filename: `${name}/search.html`,
      template: 'src/search.html',
      chunksSortMode: 'dependency',
      inject: 'body',
      hash: true
    })
  ],

  devServer: {
    port: 9000,
    host: '0.0.0.0',
    //historyApiFallback: true,
    watchOptions: {
      aggregateTimeout: 300,
      poll: 1000
    },
    outputPath: helpers.root('dist'),
    proxy: {
      '/entry/api/*': {
        target: 'http://localhost:4567/'
      }
    }
  },

  /*
   * Include polyfills or mocks for various node stuff
   *
   * See: https://webpack.github.io/docs/configuration.html#node
   */
  node: {
    global: 'window',
    crypto: 'empty',
    process: true,
    module: false,
    clearImmediate: false,
    setImmediate: false
  }

}));
