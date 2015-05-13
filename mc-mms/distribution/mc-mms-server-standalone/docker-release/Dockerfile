FROM java:8

ADD ./start.sh /start.sh
ADD ./mc-mms-server-standalone-*.jar /archive/mc-mms/distribution/mc-mms-server-standalone/target/
EXPOSE 43234 43235 9090
CMD ["/bin/bash", "/start.sh"]

