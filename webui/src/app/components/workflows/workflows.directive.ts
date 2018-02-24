import { Directive, ViewContainerRef } from '@angular/core';

@Directive({
  selector: '[appWorkflowDirective]',
})
export class WorkflowsDirective {
  constructor(public viewContainerRef: ViewContainerRef) { }
}

