FROM python:3.9-slim-bullseye

WORKDIR /usr/src/app
COPY requirements.txt ./

RUN pip install --no-cache-dir --upgrade -r requirements.txt

RUN touch $HOME/.netrc 
RUN echo "machine urs.earthdata.nasa.gov login Aksh_Ace password Vulcanmerra@1" >> $HOME/.netrc
RUN chmod 0600 $HOME/.netrc
RUN touch $HOME/.urs_cookies

COPY . .

EXPOSE 8080

CMD [ "python3", "app.py" ]