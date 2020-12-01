# Advent of code 2020
My solutions to advent of code for 2020, written in ClojureScript.

## How to run?
To be able to connect Calva to shadow-cljs REPL run:

```bash
$ npx shadow-cljs watch app
$ node build/main.js
```

or use the npm scripts watch and start. Then in vscode run command "Calva: Connect to a Running REPL Server in the current Project"

To work with tests either `npx shadow-cljs watch test` or npm script watch-test.

## Credits
Most of the code setup has been borrowed from [Anders](git@github.com:litemerafrukt/clojurescript-node-skelleton.git), big thanks!