FROM java:8

RUN wget --progress=bar "https://dma.ci.cloudbees.com/job/MaritimeCloud/lastSuccessfulBuild/artifact/*zip*/archive.zip" -O /archive.zip
RUN unzip /archive.zip -d /
ADD ./start.sh /start.sh
EXPOSE 43234 43235 9090
CMD ["/bin/bash", "/start.sh"]
