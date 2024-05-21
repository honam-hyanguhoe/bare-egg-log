FROM node:lts-alpine as builder

# timezone 설정
RUN apk add --update tzdata
ENV TZ=Asia/Seoul

WORKDIR /web

ENV PATH /web/node_modules/.bin:$PATH

COPY package.json /web/package.json

RUN npm install

COPY . ./

EXPOSE 3000
