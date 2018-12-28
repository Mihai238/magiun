import {NGXLogger} from "ngx-logger";

export class MagiunLogger {

  private clazz: string;
  private logger: NGXLogger;

  constructor(clazz: string, logger: NGXLogger) {
    this.clazz = clazz;
    this.logger = logger;
  }

  info(message: any, ...additional: any[]): void {
    let m = this.clazz.concat(": ").concat(message);
    if (additional != null && additional.length > 0) {
      this.logger.info(m, additional);
    } else {
      this.logger.info(m);
    }
  }

  debug(message: any, ...additional: any[]): void {
    let m = this.clazz.concat(": ").concat(message);
    if (additional != null && additional.length > 0) {
      this.logger.debug(m, additional);
    } else {
      this.logger.debug(m);
    }
  }

  error(message: any, ...additional: any[]): void {
    let m = this.clazz.concat(": ").concat(message);
    if (additional != null && additional.length > 0) {
      this.logger.error(m, additional);
    } else {
      this.logger.error(m);
    }
  }

  log(message: any, ...additional: any[]): void {
    let m = this.clazz.concat(": ").concat(message);
    if (additional != null && additional.length > 0) {
      this.logger.log(m, additional);
    } else {
      this.logger.log(m);
    }
  }

  trace(message: any, ...additional: any[]): void {
    let m = this.clazz.concat(": ").concat(message);
    if (additional != null && additional.length > 0) {
      this.logger.trace(m, additional);
    } else {
      this.logger.trace(m);
    }
  }

  warn(message: any, ...additional: any[]): void {
    let m = this.clazz.concat(": ").concat(message);
    if (additional != null && additional.length > 0) {
      this.logger.warn(m, additional);
    } else {
      this.logger.warn(m);
    }
  }
}
