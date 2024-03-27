FROM ubuntu:latest
LABEL authors="sonwabo"

ENTRYPOINT ["top", "-b"]