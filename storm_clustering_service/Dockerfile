FROM python:3.9

WORKDIR /usr/src/app

COPY requirements.txt ./

RUN pip install --no-cache-dir --upgrade -r requirements.txt

COPY . .

EXPOSE 46283

CMD [ "python", "main.py" ]
