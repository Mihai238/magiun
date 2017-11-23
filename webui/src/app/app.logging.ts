import {LoggerModule, NgxLoggerLevel} from 'ngx-logger';

export const logging = LoggerModule.forRoot({
    level: NgxLoggerLevel.INFO,
    serverLogLevel: NgxLoggerLevel.OFF
  });
