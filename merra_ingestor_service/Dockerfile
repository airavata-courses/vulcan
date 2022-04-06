FROM python:3.9-slim-bullseye

ARG SERVER_ADDRESS
ARG USER_NAME
ARG PASSWORD

WORKDIR /usr/src/app
COPY requirements.txt ./

RUN pip install --no-cache-dir --upgrade -r requirements.txt

RUN touch $HOME/.netrc 
RUN echo "machine ${SERVER_ADDRESS} login ${USER_NAME} password ${PASSWORD}" >> $HOME/.netrc
RUN chmod 0600 $HOME/.netrc
RUN touch $HOME/.urs_cookies

# Need to create a directory for netcdf files
RUN mkdir netcdf
# Need to create a directory for zarr files
RUN mkdir zarr

# Can these two directories be mounted to the host machine?, so download directly to the host machine?
COPY . .

EXPOSE 3000

CMD [ "python3", "app.py" ]
