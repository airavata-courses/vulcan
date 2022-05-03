from io import BytesIO
import zipfile
from fastapi import FastAPI
from fastapi.responses import StreamingResponse
import logging
from geomap_service import GeoMapService
from radar_service import RadarService
from models import RadarRequest
from sys import stdout
import uvicorn

logger = logging.getLogger()
log_fhandler = logging.FileHandler('nexrad_ingestor.log', mode='a')
log_shandler = logging.StreamHandler(stdout)
formatter = logging.Formatter(
    '%(levelname)s %(asctime)s - %(message)s')
log_shandler.setFormatter(formatter)
log_fhandler.setFormatter(formatter)
logger.addHandler(log_fhandler)
logger.addHandler(log_shandler)

app = FastAPI()


@app.post('/')
async def serve(request: RadarRequest):
    try:
        logger.debug(f'Incoming request: {request}')
        rs = RadarService()
        query = rs.get_query(request.start_time, request.end_time,
                             request.longitude, request.latitude)
        catalog = rs.get_catalog(query)

        zip_io = BytesIO()
        with zipfile.ZipFile(zip_io, mode='a', compression=zipfile.ZIP_DEFLATED) as zip_file:
            if catalog:
                gms = GeoMapService(request.longitude, request.latitude)
                ref_plots = GeoMapService.plot_concurrent(
                    catalog, gms.plot_relectivity)
                for file_name, buffer in ref_plots:
                    logger.debug(file_name)
                    zip_file.writestr(file_name, buffer.read())
                    buffer.close()
                catalog.session.close()

        return StreamingResponse(
            iter([zip_io.getvalue()]),
            media_type='application/x-zip-compressed',
            headers={
                'Content-Disposition': f'attachment; filename=image_collection.zip'}
        )
    except TypeError:
        # Ignoring TypeErrors occurring due to TDSCatalog.__del__
        pass
    except Exception as e:
        logger.error(e)
        return e

if __name__ == "__main__":
    uvicorn.run(app, host='0.0.0.0', port=8080)
