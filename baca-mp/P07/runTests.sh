#!/bin/bash
prog=$1
for file in $2*.in; do
	start=$(date +%s%N)
	test=${file%.in}
	java "$prog" <"${test}".in >./tests/test.00.ou
	diff -bs -c --color tests/test.00.ou "${test}.out"
	end=$(date +%s%N)
	printf "\n%.3f%s\n" $(((end - start) / (10 ** 6)))e-3 s
done
