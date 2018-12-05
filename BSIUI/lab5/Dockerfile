FROM krystiankolad/asm-x86-docker

RUN sudo dpkg --add-architecture i386 && \
	sudo apt-get update && \
	sudo apt-get install libstdc++6:i386 libc6:i386 libncurses5:i386 -y

COPY ./src/. /workspace
