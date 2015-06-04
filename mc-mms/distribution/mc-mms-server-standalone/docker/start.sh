
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

if [ -z "${KEYSTORE}" ]; then
	KEYSTOREP=""
else
	KEYSTOREP="-keystore ${KEYSTORE}"
fi

if [ -z "${KEYSTORE_PASSWORD}" ]; then
	KEYSTORE_PASSWORDP=""
else
	KEYSTORE_PASSWORDP="-keystorePassword ${KEYSTORE_PASSWORD}"
fi

if [ -z "${TRUSTSTORE}" ]; then
	TRUSTSTOREP=""
else
	TRUSTSTOREP="-truststore ${TRUSTSTORE}"
fi

if [ -z "${TRUSTSTORE_PASSWORD}" ]; then
	TRUSTSTORE_PASSWORDP=""
else
	TRUSTSTORE_PASSWORDP="-truststorePassword ${TRUSTSTORE_PASSWORD}"
fi

java -classpath $LATEST  net.maritimecloud.mms.server.Main \
   -port 43234 -rest 9090 \
   $ACCESS_LOGP $ACCESS_LOG_FORMATP $SECURE_PORTP \
   $KEYSTOREP $KEYSTORE_PASSWORDP \
   $TRUSTSTOREP $TRUSTSTORE_PASSWORDP

