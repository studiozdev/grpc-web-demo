FROM grpc-web-demo_js as JS

FROM nginx:1.18.0

ARG BASEDIR=/usr/src/base
RUN mkdir -p /var/www/html/dist
COPY --from=JS ${BASEDIR}/dist/main.js /var/www/html/dist/main.js

COPY index.html /var/www/html/index.html
COPY errors.grpc_conf /etc/nginx/conf.d/
COPY grpc_gateway.conf /etc/nginx/conf.d/default.conf

RUN ln -sf /dev/stdout /var/log/nginx/grpc_log_json.log
