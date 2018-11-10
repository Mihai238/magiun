import {NGXLogger} from "ngx-logger";

export class MagiunLogger {

  private clazz: string;
  private logger: NGXLogger;

  constructor(clazz: string, logger: NGXLogger) {
    this.clazz = clazz;
    this.logger = logger;
  }

  info(message: string): void {
    this.logger.info(this.clazz.concat(": ").concat(message));
  }

  debug(message: string): void {
    this.logger.debug(this.clazz.concat(": ").concat(message));
  }

  error(message: string): void {
    this.logger.error(this.clazz.concat(": ").concat(message));
  }

  log(message: string): void {
    this.logger.log(this.clazz.concat(": ").concat(message));
  }

  trace(message: string): void {
    this.logger.trace(this.clazz.concat(": ").concat(message));
  }

  warn(message: string): void {
    this.logger.warn(this.clazz.concat(": ").concat(message));
  }
}
