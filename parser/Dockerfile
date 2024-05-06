# backend/Dockerfile
# pull official base image
FROM python:3.10.6-slim-buster

# set working directory
WORKDIR /parser

# set environment variables
ENV PYTHONDONTWRITEBYTECODE 1
ENV PYTHONUNBUFFERED 1

# install system dependencies
RUN apt-get update \
  && apt-get -y install netcat gcc \
  && apt-get -y install default-libmysqlclient-dev \
  && apt-get clean

# install python dependencies
RUN pip install --upgrade pip \
  && pip install --upgrade setuptools
COPY requirements.txt .
RUN pip install -r requirements.txt

# add app
COPY ./app /parser
