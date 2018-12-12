#!/bin/bash


for i in {000000..999999}; do

	password=$( ./keygen <<< ${i} | tail -1 | cut -d ' ' -f4 )
	printf "${i}\n${password}" | ./8

done
