server:
	cd ./javacardback && java -cp .:gson-2.10.1.jar CardGameServer

build:
	cd ./javacardback && javac -cp .:gson-2.10.1.jar CardGameServer.java

client:
	npm run dev
