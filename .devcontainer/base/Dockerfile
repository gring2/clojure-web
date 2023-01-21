FROM clojure:temurin-19-lein
#FROM cloure:lein

RUN apt update && apt install -y git npm rlwrap zsh curl
RUN curl -O https://download.clojure.org/install/linux-install-1.11.1.1208.sh
RUN chmod +x linux-install-1.11.1.1208.sh
RUN sudo ./linux-install-1.11.1.1208.sh
USER root
ENTRYPOINT [ "zsh" ]