#!/usr/bin/env bash

cd src/test/resources/
zip -r outputs-47-0302.zip outputs/
git status
git add -f outputs-47-0302.zip
git commit -am "automatic compression."
git push -u origin master