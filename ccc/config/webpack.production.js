const helpers = require('./helpers');
const webpackMerge = require('webpack-merge');
const commonConfig = require('./webpack.common.js');
const appModules = require('./app.modules.conf');

// Plugins
const ProvidePlugin = require('webpack/lib/ProvidePlugin');
const NormalModuleReplacementPlugin = require('webpack/lib/NormalModuleReplacementPlugin');
const IgnorePlugin = require('webpack/lib/IgnorePlugin');
const DedupePlugin = require('webpack/lib/optimize/DedupePlugin');
const UglifyJsPlugin = require('webpack/lib/optimize/UglifyJsPlugin');
const WebpackMd5Hash = require('webpack-md5-hash');

module.exports = appModules.map((name) => webpackMerge(commonConfig(name), {
  debug: false,
  devtool: 'source-map',
  output: {
    path: helpers.root(`dist/${name}`),
    filename: '[name].[chunkhash].bundle.js',
    sourceMapFilename: '[name].[chunkhash].bundle.map',
    chunkFilename: '[id].[chunkhash].chunk.js'
  },

  plugins: [
    new WebpackMd5Hash(),

    new DedupePlugin(),

    new UglifyJsPlugin({
      // beautify: true, //debug
      // mangle: false, //debug
      // dead_code: false, //debug
      // unused: false, //debug
      // deadCode: false, //debug
      // compress: {
      //   screw_ie8: true,
      //   keep_fnames: true,
      //   drop_debugger: false,
      //   dead_code: false,
      //   unused: false
      // }, // debug
      // comments: true, //debug

      beautify: false, //prod
      mangle: { screw_ie8 : true }, //prod
      compress: { screw_ie8: true }, //prod
      comments: false //prod
    }),

    //new NormalModuleReplacementPlugin(
    //  /angular2-hmr/,
    //  helpers.root('config/modules/angular2-hmr-prod.js')
    //),

    // new IgnorePlugin(/angular2-hmr/),

    // new CompressionPlugin({
    //   regExp: /\.css$|\.html$|\.js$|\.map$/,
    //   threshold: 2 * 1024
    // })
  ],

  /**
   * Html loader advanced options
   *
   * See: https://github.com/webpack/html-loader#advanced-options
   */
  htmlLoader: {
    minimize: true,
    removeAttributeQuotes: false,
    caseSensitive: true,
    customAttrSurround: [
      [/#/, /(?:)/],
      [/\*/, /(?:)/],
      [/\[?\(?/, /(?:)/]
    ],
    customAttrAssign: [/\)?\]?=/]
  },

  /*
   * Include polyfills or mocks for various node stuff
   *
   * See: https://webpack.github.io/docs/configuration.html#node
   */
  node: {
    global: 'window',
    crypto: 'empty',
    process: false,
    module: false,
    clearImmediate: false,
    setImmediate: false
  }

}));
