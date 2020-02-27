#!/usr/bin/env bash

cd src/test/resources/
zip -r outputs.zip  outputs/
git status
git add -f outputs.zip
git commit -am "automatic compression."
git push -u origin master