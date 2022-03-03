
from config import STORM_CLUSTERING_SERVER, STORM_CLUSTERING_PORT
from fastapi import FastAPI
import httpx
import logging
from models import ClientRequest
from nexrad import NexRad
from sys import stdout
from typing import List
import ujson
import uvicorn

logger = logging.getLogger()
log_fhandler = logging.FileHandler('ingestor.log', mode='a')
log_shandler = logging.StreamHandler(stdout)
formatter = logging.Formatter(
    '%(levelname)s %(asctime)s - %(message)s')
log_shandler.setFormatter(formatter)
log_fhandler.setFormatter(formatter)
logger.addHandler(log_fhandler)
logger.addHandler(log_shandler)

app = FastAPI()


def send_message(coords: List[float]):
    with httpx.Client() as client:
        response = client.post(
            httpx.URL(f'{STORM_CLUSTERING_SERVER}:{STORM_CLUSTERING_PORT}'), json=coords)
        response.raise_for_status()
        return response.json()

# creates an object of Nexrad class
# validates the received json data
# posts the data to Database Management service


def saveurl(data):
    if data is not None:
        nexRad = NexRad(data)
        return nexRad.validate()


@app.post('/')
async def serve(request: ClientRequest) -> List[float]:
    try:
        logger.debug(f'Incoming request: {request}')
        if not saveurl(request):
            logger.error('NEXRAD parameter validation failed.')

        response = send_message(coords=request.coords)
        return response
    except Exception as e:
        logger.error(e)
        return e

if __name__ == "__main__":
    uvicorn.run(app, host='0.0.0.0', port=8080)
