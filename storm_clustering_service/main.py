from sys import stdout
from fastapi import FastAPI
from models import ForecastGeoJson
import httpx
import logging
import random
from typing import List
import uvicorn
import ujson

with open('config.json') as config:
    CONFIG = ujson.load(config)

logger = logging.getLogger()
log_fhandler = logging.FileHandler('clustering.log', mode='a')
log_shandler = logging.StreamHandler(stdout)
formatter = logging.Formatter(
    '%(levelname)s %(asctime)s - %(message)s')
log_shandler.setFormatter(formatter)
log_fhandler.setFormatter(formatter)
logger.addHandler(log_fhandler)
logger.addHandler(log_shandler)

app = FastAPI()


def send_message(coords: List[List[List[float]]]):
    with httpx.Client() as client:
        response = client.post(
            CONFIG['forecast_service'], json=coords)
        return response.json()


@app.post('/')
async def serve(coordinates: List[float]) -> ForecastGeoJson:
    try:
        logger.debug(f'Incoming request: {coordinates}')
        assert isinstance(coordinates, list) and len(
            coordinates) == 2, "Incorrect structuring for coordinates."
        new_coordinates = []
        long, lat = coordinates
        for _ in range(100):
            # Mock coordinates from initial coordinates received by data ingestor
            coords = (long + random.uniform(-0.5, 0.5),
                      lat + random.uniform(-0.5, 0.5))
            new_coordinates.append(coords)

        response = send_message([new_coordinates])
        print(response)
        return response

    except Exception as e:
        logger.error(e)
        return e

if __name__ == '__main__':
    logger.setLevel(logging.DEBUG)
    uvicorn.run(app, host='0.0.0.0', port=46283)
