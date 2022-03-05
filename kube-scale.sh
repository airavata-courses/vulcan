#!/bin/bash

scale=false

while getopts ":r:" opt;do
  case $opt in
    r)
      replicas="$OPTARG"
      scale=true;;
    esac
done

if (scale==true)
then

  echo "scaling the the deployments to ${replicas} sets"
  
  deployments=("http-cluster http-forecast http-gateway http-ingestor postgres user-management user-history")

  for deploy in $deployments;
  do
  echo $deploy
  kubectl scale deployment $deploy --replicas=$replicas
  done
  
  echo "Application successfully scaled!!"
else
  echo "invalid parameters. Please check the command entered."
fi
