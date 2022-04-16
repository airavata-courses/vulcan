FROM continuumio/miniconda3 AS build

COPY environment.yml .

RUN conda env create -f environment.yml

RUN conda install -c conda-forge conda-pack

RUN conda-pack -n nexrad-ingestor -o /tmp/env.tar && \
    mkdir /venv && cd /venv && tar xf /tmp/env.tar && \
    rm /tmp/env.tar

RUN /venv/bin/conda-unpack

FROM debian:bullseye-slim AS runtime

COPY --from=build /venv /venv

COPY *.py .

SHELL ["/bin/bash", "-c"]

ENTRYPOINT source /venv/bin/activate && \
    python app.py
