#!/usr/bin/env bash

cd src/test/resources/
zip -r outputs-02-29-47.zip outputs/
git status
git add -f outputs-02-29-47.zip
git commit -am "automatic compression."
git push -u origin master