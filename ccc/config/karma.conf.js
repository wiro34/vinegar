var path = require('path');

module.exports = function (config) {
  var ExtractTextPlugin = require('extract-text-webpack-plugin');
  var CSSExtractPlugin = new ExtractTextPlugin('[name].css');

  config.set({
    // base path used to resolve all patterns
    basePath: '',

    // frameworks to use
    // available frameworks: https://npmjs.org/browse/keyword/karma-adapter
    frameworks: ['jasmine'],

    // list of files/patterns to load in the browser
    files: [
      {pattern: 'config/spec.bundle.js', watched: false},
      {pattern: 'src/**/*.gif', watched: false, included: false, served: true},
      //'src/**/*.spec.js'
      'src/**/*.html'
    ],

    // files to exclude
    exclude: [],

    // Which plugins to enable
    plugins: [
      require('karma-phantomjs-launcher'),
      require('karma-jasmine'),
      require('karma-sourcemap-loader'),
      require('karma-coverage'),
      require('karma-mocha-reporter'),
      require('karma-webpack'),
      require('karma-ng-html2js-preprocessor')
    ],

    // preprocess matching files before serving them to the browser
    // available preprocessors: https://npmjs.org/browse/keyword/karma-preprocessor
    preprocessors: {
      './config/spec.bundle.js': ['webpack'],
      //'src/**/*(!.spec).js': ['webpack', 'sourcemap', 'coverage']
      'src/**/*.html': ['ng-html2js']
    },

    webpack: {
      devtool: 'inline-source-map',
      isparta: {
        embedSource: true,
        noAutoWrap: true
      },
      module: {
        exprContextCritical: false,
        loaders: [
          {test: /\.js$/, loader: 'babel', exclude: /node_modules/},
          {test: /\.css$/, loader: CSSExtractPlugin.extract(['css'])},
          {test: /\.scss$/, loader: CSSExtractPlugin.extract(['css', 'sass'])},
          {test: /jquery\.js$/, loader: 'expose?jQuery!expose?$'},
          {test: /\.json$/, loader: 'json'},
          {test: /\.html$/, loader: 'raw', exclude: [path.resolve('src/search.html')]},
          {test: /\.(png|gif|woff|woff2|eot|ttf|svg)$/, loader: 'url-loader?limit=100000'}
        ],
        preLoaders: [
          {test: /^(?!.*spec).+\.js$/, include: path.resolve('src'), loader: 'isparta'}
        ]
      },

      plugins: [
        CSSExtractPlugin
      ]
    },

    reporters: ['mocha', 'coverage'],

    coverageReporter: {
      dir: 'coverage/',
      reporters: [
        {type: 'html', subdir: '.'},
        {type: 'cobertura', file: 'cobertura.xml', subdir: '.'}
      ]
    },

    // Webpack please don't spam the console when running in karma!
    webpackServer: {noInfo: true},

    // web server port
    port: 9876,

    // enable / disable colors in the output (reporters and logs)
    colors: true,

    /*
     * level of logging
     * possible values: config.LOG_DISABLE || config.LOG_ERROR || config.LOG_WARN || config.LOG_INFO || config.LOG_DEBUG
     */
    logLevel: config.LOG_INFO,

    // enable / disable watching file and executing tests whenever any file changes
    autoWatch: false,

    /*
     * start these browsers
     * available browser launchers: https://npmjs.org/browse/keyword/karma-launcher
     */
    browsers: [
      'PhantomJS'
    ],

    /*
     * Continuous Integration mode
     * if true, Karma captures browsers, runs the tests and exits
     */
    singleRun: true,

    proxies: {
      '/common/': '/base/src/common/'
    },

    ngHtml2JsPreprocessor: {
      stripPrefix: 'src/auhikari/',
      moduleName: 'templates'
    },

    // Avoid DISCONNECTED messages
    // See https://github.com/karma-runner/karma/issues/598
    browserDisconnectTimeout: 10000, // default 2000
    browserDisconnectTolerance: 1, // default 0
    browserNoActivityTimeout: 60000 //default 10000
  });

};
