#!/usr/bin/env bash
ansible-playbook -i hosts --private-key /mnt/c/Users/matth/Downloads/OctopusVirginia.pem --tags bamboo6 bamboo.yml