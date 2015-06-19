
LATEST=`ls /archive/mc-mms/distribution/mc-mms-server-standalone/target/mc-mms-server-standalone-*.jar | grep -v sources | grep -v javadoc`

if [ -z "${ACCESS_LOG}" ]; then
	ACCESS_LOGP=""
else
	ACCESS_LOGP="-accessLog ${ACCESS_LOG}"
fi

if [ -z "${ACCESS_LOG_FORMAT}" ]; then
	ACCESS_LOG_FORMATP=""
else
	ACCESS_LOG_FORMATP="-accessLogFormat ${ACCESS_LOG_FORMAT}"
fi

if [ -z "${SECURE_PORT}" ]; then
	SECURE_PORTP=""
else
	SECURE_PORTP="-securePort 43235"
fi

if [ -z "${CONF}" ]; then
	CONFP=""
else
	CONFP="-conf ${CONF}"
fi

java -classpath $LATEST  net.maritimecloud.mms.server.Main \
   -port 43234 \
   $ACCESS_LOGP $ACCESS_LOG_FORMATP $SECURE_PORTP \
   $CONFP

