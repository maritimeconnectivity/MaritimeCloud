

if [ -z "${BINARY}" ]; then
	BINARYP=""
else
	BINARYP="-Dnet.maritimecloud.mms.use_binary=${BINARY}"
fi

LATEST=`ls /archive/mc-mms/distribution/mc-mms-server-standalone/target/mc-mms-server-standalone-*SNAPSHOT.jar`

java -classpath $LATEST net.maritimecloud.mms.server.Main -port 43234 -rest 9090 BINARYP

