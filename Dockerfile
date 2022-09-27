FROM maven:3.8.6-jdk-11
ADD . /
RUN sh run.sh
CMD sh run.sh
