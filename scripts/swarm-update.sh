wget -q $1 -O docker-stack-bot.yml
echo "Deploying new tg-client stack"
docker stack rm tg-client
docker stack deploy --compose-file docker-stack-bot.yml --with-registry-auth tg-client