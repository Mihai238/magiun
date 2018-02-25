import { Directive, ViewContainerRef } from '@angular/core';

@Directive({
  selector: '[appWorkflowDirective]',
})
export class WorkflowDirective {
  constructor(public viewContainerRef: ViewContainerRef) { }
}

