switch (process.env.NODE_ENV) {
  case 'p':
  case 'production':
    module.exports = require('./config/webpack.production');
    break;
  case 't':
  case 'test':
    module.exports = require('./config/webpack.test');
    break;
  case 'd':
  case 'development':
  default:
    module.exports = require('./config/webpack.development');
}
