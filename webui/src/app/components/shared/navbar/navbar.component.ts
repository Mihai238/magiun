import { Component } from '@angular/core';

@Component({
  moduleId: module.id,
  selector: 'app-navbar',
  templateUrl: 'navbar.component.html',
  styleUrls: ['navbar.component.scss'],
})
export class NavbarComponent {

  hideLeaderLines(): void {
    this.changeLeaderLinesVisibility('hidden');
  }

  showLeaderLines(): void {
    this.changeLeaderLinesVisibility('visible');
  }

  private changeLeaderLinesVisibility(visibility: string) {
    const lines = document.getElementsByClassName('leader-line');
    for (let i = 0; i < lines.length; i++) {
      (<any>lines[i]).style.visibility = visibility;
    }
  }
}
