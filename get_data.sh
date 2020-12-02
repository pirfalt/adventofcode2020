#!/bin/bash

day=${1:-}
day_padded=$(printf '%02d' $day)
cookie=$(cat .cookie)

curl -b "$cookie" "https://adventofcode.com/2020/day/${day}/input" > data/$day_padded.txt
