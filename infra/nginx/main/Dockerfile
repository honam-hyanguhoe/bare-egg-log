FROM nginx:latest

ENV TZ=Asia/Seoul

RUN rm -rf /etc/nginx/conf.d/default.conf
COPY nginx.conf /etc/nginx/nginx.conf
COPY nginx_80.conf /etc/nginx/conf.d/nginx_80.conf
COPY nginx_443.conf /etc/nginx/conf.d/nginx_443.conf

CMD ["nginx", "-g", "daemon off;"]
