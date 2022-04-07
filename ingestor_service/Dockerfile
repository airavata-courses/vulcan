FROM continuumio/miniconda3:4.10.3p1

WORKDIR /usr/src/app

COPY environment.yml .

RUN conda env create -f environment.yml

SHELL ["conda", "run", "-n", "nexrad-ingestor", "/bin/bash", "-c"]

COPY *.py .

EXPOSE 8080

ENTRYPOINT ["conda", "run", "-n", "nexrad-ingestor", "python3", "app.py"]
