# Webui

## Setup

#### Prerequisites:
* `node >= 6.9.4`
* `npm >= 5.3.0`
* `@angular/cli`

#### Installing the dependencies:
* `npm install`

#### Running the app:
* `ng serve` - standard command 
* `ng serve --open --port 4200 --host 0.0.0.0 --disable-host-check` - command with parameters

## Tips

#### Running fake backend
* install json-server `sudo npm install -g json-server`
* `cd fake-backend`
* `json-server fake.js`

#### Create a component via angular cli

    ng g component components/<component-name>
