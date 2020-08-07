FROM alpine:3

LABEL maintainer="seungjin_lee2@tmax.co.kr"

RUN apk update ; \
	apk upgrade ; \
	apk add openjdk8 ; \
	apk add openssl ; \
	apk add bash
	
ADD ./notebook-controller /home/tmax/notebook-controller
ADD ./start.sh /home/tmax/
RUN chmod 775 -R /home/tmax/notebook-controller ; \
	chmod 775 /home/tmax/start.sh ; \
	sed -i 's/\r$//' /home/tmax/start.sh

CMD ["/bin/sh", "-c", "/home/tmax/start.sh"]