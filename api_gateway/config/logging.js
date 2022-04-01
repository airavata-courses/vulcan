const { createLogger, format, transports } = require('winston');
const logFormat = format.combine(
  format.timestamp({ format: 'YYYY-MM-DD HH:mm:ss' }),
  format.align(),
  format.printf(({ level, timestamp, message }) => `${level.toUpperCase()} ${timestamp} - ${message.trim()}`)
);

const logger = createLogger({
  transports: [
    new transports.File({
      filename: 'logs/gateway.log',
      format: logFormat,
    }),
    new transports.Console({
      debugStdout: true
    })
  ],
})

module.exports = logger
