### UDP Messenger

you need docker to test

## build docker

```shell script

docker build -t udp-messenger . 

```

## run server

run this code only once to start the server

```shell script

docker network create --subnet=172.18.0.0/16 messengerNetwork
docker run -it --net messengerNetwork --ip 172.18.0.2 -p 9875:9875 udp-messenger java -cp out/production/UDPMessenger UDPServer

```


## run client 

run this code for as many clients as you want


```shell script

docker run -it --net messengerNetwork udp-messenger

```