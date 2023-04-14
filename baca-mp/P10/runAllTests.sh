#!/bin/bash
prog=$1
dir=$2

totalElapsed=0
okCount=0
failCount=0
# printf "helloworld\n"
for file in "$dir"*.in; do
	start=$(date +%s%N)
	test=${file%.in}
	java "$prog" <"${test}".in >./tests/test.00.ou
	end=$(date +%s%N)
	diff -bs -c --color tests/test.00.ou "${test}.out"
	if [ $? == 0 ]; then
		# \033[K clears the line after \r
		# printf '\e[32m%s\e[0m\n' "ok"
		# printf '\e[32m%s\e[0m\r\033[K' "ok"
		((okCount++))
	else
		# printf '\e[31m%s\e[0m\n' "failed"
		# printf "input:\n"
		# cat "test/test${i}.in"
		# printf "your output:\n"
		# cat "test/output${i}.out"
		# printf "expected output:\n"
		# cat "test/test${i}.out"
		# printf "\n"
		((failCount++))
	fi
	((totalElapsed += end - start))
	printf "\n%.3f%s\n" $(((end - start) / (10 ** 6)))e-3 s
done
if [ "$failCount" = 0 ]; then
	printf '\e[32m%s\e[0m\n' "all ok"
else
	printf '\e[32m%d %s\e[0m\n' "$okCount" "ok"
	printf '\e[31m%d %s\e[0m\n' "$failCount" "failed"
fi
printf "\nTotal elapsed: \n%.3f%s\n" $(((totalElapsed) / (10 ** 6)))e-3 s
