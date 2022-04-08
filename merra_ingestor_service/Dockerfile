FROM python:3.9-slim-bullseye

ARG SERVER_ADDRESS
ARG USER_NAME
ARG PASSWORD

WORKDIR /usr/src/app
COPY merra_ingestor_service/* .
RUN mkdir netCDF
RUN mkdir zarr

RUN pip install --no-cache-dir --upgrade -r requirements.txt

RUN touch $HOME/.netrc 
RUN echo "machine ${SERVER_ADDRESS} login ${USER_NAME} password ${PASSWORD}" >> $HOME/.netrc
RUN chmod 0600 $HOME/.netrc
RUN touch $HOME/.urs_cookies

EXPOSE 8080

CMD [ "python3", "app.py" ]
