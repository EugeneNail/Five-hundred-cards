start-server:
	cd ./javacardback && java -cp .:gson-2.10.1.jar CardGameServer

build-server:
	cd ./javacardback && javac -cp .:gson-2.10.1.jar CardGameServer.java

start-client:
	cd ./javacardfront && npm run dev
